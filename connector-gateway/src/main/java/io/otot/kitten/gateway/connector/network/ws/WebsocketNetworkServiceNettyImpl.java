package io.otot.kitten.gateway.connector.network.ws;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.Attribute;
import io.netty.util.CharsetUtil;
import io.otot.kitten.gateway.connector.core.NamedThreadFactory;
import io.otot.kitten.gateway.connector.network.NetworkAuthService;
import io.otot.kitten.gateway.connector.network.NetworkEventHandler;
import io.otot.kitten.gateway.connector.network.NetworkService;
import io.otot.kitten.gateway.connector.network.SessionChannel;
import io.otot.kitten.gateway.connector.session.LocalSessionManage;
import io.otot.kitten.gateway.connector.utils.TimeTools;
import io.otot.kitten.gateway.connector.utils.URITools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/***
 * websocke 的网络层实现
 * @author fireflyhoo
 */
@Service
public class WebsocketNetworkServiceNettyImpl implements NetworkService {


    private final static Logger LOGGER = LoggerFactory.getLogger(WebsocketNetworkServiceNettyImpl.class);
    private static Charset UTF_8 = StandardCharsets.UTF_8;

    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workGroup;
    private ServerBootstrap serverBootstrap;
    private ChannelFuture channelFuture;

    /***
     * 服务监听端口
     */
    private int port = -1;

    /***
     * 消息处理器
     */
    private NetworkEventHandler hander;

    /***
     * 认证服务
     */
    @Autowired
    private NetworkAuthService authService;


    @Autowired
    private LocalSessionManage localSessionManage;

    public int getPort() {
        return port;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }


    @Override
    public void start() {
        // XXX 待优化性能
        bossGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2, new NamedThreadFactory("websocket-netty-boss-group"));
        workGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 4, new NamedThreadFactory("websocket-netty-work-group"));
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new HttpServerCodec());
                        pipeline.addLast(new HttpObjectAggregator(8192 * 1000));
                        pipeline.addLast(new ChunkedWriteHandler());
                        pipeline.addLast(new WebSocketHandler(hander, authService));
                    }
                });
        try {
            channelFuture = serverBootstrap.bind(new InetSocketAddress(port)).sync();
            LOGGER.info("WebSocket Server listening in: {}", port);
        } catch (Exception e) {
            LOGGER.error("启动服务线程出现异常", e);
        }
    }

    @Override
    public void stop() {
        if (channelFuture != null) {
            try {
                channelFuture.channel().close().get();
            } catch (Throwable e) {
                LOGGER.error("close channel close error:", e);
            }

            if (bossGroup != null) {
                try {
                    bossGroup.shutdownGracefully().get();
                } catch (Throwable e) {
                    LOGGER.error("bossGroup.shutdownGracefully error:", e);
                }
            }

            if (workGroup != null) {
                try {
                    workGroup.shutdownGracefully().get();
                } catch (Throwable e) {
                    LOGGER.error("workGroup.shutdownGracefully error:", e);
                }
            }
        }

    }

    @Override
    public void setEventHandler(NetworkEventHandler handler) {
        this.hander = handler;
    }




    class WebSocketHandler extends SimpleChannelInboundHandler<Object> {


        /***
         * websocket的处理URL
         */
        private static final String WS_URL = "/socket-io";

        private final NetworkEventHandler networkEventHandler;


        private final NetworkAuthService authService;


        public WebSocketHandler(NetworkEventHandler networkEventHandler, NetworkAuthService authService) {
            this.networkEventHandler = networkEventHandler;
            this.authService = authService;
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof FullHttpRequest) {
                handleHttpRequest(ctx, (FullHttpRequest) msg);
            } else if (msg instanceof WebSocketFrame) {
                handleWebSocketFrame(ctx, (WebSocketFrame) msg);
            }
        }


        private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) throws URISyntaxException {
            if (!request.decoderResult().isSuccess()) {
                sendErrorResponse(ctx, HttpResponseStatus.BAD_REQUEST, "数据包未完成");
                return;
            }
            URI url = new URI(request.uri());
            if (WS_URL.equals(url.getPath())) {
                URITools.UrlEntity entity = URITools.parse(request.uri());
                String appKey = entity.getQuery("appKey");
                String token = entity.getQuery("token");
                if (appKey == null) {
                    sendErrorResponse(ctx, HttpResponseStatus.BAD_REQUEST, "未传入参数:appKey");
                    return;
                }
                if (token == null) {
                    sendErrorResponse(ctx, HttpResponseStatus.BAD_REQUEST, "未传入参数:token");
                    return;
                }
                try {
                    String userKey = authService.auth(appKey, token);
                    Attribute<String> _appKey = ctx.channel().attr(Constants.appKey);
                    Attribute<String> _userKey = ctx.channel().attr(Constants.userKey);
                    Attribute<String> _sessionKey = ctx.channel().attr(Constants.key);
                    _appKey.set(appKey);
                    _userKey.set(userKey);
                    _sessionKey.set(UUID.randomUUID().toString());
                    WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                            "ws:/" + ctx.channel() + "/socket-io", null, false);
                    WebSocketServerHandshaker webSocketServerHandshaker = wsFactory.newHandshaker(request);
                    if (webSocketServerHandshaker == null) {
                        WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
                    } else {
                        webSocketServerHandshaker.handshake(ctx.channel(), request);
                    }
                    hander.onConnect(new WebSocketSessionChannel(ctx.channel()));
                } catch (Exception e) {
                    LOGGER.error("认证出现异常 appKey:{},token:{}", appKey, token, e);
                    sendErrorResponse(ctx, HttpResponseStatus.BAD_REQUEST, "认证未成功请联系管理员!");
                }
            } else {
                sendErrorResponse(ctx, HttpResponseStatus.FORBIDDEN, "禁止访问!");
            }
        }

        private void handleWebSocketFrame(ChannelHandlerContext channelHandlerContext, WebSocketFrame msg) throws Exception {
            try{
                Attribute<String> att = channelHandlerContext.channel().attr(Constants.key);
                String channelKey = att.get();
                SessionChannel sessionChannel  = new WebSocketSessionChannel(channelHandlerContext.channel());
                if (msg instanceof TextWebSocketFrame || msg instanceof BinaryWebSocketFrame) {
                    sessionChannel.setLastActivityTime(TimeTools.currentTimeMillis());
                    ByteBuf data = msg.content();
                    byte[] body = new byte[msg.content().readableBytes()];
                    msg.content().getBytes(0, body);
                    networkEventHandler.onMessage(sessionChannel,body);
                } else if (msg instanceof CloseWebSocketFrame) {
                    networkEventHandler.onClose(sessionChannel);
                } else if (msg instanceof PingWebSocketFrame) {
                    channelHandlerContext.writeAndFlush(new PingWebSocketFrame());
                } else if (msg instanceof PongWebSocketFrame) {
                    sessionChannel.setLastActivityTime(TimeTools.currentTimeMillis());
                } else if (msg instanceof ContinuationWebSocketFrame) {
                    LOGGER.warn("收到未完成数据包 ContinuationWebSocketFrame {}", msg);
                }
            }catch (Throwable e){
                LOGGER.error("处理客户端发送的数据包出现异常",e);
                sendErrorResponse(channelHandlerContext, HttpResponseStatus.BAD_REQUEST,  "发送的数据有误!");
            }
        }


        private void sendErrorResponse(ChannelHandlerContext ctx, HttpResponseStatus status, String msg) {
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);
            ByteBuf buf = Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8);
            response.content().writeBytes(buf);
            buf.release();
            ChannelFuture f = ctx.channel().writeAndFlush(response);
            f.addListener(ChannelFutureListener.CLOSE);
        }

        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
            Attribute<String> att = ctx.channel().attr(Constants.key);
            String channelKey = att.get();
            if(channelKey!= null){
                SessionChannel sessionChannel = localSessionManage.get(channelKey);
                if (sessionChannel == null) {
                    sessionChannel = new WebSocketSessionChannel(ctx.channel());
                }
                hander.onClose(sessionChannel);
            }else{
                LOGGER.warn("连接泄露...");
            }
        }


    }
}

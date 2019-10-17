package io.otot.kitten.gateway.connector;

import io.otot.kitten.gateway.connector.core.ServiceLifecycle;
import io.otot.kitten.gateway.connector.kernel.BusinessService;
import io.otot.kitten.gateway.connector.network.NetworkService;
import io.otot.kitten.gateway.connector.network.ws.WebsocketNetworkServiceNettyImpl;
import io.otot.kitten.gateway.connector.queue.CommandQueueManager;

/**
 * 连接服务
 *
 * @author fireflyhoo
 */
public class ConnectorServer implements ServiceLifecycle {
    /***
     * 端口信息
     */
    private int port;


    /***
     * 网络服务
     */
    private NetworkService network;


    /***
     * 业务处理服务
     */
    private BusinessService businessService;


    /***
     * 任务队列
     */
    private CommandQueueManager queueManager;


    public ConnectorServer(int port) {
        this.port = port;
        WebsocketNetworkServiceNettyImpl net  = new WebsocketNetworkServiceNettyImpl();
        net.setPort(port);
        net.setEventHandler(businessService);

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}

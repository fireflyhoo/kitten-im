package io.otot.kitten.gateway.connector;

import io.otot.kitten.gateway.connector.network.NetworkEventHandler;
import io.otot.kitten.gateway.connector.network.NetworkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 连接服务
 *
 * @author fireflyhoo
 */
@Component
public class ConnectorServerRunner implements ApplicationRunner {

    private static  final Logger LOGGER = LoggerFactory.getLogger(ConnectorServerRunner.class);

    /***
     * 端口信息
     */
    @Value("${connector.port}")
    private int port;


    /***
     * 网络服务
     */
    @Autowired
    private NetworkService network;


    /***
     * 业务处理服务
     */
    @Autowired
    private NetworkEventHandler businessService;




    @Override
    public void run(ApplicationArguments args) throws Exception {
        businessService.start();
        network.setPort(port);
        network.setEventHandler(businessService);
        network.start();
        LOGGER.info("network start in 0.0.0.0:{}",port);
    }
}

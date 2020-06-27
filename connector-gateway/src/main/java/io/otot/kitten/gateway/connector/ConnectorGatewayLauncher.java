package io.otot.kitten.gateway.connector;

import io.otot.kitten.gateway.connector.session.SessionManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/***********
 * 长连接网关服务
 * @author fireflyhoo
 */
@SpringBootApplication
public class ConnectorGatewayLauncher {

    @Autowired
    private SessionManage sessionManage;

    public static void main(String[] args) {
        SpringApplication.run(ConnectorGatewayLauncher.class, args);
    }
}

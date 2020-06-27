package io.otot.kitten.gateway.connector;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/***********
 * 长连接网关服务
 * @author fireflyhoo
 */
@SpringBootApplication
public class ConnectorGatewayLauncher {


    public static void main(String[] args) {
        SpringApplication.run(ConnectorGatewayLauncher.class, args);
    }
}

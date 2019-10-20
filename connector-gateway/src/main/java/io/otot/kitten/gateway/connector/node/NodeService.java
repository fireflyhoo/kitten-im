package io.otot.kitten.gateway.connector.node;

/***
 * 节点服务
 * @author fireflyhoo
 */
public interface NodeService {


    /***
     * 获取节点客户端用于和节点间通信
     * @param serviceKey
     * @return
     */
    NodeClient getNodeClient(String serviceKey);


    /****
     * 注册当前节点
     * @param host
     * @param port
     * @return
     */
    String registerNode(String host,int port);


}

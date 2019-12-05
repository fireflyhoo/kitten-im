package io.otot.kttten.service.store.config;

/****
 * 存储服务配置信息
 */
public class StoreConfig {

    /***
     * 端口
     */
    private int port;


    /***
     * 存储地址
     */
    private String dataPath;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }
}

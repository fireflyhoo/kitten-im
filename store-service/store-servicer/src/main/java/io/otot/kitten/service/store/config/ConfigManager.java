package io.otot.kitten.service.store.config;


import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

public enum ConfigManager {
    INSTANCE;

    private StoreConfig config;

    ConfigManager() {
        initer();
    }

    private void initer() {
        //初始化配置
        Yaml yaml = new Yaml();
        InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream("store-config.yml");
        this.config = yaml.loadAs(input, StoreConfig.class);
    }


    public StoreConfig getConfig() {
        return config;
    }
}

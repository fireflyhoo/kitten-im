package io.otot.kitten.service.store.rocksdb;

import io.otot.kitten.service.store.StoreService;
import io.otot.kitten.service.store.config.ConfigManager;
import io.otot.kitten.service.store.config.StoreConfig;
import io.otot.kitten.service.store.utils.ByteTool;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

public class StoreServiceImpl implements StoreService {

    private volatile RocksDB rocksDB;


    @Override
    public void start() {
        StoreConfig config = ConfigManager.INSTANCE.getConfig();
        Options options = new Options();
        options.setTargetFileSizeBase(1024*1024*64);
        options.setCreateIfMissing(true);
        try {
            RocksDB db = RocksDB.open(options, config.getDataPath());
            rocksDB = db;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        rocksDB.close();
    }

    @Override
    public void put(long key, byte[] data) {
        try {
            rocksDB.put(ByteTool.toByteArray(key),data);
        } catch (RocksDBException e) {
            throw new  RuntimeException(e);
        }
    }

    @Override
    public byte[] get(long key) {
        try {
            return rocksDB.get(ByteTool.toByteArray(key));
        } catch (RocksDBException e) {
            throw new  RuntimeException(e);
        }
    }

    @Override
    public void remove(long key) {
        try {
            rocksDB.delete(ByteTool.toByteArray(key));
        } catch (RocksDBException e) {
            throw new  RuntimeException(e);
        }
    }

}

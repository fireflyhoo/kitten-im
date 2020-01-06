package io.otot.kitten.service.store.rocksdb;

import io.otot.kitten.service.store.StoreService;
import io.otot.kitten.service.store.config.ConfigManager;
import io.otot.kitten.service.store.config.StoreConfig;
import io.otot.kitten.service.store.utils.ByteTool;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

public class StoreServiceImpl implements StoreService {

     private static final int  DB_NUMBER   = 7;

    private volatile RocksDB[] rocksDbS = new RocksDB[DB_NUMBER];



    @Override
    public void start() {
        StoreConfig config = ConfigManager.INSTANCE.getConfig();
        Options options = new Options();
      //  options.setTargetFileSizeBase(1024*1024*64);
        options.setCreateIfMissing(true);
        try {
            for(int i = 0; i< DB_NUMBER; i++){
                RocksDB db = RocksDB.open(options, config.getDataPath()+"/dataset"+i);
                rocksDbS[i] = db;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        for (int i=0; i< rocksDbS.length;i++){
            rocksDbS[i].close();
        }
    }

    @Override
    public void put(long key, byte[] data) {
        try {
            rocksDbS[(int) (key%DB_NUMBER)].put(ByteTool.toByteArray(key),data);
        } catch (RocksDBException e) {
            throw new  RuntimeException(e);
        }
    }

    @Override
    public byte[] get(long key) {
        try {
            return rocksDbS[(int) (key%DB_NUMBER)].get(ByteTool.toByteArray(key));
        } catch (RocksDBException e) {
            throw new  RuntimeException(e);
        }
    }

    @Override
    public void remove(long key) {
        try {
            rocksDbS[(int) (key%DB_NUMBER)].delete(ByteTool.toByteArray(key));
        } catch (RocksDBException e) {
            throw new  RuntimeException(e);
        }
    }

}

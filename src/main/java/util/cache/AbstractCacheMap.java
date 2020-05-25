package util.cache;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

//抽象
abstract class AbstractCacheMap<K,V> implements CacheImpl<K,V> {
    class CacheObject<K2,V2>{
        CacheObject(K2 key, V2 value) {
            this.key = key;
            this.cacheObject = value;
        }

        final K2 key;
        final V2 cacheObject;

        V2 getObject() {
            return cacheObject;
        }
    }
    protected Map<K,CacheObject<K,V>> cacheMap;
    private final ReentrantReadWriteLock cachelock = new ReentrantReadWriteLock();
    private final Lock readLock = cachelock.readLock(); //加锁
    private final Lock writeLock = cachelock.writeLock(); //改锁

    protected int cacheSize; //缓存大小，[0,∞)/条
    protected boolean exisCustomExpire;//判断是否有设置过默认过期时间

    public int getFullSize(){
        return cacheSize;
    }
    //构造方法
    public AbstractCacheMap(int cacheSize){
        this.cacheSize = cacheSize;
    }
    public void put(K key,V value){
        //开始操作开启锁定
        writeLock.lock();
        try {
            CacheObject<K,V> co = new CacheObject<K, V>(key,value);
            cacheMap.put(key,co);
        }
        finally {
            //结束操作结束锁定
            writeLock.unlock();
        }
    }
    /**
     * {@inheritDoc}
     * */
    public  V get(K key){
        //开始执行加锁
        readLock.lock();
        try {
            CacheObject<K,V> co = cacheMap.get(key);
            if(null == co){
                return null;
            }
            return co.getObject();
        }finally {
            //结束操作，解锁
            readLock.unlock();
        }
    }

    public Set<K> getKeys(){
        readLock.lock();
        return cacheMap.keySet();
    }

    public boolean isFull(){
        if (0 == cacheSize){
            return false;
        }
        return cacheMap.size() >= cacheSize;
    }

    public void rm(K key){
        writeLock.lock();
        try{
            cacheMap.remove(key);
        }
        finally {
            writeLock.unlock();
        }
    }

    public void clear(){
        writeLock.lock();
        try {
            cacheMap.clear();
        }finally {
            writeLock.unlock();
        }
    }
    public int size(){
        return cacheMap.size();
    }
    public boolean isEmpty(){
        return size() == 0;
    }
}

package util.LRUCache;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

//抽象
abstract class AbstractCacheMap<K,V> implements LRUCacheImpl<K,V> {
    class CacheObject<K2,V2>{
        CacheObject(K2 key, V2 value, Long ttl) {
            this.key = key;
            this.cacheObject = value;
            this.ttl = ttl;
            this.lastAccess = System.currentTimeMillis(); //时间类
        }

        final K2 key;
        final V2 cacheObject;
        long lastAccess;		// 最后访问时间
        long accessCount;		// 访问次数
        long ttl;				// 对象存活时间(time-to-live)

        boolean isExpired() {
            if (ttl == 0) {
                return false;
            }
            return lastAccess + ttl < System.currentTimeMillis(); //时间比对
        }
        V2 getObject() {
            lastAccess = System.currentTimeMillis();
            accessCount++;
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
    protected long defaultExpire; //默认过期时间[0,∞)/ms
    //构造方法
    public AbstractCacheMap(int cacheSize, long defaultExpire){
        this.cacheSize = cacheSize;
        this.defaultExpire = defaultExpire;
    }
    public long defaultLifeTime(){
        return defaultExpire;
    }
    //判断是否需要清理
    protected boolean isNeedClearExpiredObject(){
        return defaultExpire > 0 || exisCustomExpire;
    }

    public void put(K key,V value){
        put(key, value,defaultExpire);
    }
    public void put(K key,V value,long expire){
        //开始操作开启锁定
        writeLock.lock();
        try {
            CacheObject<K,V> co = new CacheObject<K, V>(key,value,expire);
            if (0 != expire){
                exisCustomExpire = true;
            }
            if (isFull()){
                eliminate();
            }
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
            }if (co.isExpired() == true){
                cacheMap.remove(key);
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

    public final int eliminate() {
        writeLock.lock();
        try {
            return eliminateCache();
        }
        finally {
            writeLock.unlock();
        }
    }

    /**
     * 淘汰对象的实现
     * @return
     * */
    protected abstract int eliminateCache();

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

package util.LRUCache;

import java.util.Set;

/**
 * Created by AmamiyaAsuka on 2017/7/25. from jodd.cache.LRU
 * 设置一个LRU的缓存
 * 这个缓存会抛弃超过存活时间没有被调用过的对象，或抛弃超过划定存储大小时抛弃在链表中最末端的对象
 * 缓存大小是缓存数据对象数目，不是内存大小，缓存时间为毫秒
 */
//接口
public interface LRUCacheImpl<K,V>{
    /**
     * 获取当前缓存大小
     * @return
     * */
    int size();
    /**
     * 获取存活时间
     * */
    long defaultLifeTime();
    /**
     * 向缓存添加value对象，默认存活时间
     * @param key
     * @param value
     * */
    void put(K key, V value);
    /**
     * 向缓存添加value对象，并且添加一个存活时间
     * @param key
     * @param value
     * @param expire
     * */
    void put(K key, V value, long expire);
    /**
     * 查找缓存对象
     * @param key
     * @return
     * */
    V get(K key);
    /**
     * 获得全部key列表
     * @return
     * */
    Set<K> getKeys();
    /**
     * 淘汰对象
     * @return 被删除对象大小
     * */
    int eliminate();
    /**
     * 检查缓存是否已经被填满
     * */
    boolean isFull();
    /**
     * 删除缓存对象
     * @param key
     * */
    void rm(K key);
    /**
     * 清除所有缓存
     * */
    void clear();
    /**
     * 返回缓存总大小
     * @return
     * */
    int getFullSize();
    /**
     * 判断缓存是否为空
     * */
    boolean isEmpty();

}

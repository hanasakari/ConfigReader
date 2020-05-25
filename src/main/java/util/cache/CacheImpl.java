package util.cache;

import java.util.Set;

/**
 * Created by AmamiyaAsuka on 2020/5/25.
 * 移除了lru缓存，不会再删除了
 */
//接口
public interface CacheImpl<K,V>{
    /**
     * 获取当前缓存大小
     * @return
     * */
    int size();
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

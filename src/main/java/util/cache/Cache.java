package util.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 实现缓存
 * 超过默认大小后自动删除尾部缓存数据
 * 条和毫秒的单位
 */
public class Cache<K, V> extends AbstractCacheMap<K, V> {
    public Cache(int cacheSize) {
        super(cacheSize);
        //双向链表实现并保证线程安全
        this.cacheMap = new LinkedHashMap<K, CacheObject<K, V>>(cacheSize + 1, 1f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, CacheObject<K, V>> eldest) {
                return Cache.this.removeEldestEntry(eldest);
            }
        };
    }

    private boolean removeEldestEntry(Map.Entry<K, CacheObject<K, V>> eldest) {

        if (cacheSize == 0)
            return false;

        return size() > cacheSize;
    }
}

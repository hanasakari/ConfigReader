package entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.util.*;

import static util.CacheUtil.staticCache;

public class Config {
    //读取配置文件
    private static void init() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            //将yml读取为map
            Map map = mapper.readValue(Config.class.getClassLoader().getResourceAsStream("config.yml"), Map.class);
            for (Object key : map.keySet()) {
                staticCache.put(key.toString(), map.get(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取某个值
    public static String get(String key) {
        init();
        //根据.将string变更为list
        List<String> strings = new ArrayList<String>(Arrays.asList(key.split("\\.")));
        //如果list>1则进入递归查询
        if (strings.size() > 1) {
            try {
                Map<String, Object> map = new HashMap<>();

                List<String> keys = new ArrayList<String>();
                keys.addAll(staticCache.getKeys());
                for (String key_item : keys) {
                    map.put(key_item, staticCache.get(key_item));
                }
                return getMapItem(strings, map).toString();

            } catch (NullPointerException e) {
                System.err.println("is item not exist");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return staticCache.get(key).toString();
        }
        return null;
    }

    private static Object getMapItem(List<String> strings, Map data) throws Exception {
        for (String item : strings) {
            //每次调用删除本次使用值
            strings.remove(item);
            //如果被删空则直接返回获取的对象，如果还有则进入尾递归，减少栈帧损耗
            if (strings.size() == 0)
                return data.get(item).toString();
            else
                return getMapItem(strings, (Map) data.get(item));
        }
        return null;
    }
}

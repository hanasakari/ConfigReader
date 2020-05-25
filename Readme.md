##configReader
本项目LRU核心取自jodd.cache，修改了过期方法使内容无法过期。

可以读取yml文件，转换为Map<String,String> 格式来全局缓存属性内容，可以使用string.string的方式获取子属性
例如

config.yml
     
     get:
       data:
         "data_item"

在cacheUtil中初始化为静态方法

     public static Cache staticCache = new Cache(10000);

可以通过Config获取

     Config.get("get.data")
     
最终输出为 data_item
package cn.ymex.net.kits;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ymexc on 2018/7/5.
 */
public class LinkCallMap<V> extends HashMap<String, V> {


    public LinkCallMap<V> putPair(String key, V value) {
        put(key, value);
        return this;
    }


    public LinkCallMap<V> putAllPair(Map<? extends String, ? extends V> m) {
        putAll(m);
        return this;
    }
}

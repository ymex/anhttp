package cn.ymex.net.parser;


import java.lang.reflect.Type;

/**
 * @author ymexc
 */
public interface Parser<F, T> extends Convert<F, T> {
    /**
     * 设置泛型T的类型
     *
     * @param type
     */
    void setType(Type type);

    /**
     * 获取输出类型
     *
     * @return Type
     */
    Type getType();
}
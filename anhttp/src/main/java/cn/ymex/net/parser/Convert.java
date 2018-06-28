package cn.ymex.net.parser;

/**
 *
 * @author ymexc
 * @date 2018/6/28
 * Convert
 */
public interface Convert<F, T> {
    /**
     * @param value 输出
     * @return 输出
     * @throws Exception
     */
    T convert(F value) throws Exception;
}

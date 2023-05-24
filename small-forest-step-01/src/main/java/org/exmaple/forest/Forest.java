package org.exmaple.forest;

import org.exmaple.forest.config.ForestConfiguration;

/**
 * Forest 快捷接口
 *
 * <p>该类提供 Forest 常用的基本接口方法, 列如:
 * <pre>
 *     // 获取 Forest GET请求
 *     Forest.get("http://localhost:8080")
 *
 *     // 获取 Forest POST请求
 *     Forest.post("http://localhost:8080")
 *
 *     // 创建或获取全局默认配置，即 ForestConfiguration 对象
 *     Forest.config();
 * </pre>
 *
 * @author gongjun [dt_flys@hotmail.com]
 * @since 1.5.2
 */
public abstract class Forest {


    /**
     * 创建 Forest 客户端接口实例
     *
     * @param clazz  请求接口类
     * @param <T>    请求接口类泛型
     * @return       Forest 接口实例
     */
    public static <T> T client(Class<T> clazz) {
        return config().createInstance(clazz);
    }

    /**
     * 获取或创建全局默认配置，即 {@link ForestConfiguration} 对象
     * <p>全局默认配置的配置ID为 forestConfiguration
     *
     * @return {@link ForestConfiguration} 对象
     */
    public static ForestConfiguration config() {
        return ForestConfiguration.configuration();
    }

}

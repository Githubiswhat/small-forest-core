/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Jun Gong
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.exmaple.forest.config;


import org.exmaple.forest.proxy.ProxyFactory;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * global configuration
 * Forest全局配置管理类
 *
 * @author gongjun[dt_flys@hotmail.com]
 * @since 2016-03-24
 */
public class ForestConfiguration implements Serializable {

    private String id;

    private final static Map<String, ForestConfiguration> CONFIGURATION_CACHE = new ConcurrentHashMap<>();

    /**
     * 请求接口的实例缓存，用于缓存请求接口的动态代理的实例
     */
    private Map<Class, Object> instanceCache = new ConcurrentHashMap<>();

    private boolean cacheEnabled = true;

    public static ForestConfiguration configuration() {
        return configuration("forestConfiguration");
    }


    /**
     * 实例化ForestConfiguration对象，并初始化默认值
     *
     * @param id 配置ID
     * @return 新创建的ForestConfiguration实例
     */
    public static ForestConfiguration configuration(String id) {
        ForestConfiguration configuration = ForestConfiguration.CONFIGURATION_CACHE.get(id);
        if (configuration == null) {
            synchronized (ForestConfiguration.class) {
                if (!CONFIGURATION_CACHE.containsKey(id)) {
                    configuration = createConfiguration();
                    configuration.setId(id);
                    CONFIGURATION_CACHE.put(id, configuration);
                }
            }
        }
        return CONFIGURATION_CACHE.get(id);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static ForestConfiguration createConfiguration() {
        ForestConfiguration configuration = new ForestConfiguration();
        configuration.setId("forestConfiguration" + configuration.hashCode());
        return configuration;
    }

    /**
     * 创建请求接口的动态代理实例
     *
     * @param clazz 请求接口类
     * @param <T>   请求接口类泛型
     * @return 动态代理实例
     * @see ForestConfiguration#client(Class)
     */
    public <T> T createInstance(Class<T> clazz) {
        ProxyFactory<T> proxyFactory = getProxyFactory(clazz);
        return proxyFactory.createInstance();
    }

    /**
     * 创建请求接口的动态代理实例
     *
     * @param clazz 请求接口类
     * @param <T>   请求接口类泛型
     * @return 动态代理实例
     */
    public <T> T client(Class<T> clazz) {
        return createInstance(clazz);
    }

    /**
     * 根据请求接口类创建并获取请求接口的动态代理工厂
     *
     * @param clazz 请求接口类
     * @param <T>   请求接口类泛型
     * @return 动态代理工厂
     */
    public <T> ProxyFactory<T> getProxyFactory(Class<T> clazz) {
        return new ProxyFactory<>(this, clazz);
    }

    /**
     * 获取请求接口实例缓存，返回的缓存对象集合用于缓存请求接口的动态代理的实例
     *
     * @return 缓存对象集合
     */
    public Map<Class, Object> getInstanceCache() {
        return instanceCache;
    }


    /**
     * 是否缓存请求接口实例
     *
     * @return 如果允许缓存实例为 {@code true}, 否则为 {@code false}
     */
    public boolean isCacheEnabled() {
        return cacheEnabled;
    }
}

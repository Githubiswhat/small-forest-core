package org.exmaple.forest.interceptor;


import org.exmaple.forest.exceptions.ForestRuntimeException;
import org.exmaple.forest.reflection.ForestMethod;

/**
 * Forest拦截器接口
 * <p>拦截器在请求的初始化、发送请求前、发送成功、发送失败等生命周期中都会被调用
 * <p>总的生命周期回调函数调用顺序如下:
 * <pre>
 * Forest接口方法调用 -&gt;
 *  &#166; onInvokeMethod -&gt;
 *  &#166; beforeExecute -&gt;
 *     &#166; 如果返回 false -&gt; 中断请求，直接返回
 *     &#166; 如果返回 true -&gt;
 *        &#166; 发送请求 -&gt;
 *          &#166; 发送请求失败 -&gt;
 *              &#166; retryWhen -&gt;
 *                 &#166; 返回 true 则触发请求重试
 *                 &#166; 返回 false 则跳转到 [onError]
 *              &#166; onError -&gt; 跳转到 [afterExecute]
 *          &#166; 发送请求成功 -&gt;
 *             &#166; 等待响应 -&gt;
 *             &#166; 接受到响应 -&gt;
 *             &#166; retryWhen -&gt;
 *                 &#166; 返回 true 则触发请求重试
 *                 &#166; 返回 false 判断响应状态 -&gt;
 *                     &#166; 响应失败 -&gt; onError -&gt; 跳转到 [afterExecute]
 *                     &#166; 响应成功 -&gt; onSuccess -&gt; 跳转到 [afterExecute]
 *  &#166; afterExecute -&gt; 退出 Forest 接口方法，并返回数据
 * </pre>
 */
public interface Interceptor<T> {



}

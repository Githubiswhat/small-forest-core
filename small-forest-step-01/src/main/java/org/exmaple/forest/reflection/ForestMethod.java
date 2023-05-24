package org.exmaple.forest.reflection;

import org.exmaple.forest.config.ForestConfiguration;
import org.exmaple.forest.interceptor.Interceptor;
import org.exmaple.forest.proxy.InterfaceProxyHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * 通过代理调用的实际执行的方法对象
 *
 * @author gongjun
 * @since 2016-05-03
 */
public class ForestMethod<T> {

    private final InterfaceProxyHandler interfaceProxyHandler;
    private final ForestConfiguration configuration;
    private final Method method;

    private Class returnClass;

    public ForestMethod(InterfaceProxyHandler interfaceProxyHandler, ForestConfiguration configuration, Method method) {
        this.interfaceProxyHandler = interfaceProxyHandler;
        this.configuration = configuration;
        this.method = method;

        processMethodAnnotations();
    }


    /**
     * 处理方法上的注解列表
     */
    private void processMethodAnnotations() {
        List<Annotation> annotationList = new LinkedList<>();
        fetchAnnotationsFromClasses(annotationList, new Class[]{interfaceProxyHandler.getInterfaceClass()});

        for (Annotation ann : method.getAnnotations()) {
            annotationList.add(ann);
        }

        List<ForestAnnotation> requestAnns = new LinkedList<>();
        List<ForestAnnotation> methodAnns = new LinkedList<>();

    }


    private void fetchAnnotationsFromClasses(List<Annotation> annotationList, Class[] classes) {
        for (Class clazz : classes) {
            if (clazz == null || clazz == Object.class) {
                continue;
            }
            fetchAnnotationsFromClasses(annotationList, clazz.getInterfaces());
            for (Annotation ann : clazz.getAnnotations()) {
                annotationList.add(ann);
            }
        }
    }


    /**
     * 调用 Forest 动态代理接口对象的方法
     *
     * @param proxy 动态代理对象
     * @param method 所要调用的方法 {@link Method}对象
     * @param args 所要调用方法的入参数组
     * @return 方法调用返回结果
     * @throws Throwable 方法调用过程中可能抛出的异常
     */
    public Object invoke(Object[] args) throws Throwable {
        return "hello world";
    }


    private static class ForestAnnotation {
        private final Annotation annotation;
        private final Class<? extends Interceptor> interceptor;

        private ForestAnnotation(Annotation annotation, Class<? extends Interceptor> interceptor) {
            this.annotation = annotation;
            this.interceptor = interceptor;
        }

        public Annotation getAnnotation() {
            return annotation;
        }

        public Class<? extends Interceptor> getInterceptor() {
            return interceptor;
        }
    }




}

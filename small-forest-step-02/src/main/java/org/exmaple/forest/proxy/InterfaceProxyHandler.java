package org.exmaple.forest.proxy;


import org.exmaple.forest.config.ForestConfiguration;
import org.exmaple.forest.exceptions.ForestRuntimeException;
import org.exmaple.forest.reflection.ForestMethod;
import org.exmaple.forest.utils.MethodHandlesUtil;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gongjun[dt_flys@hotmail.com]
 * @since 2016-05-04
 */
public class InterfaceProxyHandler<T> implements InvocationHandler {

    private interface NonParamsInvocation {
        Object invoke(InterfaceProxyHandler<?> handler, Object proxy) throws Throwable;
    }

    private final static Map<String, NonParamsInvocation> NON_PARAMS_INVOCATION_MAP = new HashMap<>();

    private final ForestConfiguration configuration;

    private final ProxyFactory proxyFactory;

    private final Class<T> interfaceClass;

    private final MethodHandles.Lookup defaultMethodLookup;

    private final Map<Method, ForestMethod> forestMethodMap = new HashMap<>();

    public InterfaceProxyHandler(ForestConfiguration configuration, ProxyFactory proxyFactory, Class<T> interfaceClass) {
        this.configuration = configuration;
        this.proxyFactory = proxyFactory;
        this.interfaceClass = interfaceClass;

        try {
            defaultMethodLookup = MethodHandlesUtil.lookup(interfaceClass);
        } catch (Throwable e) {
            throw new ForestRuntimeException(e);
        }

        initMethods();
    }
    private void initMethods() {
        initMethods(interfaceClass);
    }

    private void initMethods(Class<?> clazz) {
        Class<?>[] superClasses = clazz.getInterfaces();
        for (Class<?> superClass : superClasses) {
            initMethods(superClass);
        }

        Method[] methods = clazz.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if (method.isDefault()) {
                continue;
            }
            ForestMethod forestMethod = new ForestMethod(this, configuration, method);
            forestMethodMap.put(method, forestMethod);
        }
    }


    private static NonParamsInvocation getNonParamsInvocation(String methodName) {
        return NON_PARAMS_INVOCATION_MAP.get(methodName);
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
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if (method.isDefault()) {
            return invokeDefaultMethod(proxy, method, args);
        }
        ForestMethod forestMethod = forestMethodMap.get(method);
        if (forestMethod == null) {
            if (args == null || args.length == 0) {
                NonParamsInvocation invocation = getNonParamsInvocation(methodName);
                if (invocation != null) {
                    return invocation.invoke(this, proxy);
                }
            }
            if (args != null && args.length == 1) {
                if ("equals".equals(methodName)) {
                    Object obj = args[0];
                    if (Proxy.isProxyClass(obj.getClass())) {
                        InvocationHandler h1 = Proxy.getInvocationHandler(proxy);
                        InvocationHandler h2 = Proxy.getInvocationHandler(obj);
                        return h1.equals(h2);
                    }
                    return false;
                }
                if ("wait".equals(methodName) && args[0] instanceof Long) {
                    proxy.wait((Long) args[0]);
                }
            }
            if (args != null && args.length == 2 &&
                    args[0] instanceof Long &&
                    args[1] instanceof Integer) {
                if ("wait".equals(methodName)) {
                    proxy.wait((Long) args[0], (Integer) args[1]);
                }
            }
            throw new NoSuchMethodError(method.getName());
        }
        return forestMethod.invoke(args);
    }

    private Object invokeDefaultMethod(Object proxy, Method method, Object[] args)
            throws Throwable {
        return defaultMethodLookup.findSpecial(interfaceClass, method.getName(), MethodType.methodType(method.getReturnType(),
                        method.getParameterTypes()), interfaceClass)
                .bindTo(proxy).invokeWithArguments(args);
    }


    public Class<T> getInterfaceClass() {
        return this.interfaceClass;
    }
}

package org.example;


import org.example.Client.MyClient;
import org.exmaple.forest.Forest;
import org.junit.Test;

public class TestForest {
    @Test
    public void testForest() {
        // 实例化Forest请求接口
        MyClient myClient = Forest.client(MyClient.class);
        // 调用Forest请求接口，并获取响应返回结果
        String result = myClient.helloForest();
        // 打印响应结果
        System.out.println(result);
    }
}

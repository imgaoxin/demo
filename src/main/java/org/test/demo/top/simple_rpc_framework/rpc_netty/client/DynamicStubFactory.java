package org.test.demo.top.simple_rpc_framework.rpc_netty.client;

import java.util.Map;

import com.itranswarp.compiler.JavaStringCompiler;
import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.Transport;

public class DynamicStubFactory implements StubFactory {
    private final static String STUB_SOURCE_TEMPLATE = "package org.test.demo.top.simple_rpc_framework.rpc_netty.client.stubs;\n"
            + "import org.test.demo.top.simple_rpc_framework.rpc_netty.serialize.SerializeSupport;\n"
            + "public class %s extends AbstractStub implements %s {\n" 
            + "    @Override\n"
            + "    public String %s(String arg) {\n" 
            + "        return SerializeSupport.parse(\n"
            + "                invokeRemote(\n" 
            + "                        new RpcRequest(\n"
            + "                                \"%s\",\n" 
            + "                                \"%s\",\n"
            + "                                SerializeSupport.serialize(arg)\n" 
            + "                        )\n"
            + "                )\n" 
            + "        );\n" 
            + "    }\n" 
            + "}";

    @Override
    @SuppressWarnings("unchecked")
    public <T> T createStub(Transport transport, Class<T> serviceClass) {
        try {
            // 填充源码模板
            // todo 解除单方法，String类型参数和返回值的限定
            String stubSimpleName = serviceClass.getSimpleName() + "Stub";
            String classFullName = serviceClass.getName();
            String stubFullName = "org.test.demo.top.simple_rpc_framework.rpc_netty.client.stubs." + stubSimpleName;
            String methodName = serviceClass.getMethods()[0].getName();
            
            String source = String.format(STUB_SOURCE_TEMPLATE, stubSimpleName, classFullName, methodName, classFullName, methodName);

            // 编译源代码
            JavaStringCompiler compiler = new JavaStringCompiler();
            Map<String, byte[]> results = compiler.compile(stubSimpleName + ".java", source);

            // 加载编译好的类
            Class<?> clazz = compiler.loadClass(stubFullName, results);

            // 把Transport赋值给桩
            // ServiceStub stubInstance = (ServiceStub) clazz.newInstance();
            ServiceStub stubInstance = (ServiceStub) clazz.getDeclaredConstructor().newInstance();
            stubInstance.setTransport(transport);

            // 返回桩
            return (T) stubInstance;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
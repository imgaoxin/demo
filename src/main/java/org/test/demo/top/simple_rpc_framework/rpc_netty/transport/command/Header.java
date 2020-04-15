package org.test.demo.top.simple_rpc_framework.rpc_netty.transport.command;

public class Header{
    /**
     * 唯一标识一个请求命令，用于双工异步通信的请求和响应配对
     */
    private int requestId;
    /**
     * 命令的版本号，使传输协议具备持续升级和向下兼容的能力。
     * 接收方收到命令后首先检查命令的版本号，在根据版本号执行对应的逻辑
     */
    private int version;
    /**
     * 命令的类型，用于接收方识别并路由到对应的处理类
     */
    private int type;

    public Header() {}
    public Header(int type, int version, int requestId) {
        this.type = type;
        this.version = version;
        this.requestId = requestId;
    }
    public int getRequestId() {
        return this.requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int length() {
        return Integer.BYTES + Integer.BYTES + Integer.BYTES;
    }
}
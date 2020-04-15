package org.test.demo.top.simple_rpc_framework.rpc_netty.client.stubs;

import java.util.concurrent.ExecutionException;

import org.test.demo.top.simple_rpc_framework.rpc_netty.client.RequestIdSupport;
import org.test.demo.top.simple_rpc_framework.rpc_netty.client.ServiceStub;
import org.test.demo.top.simple_rpc_framework.rpc_netty.client.ServiceTypes;
import org.test.demo.top.simple_rpc_framework.rpc_netty.client.ServiceVersions;
import org.test.demo.top.simple_rpc_framework.rpc_netty.serialize.SerializeSupport;
import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.Transport;
import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.command.Code;
import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.command.Command;
import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.command.Header;
import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.command.ResponseHeader;

public abstract class AbstractStub implements ServiceStub {
    protected Transport transport;
    
    @Override
    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    protected byte[] invokeRemote(RpcRequest request){
        // 构建Command
        Header header = new Header(ServiceTypes.TYPE_RPC_REQUEST, ServiceVersions.VERSION_RPC_REQUEST, RequestIdSupport.next());
        byte[] payload = SerializeSupport.serialize(request);
        Command requestCommand = new Command(header, payload);

        try {
            Command responseCommand = transport.send(requestCommand).get();
            ResponseHeader responseHeader = (ResponseHeader) responseCommand.getHeader();
            if (responseHeader.getCode() == Code.SUCCESS.getCode()) {
                return responseCommand.getPayload();
            } else {
                throw new Exception(responseHeader.getError());
            }
            
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
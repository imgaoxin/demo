package org.test.demo.top.simple_rpc_framework.rpc_netty.serialize;

public class SerializeException extends RuntimeException{

    private static final long serialVersionUID = -1647719875595293474L;

    public SerializeException(String msg){
        super(msg);
    }

    public SerializeException(Throwable t){
        super(t);
    }
}

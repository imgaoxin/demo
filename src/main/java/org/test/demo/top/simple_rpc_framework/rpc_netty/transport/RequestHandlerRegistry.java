package org.test.demo.top.simple_rpc_framework.rpc_netty.transport;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.test.demo.top.simple_rpc_framework.rpc_api.spi.ServiceSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 命令注册机制,方便扩展命令处理器,不用修改路由分发的方法.
 */
public class RequestHandlerRegistry {
	private static final Logger logger = LoggerFactory.getLogger(RequestHandlerRegistry.class);
	private Map<Integer, RequestHandler> handlerMap = new HashMap<>();
	private static RequestHandlerRegistry instance = null;

	private RequestHandlerRegistry() {
		Collection<RequestHandler> requestHandlers = ServiceSupport.loadAll(RequestHandler.class);
		for (RequestHandler requestHandler : requestHandlers) {
			handlerMap.put(requestHandler.type(), requestHandler);
			logger.info("Load request handler, type: {}, class: {}.", requestHandler.type(),
					requestHandler.getClass().getCanonicalName());
		}
	}

	public static RequestHandlerRegistry getInstance() {
		if (null == instance) {
			synchronized (RequestHandlerRegistry.class) {
				if (null == instance) {
					instance = new RequestHandlerRegistry();
				}
			}
		}
		return instance;
	}

	public RequestHandler get(int type) {
		return handlerMap.get(type);
	}
}
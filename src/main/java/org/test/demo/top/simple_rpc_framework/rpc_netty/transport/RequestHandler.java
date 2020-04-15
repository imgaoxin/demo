package org.test.demo.top.simple_rpc_framework.rpc_netty.transport;

import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.command.Command;

/**
 * 请求处理器
 */
public interface RequestHandler {

	/**
	 * 处理请求
	 * @param request 请求命令
	 * @return 响应命令
	 */
	Command handle(Command request);

	/**
	 * 支持的请求类型
	 */
	int type();
}
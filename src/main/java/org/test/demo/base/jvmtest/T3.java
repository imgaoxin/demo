package org.test.demo.base.jvmtest;

/**
 * 通过Runtime类查看内存情况
 * 
 * @author Gao Xin
 *
 */
public class T3 {

	public static void main(String[] args) {
		// printMemeryInfo();
		// byte[] b = new byte[1024 * 1024];
		// printMemeryInfo();
	}

	static void printMemeryInfo() {
		System.out.println("total:" + Runtime.getRuntime().totalMemory());
		System.out.println("free:" + Runtime.getRuntime().freeMemory());
	}
}

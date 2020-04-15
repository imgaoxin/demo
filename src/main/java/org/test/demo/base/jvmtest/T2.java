package org.test.demo.base.jvmtest;

/**
 * jvm 调参 -XX:-DoEscapeAnalysis -XX:-EliminateAllocations -XX:-UseTLAB -XX:+PrintGCDetails
 * 关闭逃逸分析,标量替换(关闭栈上分配);关闭线程本地分配;打印GC详细信息
 * @author Gao Xin
 *
 */
public class T2 {

	public static void main(String[] args) {
		// byte[] b = new byte[1024];
	}
}

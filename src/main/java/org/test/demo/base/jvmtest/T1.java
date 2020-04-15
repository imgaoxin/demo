package org.test.demo.base.jvmtest;

/**
 * jvm 调参 -XX:-DoEscapeAnalysis -XX:-EliminateAllocations -XX:-UseTLAB -XX:+PrintGC
 * 关闭逃逸分析,标量替换(关闭栈上分配);关闭线程本地分配;打印GC信息
 * @author Gao Xin
 *
 */

public class T1 {

	class User {
		int id;
		String name;
		public User(int i, String name){
			this.id = i;
			this.name = name;
		}
	}
	
	public void createUser(int i, String name){
		new User(i, name);
	}
	
	public static void main(String[] args) {
		T1 t = new T1();
		long start = System.currentTimeMillis();
		for (int i = 0; i < 10000000; i++) {
			t.createUser(i, "name" + i);
		}
		long end = System.currentTimeMillis() - start;
		System.out.println(end);
	}
}
package org.test.demo.base.jvmtest;

/**
 * 线程栈大小 -Xss  128k 512k
 * @author Gao Xin
 *
 */
public class T5 {

	static int count = 0;
	static void add(){
		count++;
		add();
	}
	
	public static void main(String[] args) {
		try{
			add();
		}catch(Throwable e){
			e.printStackTrace();//StackOverflowError
			System.out.println(count);
		}
	}
}

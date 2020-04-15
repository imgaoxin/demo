package org.test.demo.base.jvmtest;

import java.util.ArrayList;
import java.util.List;

/**
 * 内存溢出 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=d:\jvm.dump
 * -XX:+PrintGCDetails -Xms10M -Xmx10M 内存文件的查看工具VisualVM
 * 
 * @author Gao Xin
 *
 */
public class T4 {

	public static void main(String[] args) {
		List<Object> list = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			list.add(new byte[1024 * 1024]);
		}
	}
}

package org.test.demo.base.functional;

public class FuncTest{
    public static void main(String[] args) {
        showTest(() -> System.out.println("hello! ..."));
    }

    public static void showTest(MyFuncInf myf){
        myf.hello();
    }
    
}
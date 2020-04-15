package org.test.demo.base.seriallze;

import org.json.JSONObject;

import java.io.FileNotFoundException;

/**
 * @author gx
 * @create 2019-08-19 17:42
 */
public class SerializeTest {
    public static void main(String[] args) throws FileNotFoundException {
        //User user1 = new User("Zhangsan", 18, true);
        //User user2 = new User();
        //user2.setName("Lili");
        //user2.setAge(25);
        //user2.setMale(false);
        //
        //Kryo kryo = new Kryo();
        //kryo.register(User.class);
        //Output output1 = new Output(new FileOutputStream("D://user1.bin"));
        //Output output2 = new Output(new FileOutputStream("D://user2.bin"));
        //kryo.writeObject(output1, user1);
        //kryo.writeObject(output2, user2);
        //output1.close();
        //output2.close();
        //
        //Input input = new Input(new FileInputStream("D://user2.bin"));
        //User user = kryo.readObject(input, User.class);
        //input.close();
        //System.out.println(user.getName());
        //System.out.println(user.getAge());
        //System.out.println(user.getMale());

        User user = new User("Zhanglili", 12, true);
        JSONObject jsonObject = new JSONObject(user, new String[]{"name", "age", "gender"});
        System.out.println(jsonObject);
    }
}


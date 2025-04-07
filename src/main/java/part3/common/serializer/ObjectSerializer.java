package part3.common.serializer;

import java.io.*;

public class ObjectSerializer implements Serializer{
    @Override
    public byte[] serialize(Object obj) {

        byte[] bytes = null;
        //创建一个内存中的输出流，存储序列化的数据，
        ByteArrayOutputStream bos = new ByteArrayOutputStream();//一个可变大小的字节数据缓冲区
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);//将对象转换为二进制数据，并写入字节缓冲区
            oos.writeObject(obj);//将对象写入输出流中，触发序列化
            oos.flush();//将数据刷新到底层流中
            bytes = bos.toByteArray(); //将字节缓冲区的内容转换为字节数组
            oos.close();
            bos.close();
        } catch (IOException e) {
           e.printStackTrace();
        }
        return bytes;
    }

    @Override
    public Object deserializer(byte[] bytes, int messageType) {
        Object obj = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);//将字节数组包装成输入流
        try {
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject(); //从ois中读取序列化的对象，并反序列化
            ois.close();
            bis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public int getType() {
        return 0;
    }
}

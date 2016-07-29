package com.gc.weather.util;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtil {

    public static String FILENAME = "db";

    /**
     * 对象序列化保存至本地
     * @param context 上下文
     * @param object 保存的对象
     * @param name 文件名
     * @throws IOException
     */
    public static void saveObject(Context context, Object object, String name) throws IOException {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = context.openFileOutput(name + "." + FILENAME, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
        } finally {
            if (fos != null) {
                fos.close();
            }
            if (oos != null) {
                oos.close();
            }
        }
    }

    /**
     * 反序列化成对象
     * @param context 上下文
     * @param name 文件名
     * @return 序列化对象
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object getObject(Context context, String name) throws IOException, ClassNotFoundException {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(name + "." + FILENAME);
            ois = new ObjectInputStream(fis);
            return ois.readObject();
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (ois != null) {
                ois.close();
            }
        }
    }
}

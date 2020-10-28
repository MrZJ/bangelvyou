package com.shenmai.system.utlis;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * Created by Administrator on 2016/11/25 0025.
 */
public class CacheObjUtils {

    public static void saveObj(Context context,

                               String key, Object value) throws IOException {
        SharedPreferences sp = context.getSharedPreferences("ConfigPerference",
                Context.MODE_WORLD_WRITEABLE);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(

                byteArrayOutputStream);

        objectOutputStream.writeObject(value);

        String objString = new String(Base64.encode(

                byteArrayOutputStream.toByteArray(), Base64.DEFAULT));

        sp.edit().putString(key, objString).commit();

        objectOutputStream.close();

    }

    public static Object getObj(Context context,
                                String key) throws StreamCorruptedException, IOException,
            ClassNotFoundException {
        SharedPreferences sp = context.getSharedPreferences("ConfigPerference",
                Context.MODE_PRIVATE);
        String str = sp.getString(key, "");
        if (str.length() <= 0)
            return null;
        Object obj = null;
        byte[] mobileBytes = Base64.decode(str.toString().getBytes(),
                Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                mobileBytes);
        ObjectInputStream objectInputStream;
        objectInputStream = new ObjectInputStream(byteArrayInputStream);
        obj = objectInputStream.readObject();
        objectInputStream.close();
        mobileBytes = null;
        return obj;
    }
}

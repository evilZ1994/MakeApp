package com.example.lollipop.makeupapp.util;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Lollipop on 2017/8/12.
 */

public class AESUtil {

    public static String aes1Encode(String data, String pwd){
        String result = null;
        byte[] encodeResult = null;
        try {
            //创建加密引擎
            Cipher cipher = Cipher.getInstance("AES");
            //根据参数生成key
            SecretKey key = new SecretKeySpec(pwd.getBytes(), "AES");
            //对Cipher初始化，设置为加密模式
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //加密数据
            encodeResult = cipher.doFinal(data.getBytes());
            result = Base64.encodeToString(encodeResult, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }  catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String aes1Decode(String encodedData, String pwd){
        String result = null;
        byte[] decodeResult = null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec key = new SecretKeySpec(pwd.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            decodeResult = cipher.doFinal(Base64.decode(encodedData, Base64.DEFAULT));
            result = new String(decodeResult);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return result;
    }
}

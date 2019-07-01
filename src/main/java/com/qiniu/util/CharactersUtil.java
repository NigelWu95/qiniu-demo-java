package com.qiniu.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

public class CharactersUtil {

    public static void main(String[] args) {

        System.out.println(QiniuEtagUtil.urlSafeBase64Decode("Fto5o-5ea0sNMlW_75VgGJCv2AcJ"));
        System.out.println(new String(UrlSafeBase64.decode("Fto5o-5ea0sNMlW_75VgGJCv2AcJ")));
        System.out.println(QiniuEtagUtil.urlSafeBase64Encode("test:test.jpg"));
        System.out.println(QiniuEtagUtil.urlSafeBase64Decode("dGVzdDp0ZXN0LmpwZw=="));
        System.out.println(UrlSafeBase64.encodeToString("test:test.jpg"));
        System.out.println(new String(UrlSafeBase64.decode("dGVzdDp0ZXN0LmpwZw==")));
        System.out.println(CharactersUtil.bytesToHexString("lln5g3WQCBqSp3WazTf0shKDyRAP".getBytes()));
        System.out.println(Arrays.toString(CharactersUtil.parseStringToHexArray("lln5g3WQCBqSp3WazTf0shKDyRAP")));

    }

    public static String[] parseStringToHexArray(String originString) {

        byte[] byteArray = originString.getBytes();
        return parseBytesToHexArray(byteArray);
    }

    public static String[] parseBytesToHexArray(byte[] byteArray) {

        String hex;
        StringBuilder hexString = new StringBuilder();

        for (int i = 0; i < byteArray.length; i++) {
            if (i > 0)
                hexString.append(",");

            hex = Integer.toHexString(0xFF & byteArray[i]);
            if ((byteArray[i] & 0xFF) < 0x10) // 0~F前面加上零
                hexString.append("0x0").append(hex);
            else
                hexString.append("0x").append(hex);
        }

        return hexString.toString().split(",");
    }

    public static String bytesToHexString(byte[] byteArray) {
        String result = "";
        for (int i = 0; i < byteArray.length; i++) {
            String temp = Integer.toHexString(byteArray[i] & 0xff);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            result += temp;
        }

        return result;
    }

    /*
       将 16 进制字符串转换为字节数组
     */
    public static byte[] decode(char[] data) throws IllegalArgumentException {
        int len = data.length;
        // data 的长度不能为奇数
        if ((len & 0x01) != 0) {
            throw new IllegalArgumentException("Odd number of characters.");
        }
        byte[] out = new byte[len >> 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int digit = toDigit(data[j], j) << 4;
            j++;
            digit = digit | toDigit(data[j], j);
            j++;
            out[i] = (byte) (digit & 0xFF);
        }
        return out;
    }

    protected static int toDigit(char ch, int index) throws IllegalArgumentException {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new IllegalArgumentException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }

    public static byte[] getBytes(char[] chars) {
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer byteBuffer = cs.encode(cb);
        return byteBuffer.array();
    }
}
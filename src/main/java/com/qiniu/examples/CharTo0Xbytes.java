package com.qiniu.examples;

public class CharTo0Xbytes {

    public static void main(String[] args) {

        byte[] byteArray ="VkVyMZBTvY6f3Ayp".getBytes();
        System.out.println(byteArray[0]);

        StringBuilder hexString = new StringBuilder();

        for (int i = 0; i < byteArray.length; i++) {
            if ((byteArray[i] & 0xff) < 0x10)//0~F前面不零
                hexString.append("0");
            hexString.append("0x" + Integer.toHexString(0xFF & byteArray[i]) + ",");
        }

        System.out.println(hexString.toString().toLowerCase());
    }

}
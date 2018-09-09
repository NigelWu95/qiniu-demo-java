package com.qiniu.examples.util;

public class CharTo0XBytes {

    public static void main(String[] args) {

        String hexStrings = parseStringToHex("VkVyMZBTvY6f3Ayp");
        System.out.println(hexStrings);
    }

    public static String parseStringToHex(String originString) {

        byte[] byteArray = originString.getBytes();
        String hex = "";
        StringBuilder hexString = new StringBuilder();

        for (int i = 0; i < byteArray.length; i++) {
            if (i > 0)
                hexString.append(",");

            hex = Integer.toHexString(0xFF & byteArray[i]);
            if ((byteArray[i] & 0xFF) < 0x10) // 0~F前面加上零
                hexString.append("0x0" + hex);
            else
                hexString.append("0x" + hex);
        }

        return hexString.toString();
    }
}
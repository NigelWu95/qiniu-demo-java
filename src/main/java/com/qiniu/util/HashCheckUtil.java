package com.qiniu.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashCheckUtil {

    public static void main(String[] args) throws Exception {

        System.out.println(getFileHash("/Users/wubingheng/Downloads/parsed-3.jpg", "sha1"));
        System.out.println(getFileHash("/Users/wubingheng/Downloads/paper-hor-1.jpg", "sha1")
                .equals("01105c5436820997b0588ef323c83ca326d79805"));
        System.out.println(getFileHash("/Users/wubingheng/Downloads/parsed-3.jpg", "sha1")
                .equals("3edc2d8fd7d1c88e109c6fe64fb70a72ea1ab80c"));
    }

    public static String getFileHash(String filePath, String algorithm) throws NoSuchAlgorithmException, IOException {
        File file = new File(filePath);
        return getFileHash(file, algorithm);
    }

    public static String getFileHash(File file, String algorithm) throws NoSuchAlgorithmException, IOException {
        try (FileInputStream in = new FileInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] buffer = new byte[1024 * 1024 * 10];
            int len;
            while ((len = in.read(buffer)) > 0) {
                digest.update(buffer, 0, len);
            }
            String sha1 = new BigInteger(1, digest.digest()).toString(16);
            int length = 40 - sha1.length();
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    sha1 = "0" + sha1;
                }
            }
            return sha1;
        }
    }
}
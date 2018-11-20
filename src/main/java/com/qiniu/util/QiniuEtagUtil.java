package com.qiniu.util;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import javax.xml.bind.DatatypeConverter;

public class QiniuEtagUtil {

    public static void main(String[] args) {

        try {
            String srcEtag =
//                    calcETag("");
                    "FrXIWGT7eB94QR46knD4TbH5TGdc"; // hex string b5c85864fb781f78411e3a9270f84db1f94c675c
            String tarEtag = "AN751gvBIGo6x37EJDcZ8dVRLb_7";
            String fopEtag = calcFopEtag(srcEtag, "imageMogr2/rotate/90/crop/!2105x1440a227a0/thumbnail/1866x1276");

            System.out.println("source etag: " + srcEtag);
            System.out.println("src etag hex string is: " + CharactersUtil.bytesToHexString(checkHashFromEtag(srcEtag)));
            System.out.println("move preamble from src etag: " + CharactersUtil.bytesToHexString(movePreambleFromEtag(srcEtag)));
            System.out.println("target etag: " + tarEtag);
            System.out.println("fop etag is " + fopEtag + ", and it is " + (tarEtag.equals(fopEtag) ? "" : "not ") +
                    "equal with tarEtag");
            System.out.println("fop etag hex string is: " + CharactersUtil.bytesToHexString(checkHashFromEtag(fopEtag)));
            System.out.println("add preamble to origin hash: " + CharactersUtil.bytesToHexString(addPreambleToHash(
                    CharactersUtil.decode("def9d60bc1206a3ac77ec4243719f1d5512dbffb".toCharArray()), (byte)0x16)));

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static final int CHUNK_SIZE = 1 << 22;

    public static byte[] sha1(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("sha1");
        return mDigest.digest(data);
    }

    public static String urlSafeBase64Encode(byte[] data) {
        String encodedString = DatatypeConverter.printBase64Binary(data);
        encodedString = encodedString.replace('+', '-').replace('/', '_');
        return encodedString;
    }

    public static String urlSafeBase64Encode(String data) {
        return urlSafeBase64Encode(data.getBytes());
    }

    public static String urlSafeBase64Decode(String encodedString) {
        byte[] bytes = DatatypeConverter.parseBase64Binary(encodedString
                .replace('-', '+').replace('_', '/'));
        return new String(bytes);
    }

    public static byte[] lessThan4mHash(FileInputStream inputStream) throws IOException, NoSuchAlgorithmException {
        byte[] bytesData = new byte[CHUNK_SIZE];
        int bytesReadLen = inputStream.read(bytesData, 0, CHUNK_SIZE);
        byte[] bytesRead = new byte[bytesReadLen];
        System.arraycopy(bytesData, 0, bytesRead, 0, bytesReadLen);
        return sha1(bytesRead);
    }

    public static byte[] greaterThan4mHash(FileInputStream inputStream, long fileLength) throws IOException,
            NoSuchAlgorithmException {
        int chunkCount = (int) (fileLength / CHUNK_SIZE);
        if (fileLength % CHUNK_SIZE != 0) {
            chunkCount += 1;
        }
        byte[] allSha1Data = new byte[0];
        for (int i = 0; i < chunkCount; i++) {
            byte[] chunkDataSha1 = lessThan4mHash(inputStream);
            allSha1Data = addBytesToHashArray(allSha1Data, chunkDataSha1);
        }
        return sha1(allSha1Data);
    }

    public static String calcETag(String fileName) throws IOException, NoSuchAlgorithmException {
        String etag;
        File file = new File(fileName);
        if (!(file.exists() && file.isFile() && file.canRead())) {
            throw new IOException("Error: File not found or not readable");
        }
        long fileLength = file.length();
        FileInputStream inputStream = new FileInputStream(file);
        if (fileLength <= CHUNK_SIZE) {
            byte[] hashDataSha1 = lessThan4mHash(inputStream);
            etag = urlSafeBase64Encode(addPreambleToHash(hashDataSha1, (byte)0x16));
        } else {
            byte[] allSha1DataSha1 = greaterThan4mHash(inputStream, fileLength);
            etag = urlSafeBase64Encode(addPreambleToHash(allSha1DataSha1, (byte)0x96));
        }
        inputStream.close();
        return etag;
    }

    public static byte[] checkHashFromEtag(String etag) {

        String encodedString = etag.replace('-', '+').replace('_', '/');
        byte[] encodedStringHash = DatatypeConverter.parseBase64Binary(encodedString);
        if (etag.equals(urlSafeBase64Encode(encodedStringHash))) {
            return encodedStringHash;
        } else {
            return DatatypeConverter.parseBase64Binary(etag);
        }
    }

    public static byte[] addBytesToHashArray(byte[] allSha1Data, byte[] bytesSha1Data) {
        byte[] newAllSha1Data = new byte[bytesSha1Data.length + allSha1Data.length];
        System.arraycopy(allSha1Data, 0, newAllSha1Data, 0, allSha1Data.length);
        System.arraycopy(bytesSha1Data, 0, newAllSha1Data, allSha1Data.length, bytesSha1Data.length);
        return newAllSha1Data;
    }

    public static byte[] addPreambleToHash(byte[] hash, byte preamble) {
        byte[] hashData = new byte[hash.length + 1];
        System.arraycopy(hash, 0, hashData, 1, hash.length);
        hashData[0] = preamble;
        return hashData;
    }

    public static byte[] movePreambleFromEtag(String etag) {
        String encodedString = etag.replace('-', '+').replace('_', '/');
        byte[] srcEtagHashBytes = Base64.getDecoder().decode(encodedString);
        byte[] hashBytes = new byte[srcEtagHashBytes.length - 1];
        System.arraycopy(srcEtagHashBytes, 1, hashBytes, 0, srcEtagHashBytes.length - 1);
        return hashBytes;
    }

    public static String calcFopEtag(String srcEtag, String processCommand) throws NoSuchAlgorithmException {

        byte[] srcEtagHashBytes = Base64.getDecoder().decode(srcEtag);
        byte[] fopBytes = new byte[]{0x01, 0x01};
        byte[] fopParamsBytes = ( "cmd=" + processCommand + "&sp=").getBytes();
        byte[] hashData = new byte[srcEtagHashBytes.length + fopBytes.length + fopParamsBytes.length];
        System.arraycopy(srcEtagHashBytes, 0, hashData, 0, srcEtagHashBytes.length);
        System.arraycopy(fopBytes, 0, hashData, srcEtagHashBytes.length, fopBytes.length);
        System.arraycopy(fopParamsBytes, 0, hashData, srcEtagHashBytes.length + fopBytes.length,
                fopParamsBytes.length);
        byte[] allSha1DataSha1 = sha1(hashData);
        byte[] finalHashData = new byte[allSha1DataSha1.length + 1];
        System.arraycopy(allSha1DataSha1, 0, finalHashData, 1, allSha1DataSha1.length);
        finalHashData[0] = (byte) 0x00;
        String etag = urlSafeBase64Encode(finalHashData);

        return etag;
    }
}

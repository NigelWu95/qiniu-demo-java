package com.qiniu.examples.oss;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;

public class QiniuEtag {

    private final int CHUNK_SIZE = 1 << 22;

    public byte[] sha1(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("sha1");
        return mDigest.digest(data);
    }

    public String urlSafeBase64Encode(byte[] data) {
        String encodedString = DatatypeConverter.printBase64Binary(data);
        encodedString = encodedString.replace('+', '-').replace('/', '_');
        return encodedString;
    }

    public String calcETag(String fileName) throws IOException,
            NoSuchAlgorithmException {
        String etag = "";
        File file = new File(fileName);
        if (!(file.exists() && file.isFile() && file.canRead())) {
            System.err.println("Error: File not found or not readable");
            return etag;
        }
        long fileLength = file.length();
        FileInputStream inputStream = new FileInputStream(file);
        if (fileLength <= CHUNK_SIZE) {
            byte[] fileData = new byte[(int) fileLength];
            inputStream.read(fileData, 0, (int) fileLength);
            byte[] sha1Data = sha1(fileData);
            int sha1DataLen = sha1Data.length;
            byte[] hashData = new byte[sha1DataLen + 1];
            System.arraycopy(sha1Data, 0, hashData, 1, sha1DataLen);
            hashData[0] = 0x16;
            etag = urlSafeBase64Encode(hashData);
        } else {
            int chunkCount = (int) (fileLength / CHUNK_SIZE);
            if (fileLength % CHUNK_SIZE != 0) {
                chunkCount += 1;
            }
            byte[] allSha1Data = new byte[0];
            for (int i = 0; i < chunkCount; i++) {
                byte[] chunkData = new byte[CHUNK_SIZE];
                int bytesReadLen = inputStream.read(chunkData, 0, CHUNK_SIZE);
                byte[] bytesRead = new byte[bytesReadLen];
                System.arraycopy(chunkData, 0, bytesRead, 0, bytesReadLen);
                byte[] chunkDataSha1 = sha1(bytesRead);
                byte[] newAllSha1Data = new byte[chunkDataSha1.length + allSha1Data.length];
                System.arraycopy(allSha1Data, 0, newAllSha1Data, 0, allSha1Data.length);
                System.arraycopy(chunkDataSha1, 0, newAllSha1Data, allSha1Data.length, chunkDataSha1.length);
                allSha1Data = newAllSha1Data;
            }
            byte[] allSha1DataSha1 = sha1(allSha1Data);
            byte[] hashData = new byte[allSha1DataSha1.length + 1];
            System.arraycopy(allSha1DataSha1, 0, hashData, 1, allSha1DataSha1.length);
            hashData[0] = (byte) 0x96;
            etag = urlSafeBase64Encode(hashData);
        }
        inputStream.close();
        return etag;
    }

    public String bytesToHexString(byte[] hash) {
        String result = "";
        for (int i = 0; i < hash.length; i++) {
            String temp = Integer.toHexString(hash[i] & 0xff);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            result += temp;
        }

        return result;
    }

    public byte[] checkOriginHashFromQiniuEtag(String etag) {

        String encodedString = etag.replace('-', '+').replace('_', '/');
        byte[] encodedStringHash = DatatypeConverter.parseBase64Binary(encodedString);
        byte[] originHash = new byte[encodedStringHash.length - 1];
        if (etag.equals(urlSafeBase64Encode(encodedStringHash))) {
            System.arraycopy(encodedStringHash, 1, originHash, 0, encodedStringHash.length - 1);
        } else {
            byte[] etagHash = DatatypeConverter.parseBase64Binary(etag);
            System.arraycopy(etagHash, 1, originHash, 0, etagHash.length - 1);
        }
        return originHash;
    }

    public byte[] checkHashFromQiniuEtag(String etag) {

        String encodedString = etag.replace('-', '+').replace('_', '/');
        byte[] encodedStringHash = DatatypeConverter.parseBase64Binary(encodedString);
        if (etag.equals(urlSafeBase64Encode(encodedStringHash))) {
            return encodedStringHash;
        } else {
            return DatatypeConverter.parseBase64Binary(etag);
        }
    }

    public String calcEtagFromHash(byte[] hash, byte preamble) {
        byte[] hashData = new byte[hash.length + 1];
        System.arraycopy(hash, 0, hashData, 1, hash.length);
        hashData[0] = preamble;
        return urlSafeBase64Encode(hashData);
    }

    public byte[] calcPreambleHash(byte[] hash, byte preamble) {
        byte[] hashData = new byte[hash.length + 1];
        System.arraycopy(hash, 0, hashData, 1, hash.length);
        hashData[0] = preamble;
        return hashData;
    }

    public String calcEtagByProcessQuery(String fileEtag, String processCommand) {
        byte[] hashData = (fileEtag + processCommand).getBytes();
        String etag = "";
        try {
            byte[] allSha1DataSha1 = sha1(hashData);
            byte[] finalHashData = new byte[allSha1DataSha1.length + 1];
            System.arraycopy(allSha1DataSha1, 0, finalHashData, 1, allSha1DataSha1.length);
            finalHashData[0] = (byte) 0x00;
            etag = urlSafeBase64Encode(finalHashData);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return etag;
    }

    public static void main(String[] args) {

        QiniuEtag qiniuEtag = new QiniuEtag();

        try {
            String etag1 = qiniuEtag.calcETag("/Users/wubingheng/Downloads/6351fa257697.jpg");
            String etag2 = qiniuEtag.calcETag("/Users/wubingheng/Downloads/6351fa257697-1.jpg");
            System.out.println("6351fa257697.jpg:    " + etag1);
            System.out.println("6351fa257697-1.jpg:  " + etag2);
            System.out.println("6351fa257697 header: AN751gvBIGo6x37EJDcZ8dVRLb_7");

            byte[] etagHash1 = qiniuEtag.checkHashFromQiniuEtag("AN751gvBIGo6x37EJDcZ8dVRLb_7");
            System.out.println("hash: " + etagHash1.length);
            System.out.println("result: " + qiniuEtag.bytesToHexString(etagHash1));
            System.out.println("base64: " + qiniuEtag.urlSafeBase64Encode(etagHash1));
//            byte[] etagHash2 = qiniuEtag.checkHashFromQiniuEtag("AFmn1RgEN7k-3McZrnED_vmmxYJ9");
//            System.out.println("hash: " + etagHash2.length);
//            System.out.println("result: " + qiniuEtag.bytesToHexString(etagHash2));
//            System.out.println("base64: " + qiniuEtag.urlSafeBase64Encode(etagHash2));
//            byte[] etagHash3 = qiniuEtag.checkHashFromQiniuEtag("ANqUm0wmrSXoKBXiTta-Vy5JevNN");
//            System.out.println("hash: " + etagHash3.length);
//            System.out.println("result: " + qiniuEtag.bytesToHexString(etagHash3));
//            System.out.println("base64: " + qiniuEtag.urlSafeBase64Encode(etagHash3));

//            System.out.println("etagCommand1:        " + qiniuEtag.calcEtagByProcessQuery(etag1, "?imageMogr2/rotate/90/crop/!2105x1440a227a0/thumbnail/1866x1276"));

//            byte[] etagOriginHash = qiniuEtag.checkOriginHashFromQiniuEtag("AN751gvBIGo6x37EJDcZ8dVRLb_7");
//            System.out.println("hash: " + etagOriginHash.length);
//            System.out.println("result: " + qiniuEtag.bytesToHexString(etagOriginHash));
//            System.out.println("base64: " + qiniuEtag.calcEtagFromHash(etagOriginHash, (byte) 0x16));

//            byte[] etagHash1 = qiniuEtag.checkOriginHashFromQiniuEtag(etag1);
//            byte[] etagHash2 = qiniuEtag.checkOriginHashFromQiniuEtag(etag2);
//            System.out.println("hash1: " + etagHash1.length + "; hash2: " + etagHash2.length);
//            System.out.println("result1: " + qiniuEtag.bytesToHexString(etagHash1));
//            System.out.println("result2: " + qiniuEtag.bytesToHexString(etagHash2));
//            System.out.println("base641: " + qiniuEtag.calcEtagFromHash(etagHash1, (byte) 0x16));
//            System.out.println("base642: " + qiniuEtag.calcEtagFromHash(etagHash2, (byte) 0x16));
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("Unsupported algorithm:" + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("IO Error:" + ex.getMessage());
        }

        if (args.length != 1) {
            System.out.println("Usage: qetag <filename>");
        } else {
            String fileName = args[0];
            QiniuEtag etag = new QiniuEtag();
            try {
                System.out.println(etag.calcETag(fileName));
            } catch (NoSuchAlgorithmException ex) {
                System.err.println("Unsupported algorithm:" + ex.getMessage());
            } catch (IOException ex) {
                System.err.println("IO Error:" + ex.getMessage());
            }
        }
    }
}

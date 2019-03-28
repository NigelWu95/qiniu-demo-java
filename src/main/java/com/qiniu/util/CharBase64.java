package com.qiniu.util;

public class CharBase64 {

    public static void main(String[] args) {

        System.out.println(new String(UrlSafeBase64.decode("aGhoaAphYWFhYQ==")));
        System.out.println(UrlSafeBase64.encodeToString("hhhh"));
        System.out.println(UrlSafeBase64.encodeToString("aaaaa"));
        System.out.println(UrlSafeBase64.encodeToString("hhhh\naaaaa"));
        System.out.println(UrlSafeBase64.encodeToString("黑体"));
//        System.out.println(new String(UrlSafeBase64.decode("eyJjYWxsYmFja0JvZHkiOiJidWNrZXQ9JChidWNrZXQpJmtleT0kKGtleSkmbWltZT0kKG1pbWVUeXBlKSZjbGllbnRJZD10ZXN0JnBvbGljeT0yIiwiY2FsbGJhY2tVcmwiOiJodHRwOi8vcW5mcy5mYW5nZGQubmV0OjgwMDIvY2FsbGJhY2svcWluaXUtdXBsb2FkIiwiZGVhZGxpbmUiOjE1NTM1MDQzMjMsImVuZFVzZXIiOiIxIiwiaW5zZXJ0T25seSI6MSwic2F2ZUtleSI6InRlc3Qvb2NlYW4vJChldGFnKSQoZXh0KSIsInNjb3BlIjoidGVzdCJ9==")));
        System.out.println(UrlSafeBase64.encodeToString("{\"callbackBody\":\"bucket=$(bucket)&key=$(key)&mime=$(mimeType)&clientId=test&policy=2\",\"callbackUrl\":\"http://qnfs.fangdd.net:8002/callback/qiniu-upload\",\"deadline\":1553504323,\"endUser\":\"1\",\"insertOnly\":1,\"saveKey\":\"test/ocean/$(etag)$(ext)\",\"scope\":\"test\"}"));
    }
}

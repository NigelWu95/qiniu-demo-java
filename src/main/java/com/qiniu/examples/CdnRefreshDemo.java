package com.qiniu.examples;

import com.qiniu.cdn.CdnManager;
import com.qiniu.cdn.CdnResult.*;
import com.qiniu.common.Config;
import com.qiniu.common.QiniuException;
import com.qiniu.util.Auth;

/**
 * ClassName: CdnRefreshDirsDemo
 * Description: 批量刷新 CDN 目录或 URL
 * Document: https://developer.qiniu.com/kodo/sdk/java#fusion-refresh-urls
 */
public class CdnRefreshDemo {

   public static void main(String args[]) {

       Config config = Config.getInstance();
       String accesskey = config.getAccesskey();
       String secretKey = config.getSecretKey();
       Auth auth = Auth.create(accesskey, secretKey);
       CdnManager c = new CdnManager(auth);

       // 待刷新的目录列表，目录必须以 / 结尾
       String[] dirs = new String[] {
               //....
               "http://cdn_domain/dir1/",
               "http://cdn_domain/dir2/"
       };

       //待刷新的链接列表
       String[] urls = new String[]{
               //....
               "http://cdn_domain/dir1/",
               "http://cdn_domain/dir2/"
       };

       try {
           RefreshResult resultDirs = c.refreshDirs(dirs);
           System.out.println(resultDirs.code);
           System.out.println(resultDirs.requestId);
           System.out.println(resultDirs.invalidDirs);
           //剩余额度
           System.out.println(resultDirs.dirSurplusDay);

           //单次方法调用刷新的链接不可以超过100个
           RefreshResult resultUrls = c.refreshUrls(urls);
           System.out.println(resultUrls.code);
           System.out.println(resultUrls.requestId);
           System.out.println(resultUrls.invalidDirs);
           //剩余额度
           System.out.println(resultUrls.dirSurplusDay);
       } catch (QiniuException e) {
           System.err.println(e.response.toString());
       }
   }

}

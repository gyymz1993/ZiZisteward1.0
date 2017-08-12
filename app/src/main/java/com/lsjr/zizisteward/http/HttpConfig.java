package com.lsjr.zizisteward.http;

public class HttpConfig {
    //public static final String Http = "http://zizi.imzi.com:10804";
   /*6.0开发服务器*/
    //public static final String Http = "https://dev.zizi.com.cn";
    /*开发服务器http的*/
//    public static final String Http = "http://61.183.182.34:8888";
    /* 正式服务器 */
    public static final String Http = "https://app.zizi.com.cn";
    /*赵梦景*/
//	 public static final String Http = "http://192.168.100.46:9000";

    /*cdn加速服务器*/
    //	 public static final String Http = "https://dev.zizi.com.cn";

    /*李桥*/
//    public static final String Http = "https://cdn.zizi.com.cn";

    public static final String IMAGEHOST = Http;
    public static final String HOST = Http + "/app/services";
    public static final String Qr_code = Http + "/app/qrcodeisgenerated";
    public static final String UPLOAD_IMAGE = Http + "/app/uploadUserPhoto";
    public static final String uploadGroupPhoto = Http + "/app/uploadGroupPhoto";
    public static final String uploadFriendCircle = Http + "/app/uploadFriendCircle";
    public static final String adduploadGroupPhoto = Http + "/app/adduploadGroupPhoto";
    public static final String Up_friend_cicle = Http + "/app/uploadbackgroundPicture";
}

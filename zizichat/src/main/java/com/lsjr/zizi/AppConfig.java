package com.lsjr.zizi;


public class AppConfig {

	//http://192.168.100.10:8092/config

	/*
	*
	* {"XMPPDomain":"192.168.100.10","XMPPHost":"192.168.100.10","androidAppUrl":""
	* ,"androidExplain":"","androidVersion":0,"apiUrl":"http://192.168.100.10:8092/","
	* audioLen":"20","displayRedPacket":1,"distance":20,"downloadAvatarUrl":"http://192.168.100.10/",
	* "downloadUrl":"http://192.168.100.10/","freeswitch":"","helpUrl":"","id":10000,"iosAppUrl":"",
	* "iosExplain":"","iosVersion":0,"liveUrl":"","meetingHost":"","shareUrl":"","softUrl":"",
	* "uploadUrl":"http://192.168.100.10:8081/upload/","videoLen":"20",
	* "xMPPDomain":"192.168.100.10",
	* "xMPPHost":"192.168.100.10"

	*
	* */

	/*李桥测试*/
	public static final String CONFIG_URL = "http://192.168.100.10:8092/config";

	/*李桥测试*/
	private static final String CONFIG_IP = "http://192.168.100.10:8092/";
	public static String USER_LOGIN=CONFIG_IP + "user/login";// 登陆
	public static final String DOWNLOAD_CIRCLE_MESSAGE = CONFIG_IP + "b/circle/msg/ids";// 下载商务圈消息
	public static final String FRIENDS_ATTENTION_LIST=CONFIG_IP + "friends/attention/list";// 获取关注列表

	public static final String USER_GET_URL=CONFIG_IP + "user/get";// 获取用户资料，更新本地数据的接口;

	public static final  String USER_PHOTO_LIST=CONFIG_IP + "user/photo/list";// 获取相册列表;// 获取相册列表

	public static final  String ROOM_LIST_HIS= CONFIG_IP + "/room/list/his";// 获取某个用户已加入的房间列表;

	public static final String USER_LOGIN_AUTO=CONFIG_IP + "user/login/auto";// 检测Token是否过期 0未更换 1更换过


	/* 商务圈 */
	public static final String USER_CIRCLE_MESSAGE=CONFIG_IP + "b/circle/msg/user/ids";// 获取某个人的商务圈消息

	//http://192.168.100.10:8081/
	private static final String CONFIG_UPLOAD = "http://192.168.100.10:8081/";
	public static final String UPLOAD_URL= CONFIG_UPLOAD + "upload/UploadServlet";// 上传图片接口;// 上传图片接口

	public static final String  XMPPHost="192.168.100.10";
	public static final int XMPP_PORT=5222 ;
}

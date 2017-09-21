package com.lsjr.bean;


public class Result {
	/**
	 * 通用的Http Result Code http 请求返回的结果码 <br/>
	 * 0表示一般性错误</br> 1-100表示成功</br> 大于100000表示一些详细的错误</br>
	 */
	public final static int CODE_ERROE = 0;// 未知的错误 或者系统内部错误
	public final static int CODE_SUCCESS = 1;// 正确的Http请求返回状态码
	public final static int CODE_ARGUMENT_ERROR1 = 1010101;// 请求参数验证失败，缺少必填参数或参数错误
	public final static int CODE_ARGUMENT_ERROR2 = 1010102;// 缺少请求参数：%1$s

	public final static int CODE_INTERNAL_ERROR = 1020101;// 接口内部异常
	public final static int CODE_NO_TOKEN = 1030101;// 缺少访问令牌
	public final static int CODE_TOKEN_ERROR = 1030102;// 访问令牌过期或无效
	/* 登陆接口的Http Result Code */
	public final static int CODE_ACCOUNT_INEXISTENCE = 1040101;// 帐号不存在
	public final static int CODE_ACCOUNT_ERROE = 1040102;// 帐号或密码错误

	public static final String RESULT_CODE = "resultCode";
	public static final String RESULT_MSG = "resultMsg";
	public static final String DATA = "data";

	public int resultCode;
	public String resultMsg;

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}


}

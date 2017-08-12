package com.lsjr.zizi.mvp.chat;

import android.app.Application;
import android.os.Environment;

import com.google.gson.Gson;
import com.lsjr.bean.ObjectResult;
import com.lsjr.bean.Result;
import com.lsjr.callback.ChatObjectCallBack;
import com.lsjr.net.DcodeService;
import com.lsjr.zizi.AppConfig;
import com.lsjr.zizi.http.HttpUtils;
import com.lsjr.zizi.mvp.chat.bean.ConfigBean;
import com.lsjr.zizi.mvp.chat.db.User;
import com.lsjr.zizi.mvp.chat.helper.SQLiteHelper;
import com.nostra13.universalimageloader.utils.L;

import java.io.File;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/8/4 15:08
 */

public class ConfigApplication {
    private static ConfigApplication configApplication;
    private static Application mApplication;


    /*********************** 保存当前登陆用户的全局信息 ***************/
    public String roomName;
    public String mAccessToken;
    public long mExpiresIn;
    public int mUserStatus;
    public boolean mUserStatusChecked = false;
    public User mLoginUser = new User();// 当前登陆的用户

    public String getmAccessToken() {
        return mAccessToken;
    }

    public void setmAccessToken(String mAccessToken) {
        this.mAccessToken = mAccessToken;
    }

    private ConfigApplication(){

    }

    public static ConfigApplication instance(){
        if (configApplication==null){
            synchronized (ConfigApplication.class){
                if (mApplication==null){
                    configApplication=new ConfigApplication();
                }
            }
        }
        return configApplication;
    }


    public void initialize(Application application) {
        mApplication=application;
        DcodeService.initialize(application);
        initConfig();
        // 初始化数据库
        SQLiteHelper.copyDatabaseFile(application);
    }



    /**
     * 配置参数初始化
     */
    private void initConfig() {
        HttpUtils.getInstance().postServiceData(AppConfig.CONFIG_URL, null,new ChatObjectCallBack<ConfigBean>(ConfigBean.class) {
            @Override
            protected void onXError(String exception) {

            }

            @Override
            protected void onSuccess(ObjectResult<ConfigBean> result) {
                ConfigBean configBean = null;
                if (result == null || result.getResultCode() != Result.CODE_SUCCESS || result.getData() == null) {
                    configBean = new ConfigBean();// 读取网络配置失败，使用默认配置
                    L.e("--------->读取网关配置失败",configBean.toString());
                } else {
                    configBean = result.getData();
                    L.e("--------->读取网关配置成功"+new Gson().toJson(configBean));
                }
            }
        });

    }

}

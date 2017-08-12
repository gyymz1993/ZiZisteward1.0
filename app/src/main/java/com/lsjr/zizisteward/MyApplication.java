package com.lsjr.zizisteward;

import android.app.Application;
import android.content.res.Configuration;
import android.graphics.Point;
import android.view.WindowManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.lsjr.net.BaseUrl;
import com.lsjr.net.DcodeService;
import com.lsjr.zizisteward.activity.contentprovider.Global;
import com.lsjr.zizisteward.http.AppUrl;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ymz.baselibrary.BaseApplication;
import com.ymz.baselibrary.utils.L_;

import cn.sharesdk.framework.ShareSDK;

/**
 * 创建人：gyymz1993
 * 创建时间：2017/4/14/12:13
 **/
public class MyApplication extends Application {


    // 自己微信应用的 appId
    public static String WX_APP_ID = "wx388b90f68846eb73";
    // 自己微信应用的 appSecret
    public static String WX_SECRET = "ab78f012fd5b2ce002cd1b3fe209265b";
    public static String WX_CODE = "";

    //商户号
    public static final String WX_MCH_ID = "1317019401";
    //  API密钥，在商户平台设置
    public static final String API_KEY = "34e5ae602475c96bf826cf425b9b77c5";


    // 自己qq应用的 appId
    public static String QQ_APP_ID = "1105677331";
    public static String QQ_APP_KEY = "v4RibiP3xgbECbX7";


    public static IWXAPI wxApi;
    //public static Tencent mTencent;

    @Override
    public void onCreate() {
        super.onCreate();
        resetDensity();
        Global.init(this);
        BaseApplication.instance().initialize(this);

        BaseUrl.setBastUrl(AppUrl.HOST);
        DcodeService.initialize(this);
        //UploadService.initialize(this);
        //LoaderFactory.initLoaderFactory(this);
        Fresco.initialize(this);

        LoadingLayoutInit();

        wxApi = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
        wxApi.registerApp(WX_APP_ID);

       // mTencent = Tencent.createInstance(QQ_APP_ID, this.getApplicationContext());

        /*微博*/
        ShareSDK.initSDK(this);
        //aliSophixManager();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        resetDensity();
    }
    public final static float DESIGN_WIDTH = 750; //绘制页面时参照的设计图宽度
    public void resetDensity(){
        Point size = new Point();
        ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getSize(size);
        getResources().getDisplayMetrics().xdpi = size.x/DESIGN_WIDTH*72f;
    }

    String appVersion = "1.1.0";
    public void aliSophixManager() {
        // initialize最好放在attachBaseContext最前面
        SophixManager.getInstance().setContext(this)
                .setAppVersion(appVersion)
                .setAesKey(null)
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        // 补丁加载回调通知
                        L_.e("补丁加载回调通知:" + handlePatchVersion);
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                            L_.e("补丁加载成功:" + handlePatchVersion);
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后应用自杀
                            L_.e("补丁生效需要重启. 开发者可提示用户或者强制重启:" + handlePatchVersion);
                        } else if (code == PatchStatus.CODE_LOAD_FAIL) {
                            // 内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载
                            // SophixManager.getInstance().cleanPatches();
                        } else {
                            // 其它错误信息, 查看PatchStatus类说明
                        }
                    }
                }).initialize();
        // queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中

    }

    private void LoadingLayoutInit() {

    }
}

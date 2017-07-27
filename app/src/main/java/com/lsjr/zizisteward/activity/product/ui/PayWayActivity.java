package com.lsjr.zizisteward.activity.product.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.lsjr.base.MvpActivity;
import com.lsjr.zizisteward.Config;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.product.presenter.PayWayPresenter;
import com.lsjr.zizisteward.activity.product.view.IPayWayListView;
import com.lsjr.zizisteward.bean.AliPayBean;
import com.lsjr.zizisteward.bean.WeiPayBean;
import com.lsjr.zizisteward.http.AppUrl;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.ymz.baselibrary.utils.L_;
import com.ymz.baselibrary.utils.SystemBarHelper;
import com.ymz.baselibrary.utils.T_;
import com.ymz.baselibrary.utils.UIUtils;
import com.ymz.baselibrary.widget.NavigationBarView;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.lsjr.zizisteward.MyApplication.wxApi;

/**
 * Created by admin on 2017/5/24.
 */

public class PayWayActivity extends MvpActivity<PayWayPresenter> implements IPayWayListView {

    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.id_nativgation_view)
    NavigationBarView idNativgationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface", "SetJavaScriptEnabled"})
    @Override
    protected void initView() {
        super.initView();
        Bundle bundle = getIntent().getExtras();
        String orderUrl = bundle.getString(Config.ORDERUTL);
        WebSettings webSettings = webview.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new PayWayH5Contrl(), Config.H5CONTRL);
        if (orderUrl != null) {
            webview.loadUrl(AppUrl.Http + orderUrl);
        }

    }

    @Override
    public void onNetWeiPayWayResult(String result) {
        L_.e("微信支付" + result);
        PayReq payReq = new PayReq();
        WeiPayBean wx_bean = new Gson().fromJson(result, WeiPayBean.class);
        List<WeiPayBean.AlipayValueBean> wx_code = wx_bean.getAlipayValue();
        payReq.appId = wx_code.get(0).getAppid();/*应用id*/
        payReq.partnerId = wx_code.get(0).getPartnerid();/*商户号*/
        payReq.packageValue = "Sign=WXPay";/*扩展字段*/
        payReq.prepayId = wx_code.get(0).getPrepayid();/*预支付订单号*/
        payReq.nonceStr = wx_code.get(0).getNoncestr();/*随机字符串*/
        payReq.timeStamp = wx_code.get(0).getTimestamp();/*时间戳*/
        payReq.sign = wx_code.get(0).getSign();/*签名*/
        if (payReq.checkArgs()) {
            wxApi.sendReq(payReq);
        } else {
            T_.showToastReal("请检查参数");
        }
    }

    private static final int SDK_PAY_FLAG = 1;

    @Override
    public void onNetZFBPayWayResult(String result) {
        L_.e("支付宝支付返回" + result);
        AliPayBean aliPayBean = new Gson().fromJson(result, AliPayBean.class);
        final String orderInfo = aliPayBean.getAlipayValue();
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayWayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                alipayHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    @Override
    protected void initTitle() {
        super.initTitle();
        SystemBarHelper.tintStatusBar(this, UIUtils.getColor(R.color.colorBlack));
        idNativgationView.setTitleText("选择付款");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_payway;
    }

    @Override
    protected void afterCreate(Bundle bundle) {

    }


    @SuppressLint("HandlerLeak")
    private Handler alipayHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    AliPayBean.PayResult payResult = new AliPayBean.PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        T_.showToastReal("支付成功");
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        T_.showToastReal("支付失败");
                    }
                    break;
                }
            }
        }
    };

    public class PayWayH5Contrl {
        public PayWayH5Contrl() {
            super();
        }

        /**
         * 微信支付交互
         * 和H5方法名 参数类型必须一致
         *
         * @param
         */
        @JavascriptInterface
        public void WXpayment(String body, String out_trade_no, String total_amount) {
            getWeiChatPayParamForNet(body, out_trade_no, total_amount);
        }


        /**
         * 支付宝支付
         *
         * @param body
         * @param subject
         * @param out_trade_no
         * @param total_amount
         */
        @JavascriptInterface
        public void ZFBpayment(String body, String subject, String out_trade_no, String total_amount) {
            getZFBPayParamForNet(body, subject, out_trade_no, total_amount);
        }


    }


    public void getZFBPayParamForNet(String body, String subject, String out_trade_no, String total_amount) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("OPT", "93");
        map.put("tradeNo", out_trade_no);
        map.put("amount", total_amount);
        map.put("description", body);
        map.put("title", subject);
        createPresenter().getZFBPayParam(map);
    }

    public void getWeiChatPayParamForNet(String body, String out_trade_no, String total_amount) {
        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "337");
        map.put("tradeNo", out_trade_no);
        map.put("amount", total_amount);
        map.put("body", body);
        map.put("user_ip", createPresenter().getIPAddress(this));
        createPresenter().getWeiPayParam(map);
    }


    @Override
    protected PayWayPresenter createPresenter() {
        return new PayWayPresenter(this);
    }


    @Override
    protected void onDestroy() {
        if (webview != null) {
            ViewParent parent = webview.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webview);
            }

            webview.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webview.getSettings().setJavaScriptEnabled(false);
            webview.clearHistory();
            webview.clearView();
            webview.removeAllViews();
            try {
                webview.destroy();
            } catch (Throwable ex) {

            }
        }
        super.onDestroy();

    }

    @Override
    public int getFragmentContentId() {
        return 0;
    }
}

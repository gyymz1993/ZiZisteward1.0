package com.lsjr.zizisteward.mvp.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.http.AppUrl;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

@SuppressLint({"SetJavaScriptEnabled", "NewApi", "JavascriptInterface"})
public abstract class NewPersonalActivity extends Activity implements Callback, Runnable {
    private Context context;
    private Handler mHandler;
    private ProgressDialog dialog = null;
    private final String mMode = "00";
    private WebView webview;
    private String push_link;

    //支付宝参数
    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "2017050207078320";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /**
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097
     * &docType=1
     */
    public static final String RSA2_PRIVATE = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCc5bRHWn8/NEk8iAImVHGI+VKeyZoaYAyk7Poa+xXPiKXaDrZRafhVnhP/MAaYLUucQUl5vUHYryVNgCrZOT0X5v29NAv8rZnemX7wIj0dPawU8zGv9ZxWUfEXDQbzansuVrNKg+1dtwA4/RDlS5A7tiReyS1lGEZGGuANfjKqjdzC1ArgKa8koySKQWviIiit9sF6zn41ezZFdn7ae2cuqZmcKbuSA+gW2bIvg8eaATu3aUksW1EblJRRGt0pJrUPXYEmtcbDUnoHYkqZvysQCWz8N9z8TVF5KQ+406J1DIeef7Dnxp5d9tUqVBDTZDxdRX6YyBakjCvMSuGqBmBDAgMBAAECggEBAI0pTi30ig//pvTDGcwKpaurRl5+3Btm13l7jPrBdTono7CxE+/j+/5sH2m1d/lqp2CKjOTvFlMyIcm8ytBt4z2iIiEctrw8JPhV+hNrerjYR/8BX28E+Afh6ZvnqJ1Q7Esgzmb4k5OBlr2vUog6mCZhFhAByMnwMSnZ/EOni0ZOtRXZdZSBH2JlT+KOQpFopKZA49RoqR+5oOUHwkxMr36OomwbQPht+khCoHo8roy/6RDEBFyMMTmRvmvtiHzhFxOTx7r2zfWAQdcPrNwBr4elgMapHtm0dJqoDiInYCnXYycuOlBc9g0fnggXMjv13eg227rYJIrGH+6TPe4C6UECgYEA84OVTYHrazzFPbz0H9SSvMY0nSe4qgOQMHDDIzjti0Z8kubogM/Cdm7yPj7Nh2N3dL0MBsAqfFMyHD4dO7C2vhMxlSh12CgSZjjK40xTFUQlIwvC9kxmcQADeBKlCDF9+o0ThT33C79zARVp7Mj1nsFLkTIAF22Y+24pQC4m1ekCgYEApPEr5kZweO2Gd/O1F4m7/hEZ9JQNI5NiajuK2EDGXt8IJx6TVXPZrz7H00LrhyYrEdeXgHz244TwAZwd9yV865/bJzN8CJpR/DEEsQHiVfo0LEs9mXsKiaT6I00aQZ6uU9WN2upC289gixdo+c8bix9MdA3de2PZJQ8eBDCV7UsCgYEAjERBHhI1/uFUZAmRPTx/AYnSCKw2rIe86IorfQBvpAgH/b5QMtJ5myqxErWuQcxDpNS4NrM+RbrOZIJK6HUT7ky0BMz3hHkgkA0qoN74BInqMlO2C5VadMCjPujOcve/LzFQCzH0OaofnoItL51aDgYddxcsAlK5CiscS8HJ0PECgYBAUkBRU4TG07HNkz86h57FDDw38YhSKEaHsOKLRG9XTdhrEPRZrYzlVzErxHv+vzaqhY1yMMlCnnPN3OiemYLmi/c1iVFENZHlK+RtdpOh/alc4JaMBLxuQuS84XNsxYmr7aqdBR+/glZex2lLiaVvEmyJEWMenChw2D1XWu8MkwKBgQDAdgYMD5eJlL5dfhDBmESWidU9HDsQrow7I0vLdgWpMpYuRaNxQUGSnVtri7R+o5P3sLcd4RPmWvKWwb8vr3CuQr4pXJZ/PzxKRSqzOAo+oLWg6gr/g+PDm8NqHO9jmPY2P3g1pmwqRn4LnmhIf7dQ3k8jlXEIUDpXk1/+UUcE/w==";
    public static final String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private IWXAPI api;

    @Override
    protected void onResume() {
        context = this;
        mHandler = new Handler(this);

        webview = (WebView) findViewById(R.id.webview);
        WebSettings settings = webview.getSettings();
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new DemoJavaInterface(), "control");
        webview.loadUrl(AppUrl.HOST + push_link);
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_personal);
        push_link = getIntent().getStringExtra("url");
    }


    private String number;
    private String transflow_num;
    private String totalPrice;

    public class DemoJavaInterface {

        public DemoJavaInterface() {
            super();
        }

        @JavascriptInterface
        public void sendGnumTransflowTotaPrice(String gnum, String transflow, String totaPrice) {// 确认下单
            CustomDialogUtils.startCustomProgressDialog(NewPersonalActivity.this, "正在调转银联支付...");
            System.out.println("订单号" + gnum + "交易号" + transflow);
            number = gnum;
            transflow_num = transflow;
            totalPrice = totaPrice;

            dialog = ProgressDialog.show(context, "", "正在努力的获取tn中,请稍候...", true);
            new Thread(NewPersonalActivity.this).start();

            CustomDialogUtils.stopCustomProgressDialog(NewPersonalActivity.this);
        }

        @JavascriptInterface
        public void usershopaid(String shopid, String userid, String amount, String addressid) {// 选择地址交互
            System.out.println("第一个" + shopid + "  第二个" + userid + "  第三个" + amount + "  第四个" + addressid);
//            Intent intent = new Intent(getApplicationContext(), SelectorAddressActivity.class);
//            intent.putExtra("shopid", shopid);
//            intent.putExtra("amount", amount);
//            startActivityForResult(intent, 1);
        }


        /*微信支付交互*/
        @JavascriptInterface
        public void WXpayment(String body, String out_trade_no, String total_amount) {
            System.out.print("描述" + body + "订单号" + out_trade_no + "钱" + total_amount);
            HashMap<String, String> map = new HashMap<>();
            map.put("OPT", "337");
            map.put("tradeNo", out_trade_no);
            map.put("amount", total_amount);
            map.put("body", body);
            map.put("user_ip", getIPAddress(NewPersonalActivity.this));


        }
    }




    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    public class WeiXinPayBean {
        public List<NewPersonalActivity.alipayValue> getAlipayValue() {
            return alipayValue;
        }

        public void setAlipayValue(List<NewPersonalActivity.alipayValue> alipayValue) {
            this.alipayValue = alipayValue;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        private List<alipayValue> alipayValue;
        private String error;
        private String msg;
    }

    public class alipayValue {
        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getMch_id() {
            return mch_id;
        }

        public void setMch_id(String mch_id) {
            this.mch_id = mch_id;
        }

        public String getNonce_str() {
            return nonce_str;
        }

        public void setNonce_str(String nonce_str) {
            this.nonce_str = nonce_str;
        }

        public String getPrepay_id() {
            return prepay_id;
        }

        public void setPrepay_id(String prepay_id) {
            this.prepay_id = prepay_id;
        }

        public String getResult_code() {
            return result_code;
        }

        public void setResult_code(String result_code) {
            this.result_code = result_code;
        }

        public String getReturn_code() {
            return return_code;
        }

        public void setReturn_code(String return_code) {
            this.return_code = return_code;
        }

        public String getReturn_msg() {
            return return_msg;
        }

        public void setReturn_msg(String return_msg) {
            this.return_msg = return_msg;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getTrade_type() {
            return trade_type;
        }

        public void setTrade_type(String trade_type) {
            this.trade_type = trade_type;
        }

        private String appid;
        private String mch_id;
        private String nonce_str;
        private String prepay_id;
        private String result_code;
        private String return_code;
        private String return_msg;
        private String sign;
        private String trade_type;

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        private String timestamp;
    }
}

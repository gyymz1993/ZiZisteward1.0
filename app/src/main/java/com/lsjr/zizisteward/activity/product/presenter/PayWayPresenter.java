package com.lsjr.zizisteward.activity.product.presenter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.lsjr.base.SubscriberCallBack;
import com.lsjr.net.DcodeService;
import com.lsjr.zizisteward.activity.product.view.IPayWayListView;
import com.ymz.baselibrary.mvp.BasePresenter;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by admin on 2017/5/24.
 */

public class PayWayPresenter extends BasePresenter<IPayWayListView> {
    public PayWayPresenter(IPayWayListView mvpView) {
        super(mvpView);
    }

    public void getWeiPayParam(Map  map){
        addSubscription(DcodeService.getServiceData(map), new SubscriberCallBack() {
            @Override
            protected void onError(Exception e) {
            }

            @Override
            protected void onFailure(String response) {

            }

            @Override
            protected void onSuccess(String response) {
                mvpView.onNetWeiPayWayResult(response);
            }
        });
    }


    public void getZFBPayParam(Map  map){
        addSubscription(DcodeService.getServiceData(map), new SubscriberCallBack() {
            @Override
            protected void onError(Exception e) {
            }

            @Override
            protected void onFailure(String response) {

            }

            @Override
            protected void onSuccess(String response) {
                mvpView.onNetZFBPayWayResult(response);
            }
        });
    }

    public  String getIPAddress(Context context) {
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


}

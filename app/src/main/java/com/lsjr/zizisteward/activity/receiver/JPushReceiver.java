package com.lsjr.zizisteward.activity.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {// 自定义消息
//            processCustomMessage(context, bundle);
            // Intent i = new Intent(context, SixthNewActivity.class);
            // // i.putExtras(bundle);
            // // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
            // // Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // context.startActivity(i);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {// 通知
            //通知栏的Notification ID，可以用于清除Notification
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);

            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);

            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
            //json
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);

            try {
                JSONObject object = new JSONObject(extras);
                String servicePlan = object.getString("servicePlan");
                System.out.print(servicePlan);
                //servicePlan 定制出行
                //
                if (servicePlan.equals("servicePlan")) {/*放在后台运行*/
                    // 打开自定义的Activity
//                    Intent i = new Intent(context, TestActivity.class);
//                    i.putExtras(bundle);
                    // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    context.startActivity(i);
//
//                    if (null != MinimalismInterface.context) {
//                        MinimalismInterface.getLevelOneService();
//                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String str = new String();

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {// 用户点击打开了通知

            // 打开自定义的Activity
//            Intent i = new Intent(context, TestActivity.class);
//            i.putExtras(bundle);
//             i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            context.startActivity(i);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" + myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    // send msg to MainActivity
//    private void processCustomMessage(Context context, Bundle bundle) {
//        if (ho.isForeground) {
//            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            Intent msgIntent = new Intent(SixthNewActivity.MESSAGE_RECEIVED_ACTION);
//            msgIntent.putExtra(SixthNewActivity.KEY_MESSAGE, message);
//            if (!ExampleUtil.isEmpty(extras)) {
//                try {
//                    JSONObject extraJson = new JSONObject(extras);
//                    if (null != extraJson && extraJson.length() > 0) {
//
//                        System.out.println("获取的结果" + extraJson.toString());
//                        System.out.println("后台给我" + message);
//                        msgIntent.putExtra(StartActivity.KEY_EXTRAS, extras);
//
//                        Intent intent = new Intent(context, MinimalismInterface.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.putExtra("message", message);
//                        context.startActivity(intent);
//                    }
//                } catch (JSONException e) {
//
//                }
//
//            }
//            context.sendBroadcast(msgIntent);
//        }
//    }
}

package com.lsjr.zizi.chat.xmpp;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.lsjr.zizi.AppConfig;
import com.lsjr.zizi.chat.db.ChatMessage;
import com.lsjr.zizi.chat.db.NewFriendMessage;
import com.lsjr.zizi.chat.xmpp.listener.AuthStateListener;
import com.lsjr.zizi.chat.xmpp.listener.ChatMessageListener;
import com.ymz.baselibrary.utils.L_;

import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.util.StringUtils;

public class CoreService extends Service {
	static final boolean DEBUG = true;
	static final String TAG = "Xmpp CoreService";

	private static final Intent SERVICE_INTENT = new Intent();
	static {
		SERVICE_INTENT.setComponent(new ComponentName("com.lsjr.zizi", "com.lsjr.zizi.chat.xmpp.CoreService"));
	}

	public static Intent getIntent() {
		return SERVICE_INTENT;
	}

	public static Intent getIntent(Context context, String userId, String password, String nickName) {
		Intent intent = new Intent(context, CoreService.class);
		intent.putExtra(EXTRA_LOGIN_USER_ID, userId);
		intent.putExtra(EXTRA_LOGIN_PASSWORD, password);
		intent.putExtra(EXTRA_LOGIN_NICK_NAME, nickName);
		return intent;
	}

	private static final String EXTRA_LOGIN_USER_ID = "login_user_id";
	private static final String EXTRA_LOGIN_PASSWORD = "login_password";
	private static final String EXTRA_LOGIN_NICK_NAME = "login_nick_name";

	// Binder
	public class CoreServiceBinder extends Binder {
		public CoreService getService() {
			return CoreService.this;
		}
	}

	private CoreServiceBinder mBinder;

	/* 当前登陆用户的基本属性 */
	private String mLoginUserId;
	@SuppressWarnings("unused")
	private String mLoginNickName;
	private String mLoginPassword;

	private SmackAndroid mSmackAndroid;// Smack唯一

	private XmppConnectionManager mConnectionManager;// 唯一
	private ReceiptManager mReceiptManager;// 唯一
	private XChatManager mXChatManager;// 唯一
	private XMucChatManager mXMucChatManager;// 唯一

	@Override
	public void onCreate() {
		super.onCreate();
		mSmackAndroid = SmackAndroid.init(this);
		mBinder = new CoreServiceBinder();
		if (CoreService.DEBUG) {
			Log.d(CoreService.TAG, "CoreService OnCreate");
		}

	}

	@Override
	public IBinder onBind(Intent intent) {// 绑定服务只是为了提供一些外部调用的方法
		if (CoreService.DEBUG) {
			Log.d(CoreService.TAG, "CoreService onBind");
		}
		return mBinder;
	}

	private void initConnection() {
		mConnectionManager = new XmppConnectionManager(this, mNotifyConnectionListener);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		release();
		if (mSmackAndroid != null) {
			mSmackAndroid.onDestroy();
			mSmackAndroid = null;
		}
		if (CoreService.DEBUG) {
			L_.e(CoreService.TAG, "CoreService onDestroy");
		}
	}

	private void release() {
		if (mConnectionManager != null) {
			mConnectionManager.release();
			mConnectionManager = null;
		}

		mReceiptManager = null;
		mXChatManager = null;
		mXMucChatManager = null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		L_.e( "CoreService onStartCommand:" + flags);
		if (intent != null) {
			mLoginUserId = intent.getStringExtra(EXTRA_LOGIN_USER_ID);
			mLoginPassword = intent.getStringExtra(EXTRA_LOGIN_PASSWORD);
			mLoginNickName = intent.getStringExtra(EXTRA_LOGIN_NICK_NAME);
		}
		L_.e( "登陆Xmpp账户为   mLoginUserId：" + mLoginUserId + "   mPassword：" + mLoginPassword);

		if (mConnectionManager != null) {
			// 因为Xmpp可能在后台重启，重启时读取的是保存在SharePreference中的Host和Port。 如果程序启动的时候，从服务器获取的Host和Port和本地不一致，那么就需要重新创建一个新的XmppConnection，并且重置使用前一个XmppConnection的所有对象
			String xmppHost = AppConfig.XMPPHost;// 当前的host
			int xmppPort = AppConfig.XMPP_PORT;// 当前的port
			if (mConnectionManager.getHost() == null || mConnectionManager.getPort() == 0 || !mConnectionManager.getHost().equals(xmppHost)
					|| mConnectionManager.getPort() != xmppPort) {
				release();
				L_.e("重新创建ConnectionManager");
			}
		}

		if (mConnectionManager == null) {
			initConnection();
		}

		if (!TextUtils.isEmpty(mLoginUserId) && !TextUtils.isEmpty(mLoginPassword)) {
			mConnectionManager.login(mLoginUserId, mLoginPassword);
		}
		// return START_REDELIVER_INTENT;//根据flags判断在登陆的时候是否需要验证Token过期信息
		return START_NOT_STICKY;
	}

	private NotifyConnectionListener mNotifyConnectionListener = new NotifyConnectionListener() {
		@Override
		public void notifyConnectionClosedOnError(Exception arg0) {
			L_.e("Xmpp连接异常断开");
			ListenerManager.getInstance().notifyAuthStateChange(AuthStateListener.AUTH_STATE_NOT);
		}

		@Override
		public void notifyConnectionClosed() {
			L_.e("Xmpp连接断开");
			ListenerManager.getInstance().notifyAuthStateChange(AuthStateListener.AUTH_STATE_NOT);
		}

		@Override
		public void notifyConnected(XMPPConnection arg0) {
			L_.e("Xmpp已经连接  notifyConnected"+arg0.getServiceName()+arg0.getUser());
			//init();
			ListenerManager.getInstance().notifyAuthStateChange(AuthStateListener.AUTH_STATE_SUCCESS);

		}

		@Override
		public void notifyAuthenticated(XMPPConnection arg0) {
			L_.e("Xmpp  Xmpp已经认证  notifyAuthenticated"+arg0.getUser());
			String connectionUserName = StringUtils.parseName(arg0.getUser());
			if (!connectionUserName.equals(mLoginUserId)) {
				L_.e( "Xmpp登陆账号不匹配，重新登陆");
				mConnectionManager.login(mLoginUserId, mLoginPassword);// 重新登陆
			} else {
				init();
				L_.e( "Xmpp  初始化");
				ListenerManager.getInstance().notifyAuthStateChange(AuthStateListener.AUTH_STATE_SUCCESS);// 通知登陆成功
			}
		}

		@Override
		public void notifyConnectting() {
			ListenerManager.getInstance().notifyAuthStateChange(AuthStateListener.AUTH_STATE_ING);
		}
	};

	public boolean isAuthenticated() {
		if (mConnectionManager != null && mConnectionManager.isAuthenticated()) {
			return true;
		}
		return false;
	}


	public void logout() {
		if (CoreService.DEBUG)
			L_.e( "Xmpp登出");
		if (mConnectionManager != null) {
			mConnectionManager.logout();
		}
		stopSelf();
	}

	private void init() {
		L_.e("XMPPInit","这里会初始化消息各种");
		if (!isAuthenticated()) {
			return;
		}

		/* 消息回执管理 */
		if (mReceiptManager == null) {
			mReceiptManager = new ReceiptManager(mConnectionManager.getConnection());
		} else {
			mReceiptManager.reset();
		}

		// 初始化消息处理
		if (mXChatManager == null) {
			mXChatManager = new XChatManager(this, mConnectionManager.getConnection());
		} else {
			mXChatManager.reset();
		}

		L_.e("初始化  mXChatManager---------》"+mXChatManager);
		/**
		 * TODO 群聊的暂时不加 做的时候要考虑<br/>
		 * 1、每次连接被断开，加入的房间是不是就退出了，如果是，那么每次都需要在这里new一个新的对象。 <br/>
		 * 2、XmppDomain可能因为重启导致改变
		 */
		if (mXMucChatManager == null) {
			mXMucChatManager = new XMucChatManager(this, mConnectionManager.getConnection());
		} else {
			// 重启导致Domain可能不一致
			// String xmppDomain=Config.getXmppDoMain(this);//当前的host
			// if(mXMucChatManager.getMucNickName(roomJid))
			mXMucChatManager.reset();
		}
		L_.e("初始化mXMucChatManager---------》"+mXMucChatManager);
		//mConnectionManager.presenceOnline();
		/* 获取离线消息 */
		L_.e("roamer", "获取离线消息去了");
		//mConnectionManager.handOfflineMessage();
	}

	// private void addProvider() {
	// ProviderManager providerManager = ProviderManager.getInstance();
	// providerManager.addIQProvider("query", "jabber:iq:jixiong:RoomIQHandler", new MucIQProvider());
	// }

	/**
	 * 发送聊天消息
	 *
	 * @param toUserId
	 * @param chatMessage
	 */
	public void sendChatMessage(String toUserId, ChatMessage chatMessage) {

		if (mXChatManager == null) {
			L_.e(CoreService.TAG, "mXChatManager==null");

		}
		if (!isAuthenticated()) {
			L_.e(CoreService.TAG, "isAuthenticated==false");
		}

		if (mReceiptManager == null) {
			L_.e(CoreService.TAG, "mReceiptManager==null");
		}

		if (mXChatManager == null || !isAuthenticated() || mReceiptManager == null) {
			ListenerManager.getInstance().notifyMessageSendStateChange(mLoginUserId, toUserId, chatMessage.get_id(),
					ChatMessageListener.MESSAGE_SEND_FAILED);
			L_.e("MESSAGE_SEND_FAILED");
		} else {
			mReceiptManager.addWillSendMessage(toUserId, chatMessage, ReceiptManager.SendType.NORMAL);
			mXChatManager.sendMessage(toUserId, chatMessage);
			L_.e( "sendMessage");
		}
	}

	/**
	 *
	 * @param toUserId
	 * @param message
	 * @return
	 */
	public void sendNewFriendMessage(String toUserId, NewFriendMessage message) {
		L_.e("发送一条新消息");
		if (mXChatManager == null || !isAuthenticated() || mReceiptManager == null) {
			ListenerManager.getInstance().notifyNewFriendSendStateChange(toUserId, message, ChatMessageListener.MESSAGE_SEND_FAILED);
		} else {
			mReceiptManager.addWillSendMessage(toUserId, message, ReceiptManager.SendType.PUSH_NEW_FRIEND);
			mXChatManager.sendMessage(toUserId, message);
		}
	}

	/* 群聊的外部接口 */
	public boolean isMucEnable() {
		return isAuthenticated() && mXMucChatManager != null;
	}

	public String createMucRoom(String myNickName, String roomName, String roomSubject, String roomDesc) {
		if (isMucEnable()) {
			return mXMucChatManager.createMucRoom(myNickName, roomName, roomSubject, roomDesc);
		}
		return null;
	}

	public void invite(String roomId, String userId, String reason) {
		if (isMucEnable()) {
			mXMucChatManager.invite(roomId, userId, reason);
		}
	}

	// 踢人
	public boolean kickParticipant(String roomJid, String nickName) {
		if (isMucEnable()) {
			return mXMucChatManager.kickParticipant(roomJid, nickName);
		}
		return false;
	}

	public void sendMucChatMessage(String toUserId, ChatMessage chatMessage) {
		L_.e("Xmpp-------------------》开始发送消息");
		if (mXMucChatManager == null) {
			L_.e("mXMucChatManager==null");
		}
		if (!isAuthenticated()) {
			L_.e( "isAuthenticated==false");

		}

		if (mReceiptManager == null) {
			L_.e("mReceiptManager==null");
		}

		if (mXMucChatManager == null || !isAuthenticated() || mReceiptManager == null) {
			ListenerManager.getInstance().notifyMessageSendStateChange(mLoginUserId, toUserId, chatMessage.get_id(),
					ChatMessageListener.MESSAGE_SEND_FAILED);
		} else {
			mReceiptManager.addWillSendMessage(toUserId, chatMessage, ReceiptManager.SendType.NORMAL);
			mXMucChatManager.sendMessage(toUserId, chatMessage);
		}

	}

	/* 加入群聊 */
	public void joinMucChat(String toUserId, String nickName, int lastSeconds) {
		if (isMucEnable()) {
			mXMucChatManager.joinMucChat(toUserId, nickName, lastSeconds);
		}
	}

	public void exitMucChat(String toUserId) {
		if (isMucEnable()) {
			mXMucChatManager.exitMucChat(toUserId);
		}
	}
}

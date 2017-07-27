package com.lsjr.zizisteward.bean;

import java.io.Serializable;
import java.util.List;

public class AddressBookBean implements Serializable{

	/**
	 * friends : [{"account":"18602712500","entityId":309,"firstPinYin":"S","friend_id":246,"friend_name":"","id":309,"name":"18602712500","persistent":false,"photo":"/images?uuid=9b4e8203-50fe-43e0-ad88-8384f27fa10e","pinYin":"shangguanhaoxianghaoxianghao","user_id":77,"user_name":"上官好想好想好"}]
	 * error : 1
	 * userPhoto : /images?uuid=69c8fc4a-c111-4150-b1f7-eb73a9f16eec
	 * userUser_id : 77
	 * userUser_name : 好的
	 * msg : 查看好友成功
	 */

	private String error;
	private String userPhoto;
	private int userUser_id;
	private String userUser_name;
	private String msg;
	private List<FriendsBean> friends;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}

	public int getUserUser_id() {
		return userUser_id;
	}

	public void setUserUser_id(int userUser_id) {
		this.userUser_id = userUser_id;
	}

	public String getUserUser_name() {
		return userUser_name;
	}

	public void setUserUser_name(String userUser_name) {
		this.userUser_name = userUser_name;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<FriendsBean> getFriends() {
		return friends;
	}

	public void setFriends(List<FriendsBean> friends) {
		this.friends = friends;
	}

	public static class FriendsBean  implements Serializable {
		/**
		 * account : 18602712500
		 * entityId : 309
		 * firstPinYin : S
		 * friend_id : 246
		 * friend_name :
		 * id : 309
		 * name : 18602712500
		 * persistent : false
		 * photo : /images?uuid=9b4e8203-50fe-43e0-ad88-8384f27fa10e
		 * pinYin : shangguanhaoxianghaoxianghao
		 * user_id : 77
		 * user_name : 上官好想好想好
		 */

		private String account;
		private int entityId;
		private String firstPinYin;
		private int friend_id;
		private String friend_name;
		private int id;
		private String name;
		private boolean persistent;
		private String photo;
		private String pinYin;
		private int user_id;
		private String user_name;

		public String getAccount() {
			return account;
		}

		public void setAccount(String account) {
			this.account = account;
		}

		public int getEntityId() {
			return entityId;
		}

		public void setEntityId(int entityId) {
			this.entityId = entityId;
		}

		public String getFirstPinYin() {
			return firstPinYin;
		}

		public void setFirstPinYin(String firstPinYin) {
			this.firstPinYin = firstPinYin;
		}

		public int getFriend_id() {
			return friend_id;
		}

		public void setFriend_id(int friend_id) {
			this.friend_id = friend_id;
		}

		public String getFriend_name() {
			return friend_name;
		}

		public void setFriend_name(String friend_name) {
			this.friend_name = friend_name;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isPersistent() {
			return persistent;
		}

		public void setPersistent(boolean persistent) {
			this.persistent = persistent;
		}

		public String getPhoto() {
			return photo;
		}

		public void setPhoto(String photo) {
			this.photo = photo;
		}

		public String getPinYin() {
			return pinYin;
		}

		public void setPinYin(String pinYin) {
			this.pinYin = pinYin;
		}

		public int getUser_id() {
			return user_id;
		}

		public void setUser_id(int user_id) {
			this.user_id = user_id;
		}

		public String getUser_name() {
			return user_name;
		}

		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}

		@Override
		public String toString() {
			return "FriendsBean{" +
					"account='" + account + '\'' +
					", entityId=" + entityId +
					", firstPinYin='" + firstPinYin + '\'' +
					", friend_id=" + friend_id +
					", friend_name='" + friend_name + '\'' +
					", id=" + id +
					", name='" + name + '\'' +
					", persistent=" + persistent +
					", photo='" + photo + '\'' +
					", pinYin='" + pinYin + '\'' +
					", user_id=" + user_id +
					", user_name='" + user_name + '\'' +
					'}';
		}
	}
}

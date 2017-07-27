package com.lsjr.zizisteward.bean;

import java.io.Serializable;

public class BannerInfo implements Serializable {

		private String id;
		private String image_filename;
		private String url;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getImage_filename() {
			return image_filename;
		}

		public void setImage_filename(String image_filename) {
			this.image_filename = image_filename;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

	}
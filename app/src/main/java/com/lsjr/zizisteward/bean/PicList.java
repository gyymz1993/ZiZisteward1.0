package com.lsjr.zizisteward.bean;

public class PicList {
    private String id;
		private String image_filename;
		private String location;
		private String url;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getImage_filename() {
			return image_filename;
		}

		public void setImage_filename(String image_filename) {
			this.image_filename = image_filename;
		}

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

	@Override
	public String toString() {
		return "PicList{" +
				"id='" + id + '\'' +
				", image_filename='" + image_filename + '\'' +
				", location='" + location + '\'' +
				", url='" + url + '\'' +
				'}';
	}
}

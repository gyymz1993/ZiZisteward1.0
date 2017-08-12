package com.lsjr.bean;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class ArrayResult<T> extends Result{
	private List<T> data;

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return JSON.toJSON(this).toString();
	}

}

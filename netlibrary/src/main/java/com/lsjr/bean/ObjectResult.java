package com.lsjr.bean;


import com.alibaba.fastjson.JSON;

public class ObjectResult<T> extends Result{
	private T data;


	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return JSON.toJSON(this).toString();
	}
	
}

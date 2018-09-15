package com.shenzhou.intelligenceordering.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 返回结果对象
 * 
 */
public class ResultVo<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 返回结果标记，默认0代表成功
	 */
	private String code;
	
	/**
	 * 返回结果信息
	 */
	private String message;
	
	/**
	 * 返回结果集
	 */
	private List<T> resultList;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<T> getResultList() {
		return resultList;
	}

	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}
	
}
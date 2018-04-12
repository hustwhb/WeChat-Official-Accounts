package com.hbwan.pojo.message.req;

/**
 * 图片消息
 * 
 * @author hbwan
 * @date 2018-04-12
 */
public class ImageMessage extends BaseMessage {
	// 图片链接
	private String PicUrl;

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}
}
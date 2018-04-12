package com.hbwan.pojo.message.resp;

/**
 * 音乐消息
 * 
 * @author hbwan
 * @date 2018-04-12
 */
public class MusicMessage extends BaseMessage {
	// 音乐
	private Music Music;

	public Music getMusic() {
		return Music;
	}

	public void setMusic(Music music) {
		Music = music;
	}
}
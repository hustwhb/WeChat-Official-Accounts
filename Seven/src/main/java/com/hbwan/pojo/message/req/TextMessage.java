package com.hbwan.pojo.message.req;

 /**
 * 文本消息
 *
 * @author hbwan
 * @date 2018-04-12
 */
 public class TextMessage extends BaseMessage {
 // 消息内容
 private String Content;

 public String getContent() {
 return Content;
 }

 public void setContent(String content) {
 Content = content;
 }
 }
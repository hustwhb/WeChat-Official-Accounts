package com.hbwan.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hbwan.pojo.message.resp.Article;
import com.hbwan.pojo.message.resp.BaseMessage;
import com.hbwan.pojo.message.resp.NewsMessage;
import com.hbwan.pojo.message.resp.TextMessage;
import com.hbwan.service.CoreService;
import com.hbwan.utils.MessageUtil;


@Service
public class CoreServiceImpl implements CoreService{

	
		/** 
	     * 处理微信发来的请求 
	     *  
	     * @param request 
	     * @return 
	     */  
		@Override
	    public  String processRequest(HttpServletRequest request) {  
	        String respMessage = null;  
	        try {  
	            // 默认返回的文本消息内容  
	            String respContent = "请求处理异常，请稍候尝试！";  
	  
	            // xml请求解析  
	            Map<String, String> requestMap = MessageUtil.parseXml(request);  
	  
	            // 发送方帐号（open_id）  
	            String fromUserName = requestMap.get("FromUserName");  
	            // 公众帐号  
	            String toUserName = requestMap.get("ToUserName");  
	            // 消息类型  
	            String msgType = requestMap.get("MsgType");  
	  
	            // 回复文本消息  
	            TextMessage textMessage = new TextMessage();  
	            textMessage.setToUserName(fromUserName);  
	            textMessage.setFromUserName(toUserName);  
	            textMessage.setCreateTime(new Date().getTime());  
	            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);  
	            textMessage.setFuncFlag(0);  
	  
	            // 文本消息  
	            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {  
	            	// 文本消息内容  
	                String content = requestMap.get("Content"); 
	            	if (isQqFace(content)) {
	            		respContent = content;//"您发送的是表情！"; 
					} else if(content.equals("1")||content.equals("2")||content.equals("3")||content.equals("4")||content.equals("5")){
						respMessage = MessageUtil.newsMessageToXml(newsMessage(content,fromUserName,toUserName));  //图文消息
						return respMessage;  
						
					}else {
							respContent = getMainMenu();//"您发送的是文本消息！"; 
					}
	                 
	            }  
	            // 图片消息  
	            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {  
	                respContent = "您发送的是图片消息！";  
	            }  
	            // 地理位置消息  
	            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {  
	                respContent = "您发送的是地理位置消息！";  
	            }  
	            // 链接消息  
	            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {  
	                respContent = "您发送的是链接消息！";  
	            }  
	            // 音频消息  
	            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {  
	                respContent = "您发送的是音频消息！";  
	            }  
	            // 事件推送  
	            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {  
	                // 事件类型  
	                String eventType = requestMap.get("Event");  
	                // 订阅  
	                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {  
	                    respContent = "谢谢您的关注！";  
	                }  
	                // 取消订阅  
	                else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {  
	                    // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息  
	                }  
	                // 自定义菜单点击事件  
	                else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {  
	                    // TODO 自定义菜单权没有开放，暂不处理该类消息  
	                	respContent = "您点击了自定义菜单！";
	                	System.out.println("您点击了自定义菜单");
	                }  
	            }  
	  
	            textMessage.setContent(respContent);  
	            respMessage = MessageUtil.textMessageToXml(textMessage);  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	  
	        return respMessage;  
	    }  
		
		/**
		 * xiaoqrobot的主菜单
		 * 
		 * @return
		 */
		public  String getMainMenu() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("您好，我是小q，请回复数字选择服务：").append("\n\n");
			buffer.append("1  天气预报").append("\n");
			buffer.append("2  公交查询").append("\n");
			buffer.append("3  周边搜索").append("\n");
			buffer.append("4  歌曲点播").append("\n");
			buffer.append("5  经典游戏").append("\n");
			buffer.append("6  美女电台").append("\n");
			buffer.append("7  人脸识别").append("\n");
			buffer.append("8  聊天唠嗑/::D").append("\n\n");
			buffer.append("回复“?”显示此帮助菜单\n\n");
			buffer.append("<a href=\"https://www.baidu.com\">百度一下</a>");
			
			return buffer.toString();
		}
		
		   /** 
	     * emoji表情转换(hex -> utf-16) 
	     *  
	     * @param hexEmoji 
	     * @return 
	     */  
	    public static String emoji(int hexEmoji) {  
	        return String.valueOf(Character.toChars(hexEmoji));  
	    } 
		
		/**
		 * 判断是否是QQ表情
		 * 
		 * @param content
		 * @return
		 */
		public  boolean isQqFace(String content) {
			boolean result = false;

			// 判断QQ表情的正则表达式
			String qqfaceRegex = "/::\\)|/::~|/::B|/::\\||/:8-\\)|/::<|/::$|/::X|/::Z|/::'\\(|/::-\\||/::@|/::P|/::D|/::O|/::\\(|/::\\+|/:--b|/::Q|/::T|/:,@P|/:,@-D|/::d|/:,@o|/::g|/:\\|-\\)|/::!|/::L|/::>|/::,@|/:,@f|/::-S|/:\\?|/:,@x|/:,@@|/::8|/:,@!|/:!!!|/:xx|/:bye|/:wipe|/:dig|/:handclap|/:&-\\(|/:B-\\)|/:<@|/:@>|/::-O|/:>-\\||/:P-\\(|/::'\\||/:X-\\)|/::\\*|/:@x|/:8\\*|/:pd|/:<W>|/:beer|/:basketb|/:oo|/:coffee|/:eat|/:pig|/:rose|/:fade|/:showlove|/:heart|/:break|/:cake|/:li|/:bome|/:kn|/:footb|/:ladybug|/:shit|/:moon|/:sun|/:gift|/:hug|/:strong|/:weak|/:share|/:v|/:@\\)|/:jj|/:@@|/:bad|/:lvu|/:no|/:ok|/:love|/:<L>|/:jump|/:shake|/:<O>|/:circle|/:kotow|/:turn|/:skip|/:oY|/:#-0|/:hiphot|/:kiss|/:<&|/:&>";
			Pattern p = Pattern.compile(qqfaceRegex);
			Matcher m = p.matcher(content);
			if (m.matches()) {
				result = true;
			}
			return result;
		}
		
		public NewsMessage newsMessage(String content,String fromUserName,String toUserName) {
			// 文本消息  
        
//			   // 默认回复此文本消息  
//            TextMessage textMessage = new TextMessage();  
//            textMessage.setToUserName(fromUserName);  
//            textMessage.setFromUserName(toUserName);  
//            textMessage.setCreateTime(new Date().getTime());  
//            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);  
//            textMessage.setFuncFlag(0);  
//            // 由于href属性值必须用双引号引起，这与字符串本身的双引号冲突，所以要转义  
//            textMessage.setContent("欢迎访问<a href=\"http://blog.csdn.net/lyq8479\">柳峰的博客</a>!");  
//            // 将文本消息对象转换成xml字符串  
//          String  respMessage = MessageUtil.textMessageToXml(textMessage);  
  
                // 创建图文消息  
                NewsMessage newsMessage = new NewsMessage();  
                newsMessage.setToUserName(fromUserName);  
                newsMessage.setFromUserName(toUserName);  
                newsMessage.setCreateTime(new Date().getTime());  
                newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);  
                newsMessage.setFuncFlag(0);  
  
                List<Article> articleList = new ArrayList<Article>();  
                // 单图文消息  
                if ("1".equals(content)) {  
                    Article article = new Article();  
                    article.setTitle("微信公众帐号开发教程Java版");  
                    article.setDescription("柳峰，80后，微信公众帐号开发经验4个月。为帮助初学者入门，特推出此系列教程，也希望借此机会认识更多同行！");  
                    article.setPicUrl("http://0.xiaoqrobot.duapp.com/images/avatar_liufeng.jpg");  
                    article.setUrl("http://blog.csdn.net/lyq8479");  
                    articleList.add(article);  
                    // 设置图文消息个数  
                    newsMessage.setArticleCount(articleList.size());  
                    // 设置图文消息包含的图文集合  
                    newsMessage.setArticles(articleList);  
                    // 将图文消息对象转换成xml字符串  
                     
                }  
                // 单图文消息---不含图片  
                else if ("2".equals(content)) {  
                    Article article = new Article();  
                    article.setTitle("微信公众帐号开发教程Java版");  
                    // 图文消息中可以使用QQ表情、符号表情  
                    article.setDescription("柳峰，80后，" + emoji(0x1F6B9)  
                            + "，微信公众帐号开发经验4个月。为帮助初学者入门，特推出此系列连载教程，也希望借此机会认识更多同行！\n\n目前已推出教程共12篇，包括接口配置、消息封装、框架搭建、QQ表情发送、符号表情发送等。\n\n后期还计划推出一些实用功能的开发讲解，例如：天气预报、周边搜索、聊天功能等。");  
                    // 将图片置为空  
                    article.setPicUrl("");  
                    article.setUrl("http://blog.csdn.net/lyq8479");  
                    articleList.add(article);  
                    newsMessage.setArticleCount(articleList.size());  
                    newsMessage.setArticles(articleList);  
                }  
                // 多图文消息  
                else if ("3".equals(content)) {  
                    Article article1 = new Article();  
                    article1.setTitle("微信公众帐号开发教程\n引言");  
                    article1.setDescription("");  
                    article1.setPicUrl("http://0.xiaoqrobot.duapp.com/images/avatar_liufeng.jpg");  
                    article1.setUrl("http://blog.csdn.net/lyq8479/article/details/8937622");  
  
                    Article article2 = new Article();  
                    article2.setTitle("第2篇\n微信公众帐号的类型");  
                    article2.setDescription("");  
                    article2.setPicUrl("http://avatar.csdn.net/1/4/A/1_lyq8479.jpg");  
                    article2.setUrl("http://blog.csdn.net/lyq8479/article/details/8941577");  
  
                    Article article3 = new Article();  
                    article3.setTitle("第3篇\n开发模式启用及接口配置");  
                    article3.setDescription("");  
                    article3.setPicUrl("http://avatar.csdn.net/1/4/A/1_lyq8479.jpg");  
                    article3.setUrl("http://blog.csdn.net/lyq8479/article/details/8944988");  
  
                    articleList.add(article1);  
                    articleList.add(article2);  
                    articleList.add(article3);  
                    newsMessage.setArticleCount(articleList.size());  
                    newsMessage.setArticles(articleList);  
                }  
                // 多图文消息---首条消息不含图片  
                else if ("4".equals(content)) {  
                    Article article1 = new Article();  
                    article1.setTitle("微信公众帐号开发教程Java版");  
                    article1.setDescription("");  
                    // 将图片置为空  
                    article1.setPicUrl("");  
                    article1.setUrl("http://blog.csdn.net/lyq8479");  
  
                    Article article2 = new Article();  
                    article2.setTitle("第4篇\n消息及消息处理工具的封装");  
                    article2.setDescription("");  
                    article2.setPicUrl("http://avatar.csdn.net/1/4/A/1_lyq8479.jpg");  
                    article2.setUrl("http://blog.csdn.net/lyq8479/article/details/8949088");  
  
                    Article article3 = new Article();  
                    article3.setTitle("第5篇\n各种消息的接收与响应");  
                    article3.setDescription("");  
                    article3.setPicUrl("http://avatar.csdn.net/1/4/A/1_lyq8479.jpg");  
                    article3.setUrl("http://blog.csdn.net/lyq8479/article/details/8952173");  
  
                    Article article4 = new Article();  
                    article4.setTitle("第6篇\n文本消息的内容长度限制揭秘");  
                    article4.setDescription("");  
                    article4.setPicUrl("http://avatar.csdn.net/1/4/A/1_lyq8479.jpg");  
                    article4.setUrl("http://blog.csdn.net/lyq8479/article/details/8967824");  
  
                    articleList.add(article1);  
                    articleList.add(article2);  
                    articleList.add(article3);  
                    articleList.add(article4);  
                    newsMessage.setArticleCount(articleList.size());  
                    newsMessage.setArticles(articleList);  
                }  
                // 多图文消息---最后一条消息不含图片  
                else if ("5".equals(content)) {  
                    Article article1 = new Article();  
                    article1.setTitle("第7篇\n文本消息中换行符的使用");  
                    article1.setDescription("");  
                    article1.setPicUrl("http://0.xiaoqrobot.duapp.com/images/avatar_liufeng.jpg");  
                    article1.setUrl("http://blog.csdn.net/lyq8479/article/details/9141467");  
  
                    Article article2 = new Article();  
                    article2.setTitle("第8篇\n文本消息中使用网页超链接");  
                    article2.setDescription("");  
                    article2.setPicUrl("http://avatar.csdn.net/1/4/A/1_lyq8479.jpg");  
                    article2.setUrl("http://blog.csdn.net/lyq8479/article/details/9157455");  
  
                    Article article3 = new Article();  
                    article3.setTitle("如果觉得文章对你有所帮助，请通过博客留言或关注微信公众帐号xiaoqrobot来支持柳峰！");  
                    article3.setDescription("");  
                    // 将图片置为空  
                    article3.setPicUrl("");  
                    article3.setUrl("http://blog.csdn.net/lyq8479");  
  
                    articleList.add(article1);  
                    articleList.add(article2);  
                    articleList.add(article3);  
                    newsMessage.setArticleCount(articleList.size());  
                    newsMessage.setArticles(articleList);  
                }  
                return newsMessage; 
            } 
		
	}


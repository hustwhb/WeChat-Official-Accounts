package com.hbwan.web.controller;
/**
 * @author hbwan
 * Application信息维护，实现增删改，分页显示，根据应用名称模糊查询
 */



import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hbwan.service.CoreService;
import com.hbwan.utils.SignUtil;

/*
 * application增删改查
 */
@Controller
public class MainController {
	

	@Autowired
	CoreService coreService;
	
	
	 /** 
     * 确认请求来自微信服务器 
     */ 
	@RequestMapping(value="/coreServlet",produces="application/json;charset=UTF-8",method=RequestMethod.GET)
	@ResponseBody
	public String coreServlet(HttpServletRequest request,HttpServletResponse response) throws IOException {
				// 微信加密签名
		 String signature = request.getParameter("signature");
		 // 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		 String nonce = request.getParameter("nonce");
		 // 随机字符串
		 String echostr = request.getParameter("echostr");

		 // 通过检验signature 对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		 if (SignUtil.checkSignature(signature, timestamp, nonce)) {
			 return echostr;
		 }else {
			return "error";
		}

	}
	
	
	/** 
     * 处理微信服务器发来的消息 
     */
	@RequestMapping(value="/coreServlet",produces="application/json;charset=UTF-8",method=RequestMethod.POST)
	@ResponseBody
	public void coreServletPOST(HttpServletRequest request,HttpServletResponse response) throws IOException {
				// 微信加密签名
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）  
	    request.setCharacterEncoding("UTF-8");  
	    response.setCharacterEncoding("UTF-8");  

	    // 调用核心业务类接收消息、处理消息  
	    String respMessage = coreService.processRequest(request);  
	      
	    // 响应消息  
	    PrintWriter out = response.getWriter();  
	    out.print(respMessage);  
	    out.close(); 
	}
	
	@RequestMapping(value="/test",produces="application/json;charset=UTF-8",method=RequestMethod.POST)
	@ResponseBody
	public boolean testPost(HttpServletRequest request,HttpServletResponse response) throws IOException {
	return true;
	}   
	
	@RequestMapping(value="/test",produces="application/json;charset=UTF-8",method=RequestMethod.GET)
	@ResponseBody
	public boolean testGet(HttpServletRequest request,HttpServletResponse response) throws IOException {
	return true;
	} 
}

package com.ischoolbar.programmer.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ischoolbar.programmer.util.CpachaUtil;
import com.ischoolbar.programmer.util.StringUtil;

public class CpachaServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
//		super.doPost(req, resp);
		String method = req.getParameter("method");
		if("LoginCpacha".equals(method)) {
			getLoginCpacha(req, resp);
		}
	}

	private void getLoginCpacha(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		String vl = req.getParameter("vl");
		String fs = req.getParameter("fs");
		int vcodeLen = 4;
		int fontSize = 21;
		if(!StringUtil.isEmpty(vl)) {
			vcodeLen = Integer.parseInt(vl);
		}
		if(!StringUtil.isEmpty(fs)) {
			fontSize = Integer.parseInt(fs);
		}
		CpachaUtil cpachaUtil = new CpachaUtil(vcodeLen, fontSize);
		String generatorVCode = cpachaUtil.generatorVCode();
		// 把生产的验证码放入session 用来登录时验证
		req.getSession().setAttribute("LoginCpacha", generatorVCode);
		BufferedImage generatorRotateVCodeImage = cpachaUtil.generatorRotateVCodeImage(generatorVCode, true);
		try {
			ImageIO.write(generatorRotateVCodeImage, "gif", resp.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

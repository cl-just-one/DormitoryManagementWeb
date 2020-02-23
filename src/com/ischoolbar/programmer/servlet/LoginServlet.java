package com.ischoolbar.programmer.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ischoolbar.programmer.dao.AdminDao;
import com.ischoolbar.programmer.entity.Admin;
import com.ischoolbar.programmer.util.StringUtil;

/**
 * 登录 
 */
public class LoginServlet extends HttpServlet {
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
		String name = req.getParameter("account");
		String password = req.getParameter("password");
		String vcode = req.getParameter("vcode");
		String msg = "success";
		if(StringUtil.isEmpty(name)) {
			msg = "用户名不能为空！";
		}
		if(StringUtil.isEmpty(password)) {
			msg = "密码不能为空！";
		}
		if(StringUtil.isEmpty(vcode)) {
			msg = "验证码不能为空！";
		}
		if("success".equals(msg)) {
			Object LoginCpacha = req.getSession().getAttribute("LoginCpacha");
			if (LoginCpacha == null) {
				msg = "session已过期，请刷新重试！";
			} else {
				if (!vcode.toUpperCase().equals(LoginCpacha.toString().toUpperCase())) {
					msg = "验证码错误！";
				}
			}
		}
		if("success".equals(msg)) {
			String typeStr = req.getParameter("type");
			try {
				int type = Integer.parseInt(typeStr);
				if (type == 1) {
					// 超级管理员
					AdminDao adminDao = new AdminDao();
					Admin admin = adminDao.getAdmin(name);
					if (admin == null) {
						msg = "用户不存在！";
					} else {
						if (!password.equals(admin.getPassword())) {
							msg = "密码错误！";
						} else {
							req.getSession().setAttribute("user", admin);
							req.getSession().setAttribute("userType", type);
						}
					}
				} else if (type == 2) {
					
				} else if (type == 3) {
					
				} else {
					
				}
			} catch (Exception e) {
				// TODO: handle exception
				msg = "用户类型错误！";
			}
		}
		resp.setCharacterEncoding("utf-8");
		resp.getWriter().write(msg);
	}
}

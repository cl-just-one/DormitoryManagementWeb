package com.ischoolbar.programmer.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ischoolbar.programmer.util.StringUtil;

/**
 * ��¼ 
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
		String account = req.getParameter("account");
		String password = req.getParameter("password");
		String vcode = req.getParameter("vcode");
		String msg = "success";
		if(StringUtil.isEmpty(account)) {
			msg = "�û�������Ϊ�գ�";
		}
		if(StringUtil.isEmpty(password)) {
			msg = "���벻��Ϊ�գ�";
		}
		if(StringUtil.isEmpty(vcode)) {
			msg = "��֤�벻��Ϊ�գ�";
		}
		if("success".equals(msg)) {
			Object LoginCpacha = req.getSession().getAttribute("LoginCpacha");
			if (LoginCpacha == null) {
				msg = "session�ѹ��ڣ���ˢ�����ԣ�";
			} else {
				if (!vcode.toUpperCase().equals(LoginCpacha.toString().toUpperCase())) {
					msg = "��֤�����";
				}
			}
			
		}
		resp.setCharacterEncoding("utf-8");
		resp.getWriter().write(msg);
	}
}

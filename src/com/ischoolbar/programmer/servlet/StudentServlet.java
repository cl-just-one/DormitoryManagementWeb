package com.ischoolbar.programmer.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ischoolbar.programmer.dao.StudentDao;
import com.ischoolbar.programmer.entity.Student;
import com.ischoolbar.programmer.util.StringUtil;

public class StudentServlet extends HttpServlet {

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
		String method = req.getParameter("method");
		if ("toStudentListView".equals(method)) {
			req.getRequestDispatcher("view/StudentList.jsp").forward(req, resp);
		}
		if ("AddStudent".equals(method)) {
			addStudent(req, resp);
		}
	}

	private void addStudent(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// TODO Auto-generated method stub
		String name = req.getParameter("name");
		String password = req.getParameter("password");
		String sex = req.getParameter("sex");
		resp.setCharacterEncoding("utf-8");
		if (StringUtil.isEmpty(name)) {
			resp.getWriter().write("姓名不能为空！");
			return;
		}
		if (StringUtil.isEmpty(password)) {
			resp.getWriter().write("密码不能为空！");
			return;
		}
		if (StringUtil.isEmpty(sex)) {
			resp.getWriter().write("性别不能为空！");
			return;
		}
		
		Student student = new Student();
		student.setName(name);
		student.setPassword(password);
		student.setSex(sex);
		student.setSn(StringUtil.generateSn("S", ""));
		
		StudentDao studentDao = new StudentDao();
		String msg = "添加失败";
		if (studentDao.add(student)) {
			msg = "success";
		}
		studentDao.closeConnection();
		resp.getWriter().write(msg);
	}

}

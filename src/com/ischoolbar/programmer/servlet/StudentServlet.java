package com.ischoolbar.programmer.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.ischoolbar.programmer.bean.Page;
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
		if ("StudentList".equals(method)) {
			getStudengtList(req, resp);
		}
	}

	private void getStudengtList(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub
		Map<String, Object> ret = new HashMap<String, Object>();
		
		int pageNumber = Integer.parseInt(req.getParameter("page"));
		int pageSize = Integer.parseInt(req.getParameter("rows"));
		String name = req.getParameter("name");
		
		Student student = new Student();
		student.setName(name);
		
		StudentDao studentDao = new StudentDao();
		Page page = new Page(pageNumber, pageSize);
		ret.put("rows", studentDao.findList(student, page));
		ret.put("total", studentDao.getTotal(student));
		studentDao.closeConnection();
		resp.setCharacterEncoding("utf-8");
		try {
			resp.getWriter().write(JSONObject.toJSONString(ret));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addStudent(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// TODO Auto-generated method stub
		String name = req.getParameter("name");
		String password = req.getParameter("password");
		String sex = req.getParameter("sex");
		resp.setCharacterEncoding("utf-8");
		if (StringUtil.isEmpty(name)) {
			resp.getWriter().write("��������Ϊ�գ�");
			return;
		}
		if (StringUtil.isEmpty(password)) {
			resp.getWriter().write("���벻��Ϊ�գ�");
			return;
		}
		if (StringUtil.isEmpty(sex)) {
			resp.getWriter().write("�Ա���Ϊ�գ�");
			return;
		}
		
		Student student = new Student();
		student.setName(name);
		student.setPassword(password);
		student.setSex(sex);
		student.setSn(StringUtil.generateSn("S", ""));
		
		StudentDao studentDao = new StudentDao();
		String msg = "���ʧ��";
		if (studentDao.add(student)) {
			msg = "success";
		}
		studentDao.closeConnection();
		resp.getWriter().write(msg);
	}

}

package com.ischoolbar.programmer.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.ischoolbar.programmer.bean.Operator;
import com.ischoolbar.programmer.bean.Page;
import com.ischoolbar.programmer.bean.SearchProperty;
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
		if ("EditStudent".equals(method)) {
			updateStudent(req, resp);
		}
		if ("DeleteStudent".equals(method)) {
			deleteStudent(req, resp);
		}
	}
	
	/**
	 * 删除方法实现
	 * @param req
	 * @param resp
	 */
	private void deleteStudent(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		String[] ids = req.getParameterValues("ids[]");
		StudentDao studentDao = new StudentDao();
		String msg = "";
		if (studentDao.delete(ids)) {
			msg = "success";
		}
		try {
			resp.getWriter().write(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 修改学生信息
	 * @param req
	 * @param resp
	 */
	private void updateStudent(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		int id = StringUtil.isEmpty(req.getParameter("id")) ? 0 : Integer.parseInt(req.getParameter("id"));
		String sn = req.getParameter("sn");
		String name = req.getParameter("name");
		String password = req.getParameter("password");
		String sex = req.getParameter("sex");
		
		Student student = new Student();
		student.setId(id);
		student.setName(name);
		student.setPassword(password);
		student.setSex(sex);
		student.setSn(sn);
		
		StudentDao studentDao = new StudentDao();
		String msg = "";
		if (studentDao.update(student)) {
			msg = "success";
		}
		try {
			studentDao.closeConnection();
			resp.getWriter().write(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getStudengtList(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub
		Map<String, Object> ret = new HashMap<String, Object>();
		
		int pageNumber = Integer.parseInt(req.getParameter("page"));
		int pageSize = Integer.parseInt(req.getParameter("rows"));
		String name = req.getParameter("name") != null ? req.getParameter("name") : "";
		
		Student student = new Student();
		student.setName(name);
		
		StudentDao studentDao = new StudentDao();
		Page<Student> page = new Page<Student>(pageNumber, pageSize);
		page.getSearchOperties().add(new SearchProperty("name", "%" + name + "%", Operator.LIKE));
		
		Page<Student> findList = studentDao.findList(page);
		ret.put("rows", findList.getContent());
		ret.put("total", findList.getTotal());
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

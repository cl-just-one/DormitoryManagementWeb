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
import com.ischoolbar.programmer.dao.AdminDao;
import com.ischoolbar.programmer.dao.AdminDao;
import com.ischoolbar.programmer.entity.Admin;
import com.ischoolbar.programmer.entity.Student;
import com.ischoolbar.programmer.util.StringUtil;

/**
 * Servlet implementation class AdminServlet
 */
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String method = req.getParameter("method");
		if ("toAdminListView".equals(method)) {
			req.getRequestDispatcher("view/AdminList.jsp").forward(req, resp);
		}
		if ("AddAdmin".equals(method)) {
			addAdmin(req, resp);
		}
		if ("AdminList".equals(method)) {
			getAdminList(req, resp);
		}
		if ("EditAdmin".equals(method)) {
			EditAdmin(req, resp);
		}
		if ("DeleteAdmin".equals(method)) {
			DeleteAdmin(req, resp);
		}
	}

	/**
	 * 删除管理员
	 * @param req
	 * @param resp
	 */
	private void DeleteAdmin(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		String[] ids = req.getParameterValues("ids[]");
		AdminDao adminDao = new AdminDao();
		String msg = "";
		
		if (adminDao.delete(ids)) {
			msg = "success";
		}
		
		adminDao.closeConnection();
		try {
			resp.getWriter().write(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 编辑管理员
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void EditAdmin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// TODO Auto-generated method stub
		int id = Integer.parseInt(req.getParameter("id"));
		String name = req.getParameter("name");
		String password = req.getParameter("password");
		int status = Integer.parseInt(req.getParameter("status"));
		
		resp.setCharacterEncoding("utf-8");
		if (StringUtil.isEmpty(req.getParameter("id"))) {
			resp.getWriter().write("请选择管理员！");
			return;
		}
		if (StringUtil.isEmpty(name)) {
			resp.getWriter().write("姓名不能为空！");
			return;
		}
		if (StringUtil.isEmpty(password)) {
			resp.getWriter().write("密码不能为空！");
			return;
		}
		if (StringUtil.isEmpty(req.getParameter("status"))) {
			resp.getWriter().write("状态不能为空！");
			return;
		}
		
		Admin admin = new Admin();
		admin.setId(id);
		admin.setName(name);
		admin.setPassword(password);
		admin.setStatus(status);
		
		AdminDao adminDao = new AdminDao();
		String msg = "更新失败";
		if (adminDao.update(admin)) {
			msg = "success";
		}
		adminDao.closeConnection();
		resp.getWriter().write(msg);
	}

	/**
	 * 添加管理员
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void addAdmin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// TODO Auto-generated method stub
		String name = req.getParameter("name");
		String password = req.getParameter("password");
		int status = Integer.parseInt(req.getParameter("status"));
		
		resp.setCharacterEncoding("utf-8");
		if (StringUtil.isEmpty(name)) {
			resp.getWriter().write("姓名不能为空！");
			return;
		}
		if (StringUtil.isEmpty(password)) {
			resp.getWriter().write("密码不能为空！");
			return;
		}
		if (StringUtil.isEmpty(req.getParameter("status"))) {
			resp.getWriter().write("状态不能为空！");
			return;
		}
		
		Admin admin = new Admin();
		admin.setName(name);
		admin.setPassword(password);
		admin.setStatus(status);
		
		AdminDao adminDao = new AdminDao();
		String msg = "添加失败";
		if (adminDao.add(admin)) {
			msg = "success";
		}
		adminDao.closeConnection();
		resp.getWriter().write(msg);
	}

	/**
	 * 获取管理员列表
	 * */
	private void getAdminList(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		Map<String, Object> ret = new HashMap<String, Object>();
		int pageNumber = Integer.parseInt(req.getParameter("page"));
		int pageSize = Integer.parseInt(req.getParameter("rows"));
		String name = req.getParameter("name") != null ? req.getParameter("name") : "";
		
		AdminDao adminDao = new AdminDao();
		Page<Admin> page = new Page<Admin>(pageNumber, pageSize);
		if (!"".equals(name)) {
			page.getSearchOperties().add(new SearchProperty("name", "%" + name + "%", Operator.LIKE));
		}

		Page<Admin> findList = adminDao.findList(page);
		ret.put("rows", findList.getContent());
		ret.put("total", findList.getTotal());
		
		adminDao.closeConnection();
		try {
			resp.setCharacterEncoding("utf-8");
			resp.getWriter().write(JSONObject.toJSONString(ret));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

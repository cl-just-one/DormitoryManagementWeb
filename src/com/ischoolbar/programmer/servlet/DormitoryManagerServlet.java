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
import com.ischoolbar.programmer.dao.DormitoryManagerDao;
import com.ischoolbar.programmer.entity.DormitoryManager;
import com.ischoolbar.programmer.util.StringUtil;

public class DormitoryManagerServlet extends HttpServlet {

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
		if ("toDormitoryManagerListView".equals(method)) {
			req.getRequestDispatcher("view/DormitoryManagerList.jsp").forward(req, resp);
		}
		if ("AddDormitoryManager".equals(method)) {
			addDormitoryManeger(req, resp);
		}
		if ("DormitoryManagerList".equals(method)) {
			getDormitoryManegerList(req, resp);
		}
		if ("EditDormitoryManager".equals(method)) {
			updateDormitoryManeger(req, resp);
		}
		if ("DeleteDormitoryManager".equals(method)) {
			deleteDormitoryManeger(req, resp);
		}
	}

	/**
	 * 删除宿管
	 * @param req
	 * @param resp
	 */
	private void deleteDormitoryManeger(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub
		String msg = "";
		String[] ids = req.getParameterValues("ids[]");
		
		DormitoryManagerDao dormitoryManagerDao = new DormitoryManagerDao();
		
		if (dormitoryManagerDao.delete(ids)) {
			msg = "success";
		}
		dormitoryManagerDao.closeConnection();
		try {
			resp.getWriter().write(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 更新宿管
	 * @param req
	 * @param resp
	 */
	private void updateDormitoryManeger(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub
		int id = StringUtil.isEmpty(req.getParameter("id")) ? 0 : Integer.parseInt(req.getParameter("id"));
		String sn = req.getParameter("sn");
		String name = req.getParameter("name");
		String password = req.getParameter("password");
		String sex = req.getParameter("sex");
		
		DormitoryManager dormitoryManager = new DormitoryManager();
		dormitoryManager.setId(id);
		dormitoryManager.setName(name);
		dormitoryManager.setPassword(password);
		dormitoryManager.setSex(sex);
		dormitoryManager.setSn(sn);
		
		DormitoryManagerDao dormitoryManagerDao = new DormitoryManagerDao();
		String msg = "";
		if (dormitoryManagerDao.update(dormitoryManager)) {
			msg = "success";
		}
		
		dormitoryManagerDao.closeConnection();
		try {
			resp.getWriter().write(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取宿管列表
	 * @param req
	 * @param resp
	 */
	private void getDormitoryManegerList(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub
		Map<String, Object> ret = new HashMap<String, Object>();
		
		// 查询所有的宿管列表
		String from = req.getParameter("from");
		if ("combox".equals(from)) {
			returnByCombox(req, resp);
			return;
		}
		
		int pageNumber = Integer.parseInt(req.getParameter("page"));
		int pageSize = Integer.parseInt(req.getParameter("rows"));
		String name = req.getParameter("name") != null ? req.getParameter("name") : "";
		
		DormitoryManager dormitoryManager = new DormitoryManager();
		dormitoryManager.setName(name);
		
		DormitoryManagerDao dormitoryManagerDao = new DormitoryManagerDao();
		Page<DormitoryManager> page = new Page<DormitoryManager>(pageNumber, pageSize);
		if (!"".equals(name)) {
			page.getSearchOperties().add(new SearchProperty("name", "%" + name + "%", Operator.LIKE));
		}
		
		// 判断当前用户是否是宿管
		int type = Integer.parseInt(req.getSession().getAttribute("userType").toString());
		if (type == 3) {
			// 如果是宿管只能查看自己的信息
			DormitoryManager loginDormitoryManager = (DormitoryManager) req.getSession().getAttribute("user");
			page.getSearchOperties().add(new SearchProperty("id", loginDormitoryManager.getId(), Operator.EQ));
		}
		
		Page<DormitoryManager> findList = dormitoryManagerDao.findList(page);
		ret.put("rows", findList.getContent());
		ret.put("total", findList.getTotal());
		
		dormitoryManagerDao.closeConnection();
		
		resp.setCharacterEncoding("utf-8");
		try {
			resp.getWriter().write(JSONObject.toJSONString(ret));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 查询所有宿管列表
	 * @param req
	 * @param resp
	 */
	private void returnByCombox(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		DormitoryManagerDao dormitoryManagerDao = new DormitoryManagerDao();
		Page<DormitoryManager> page = new Page<DormitoryManager>(1, 9999);
		
		Page<DormitoryManager> findList = dormitoryManagerDao.findList(page);
		dormitoryManagerDao.closeConnection();
		
		resp.setCharacterEncoding("utf-8");
		try {
			resp.getWriter().write(JSONObject.toJSONString(findList.getContent()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 添加宿管
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void addDormitoryManeger(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
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
		
		DormitoryManager dormitoryManager = new DormitoryManager();
		dormitoryManager.setName(name);
		dormitoryManager.setPassword(password);
		dormitoryManager.setSex(sex);
		dormitoryManager.setSn(StringUtil.generateSn("DM", ""));
		
		DormitoryManagerDao dormitoryManagerDao = new DormitoryManagerDao();
		String msg = "添加失败";
		if (dormitoryManagerDao.add(dormitoryManager)) {
			msg = "success";
		}
		dormitoryManagerDao.closeConnection();
		resp.getWriter().write(msg);
	}
	
}

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
import com.ischoolbar.programmer.dao.DormitoryDao;
import com.ischoolbar.programmer.dao.DormitoryDao;
import com.ischoolbar.programmer.entity.Building;
import com.ischoolbar.programmer.entity.Dormitory;
import com.ischoolbar.programmer.entity.DormitoryManager;
import com.ischoolbar.programmer.util.StringUtil;

/**
 * Servlet implementation class DormitoryServlet
 */
public class DormitoryServlet extends HttpServlet {
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
		if ("toDormitoryListView".equals(method)) {
			req.getRequestDispatcher("view/DormitoryList.jsp").forward(req, resp);
		}
		if ("AddDormitory".equals(method)) {
			addDormitory(req, resp);
		}
		if ("DormitoryList".equals(method)) {
			getDormitoryList(req, resp);
		}
		if ("EditDormitory".equals(method)) {
			updateDormitory(req, resp);
		}
		if ("DeleteDormitory".equals(method)) {
			deleteDormitory(req, resp);
		}
	}
	
	/**
	 * 删除宿舍信息
	 * */
	private void deleteDormitory(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub
		String msg = "";
		String[] ids = req.getParameterValues("ids[]");
		
		DormitoryDao dormitoryDao = new DormitoryDao();
		
		if (dormitoryDao.delete(ids)) {
			msg = "success";
		}
		dormitoryDao.closeConnection();
		try {
			resp.getWriter().write(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 更新宿舍信息
	 * */
	private void updateDormitory(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub
		int id = Integer.parseInt(req.getParameter("id"));
		String sn = req.getParameter("sn");
		String floor = req.getParameter("floor");
		int buildingId = Integer.parseInt(req.getParameter("buildingId"));
		int maxNumber = Integer.parseInt(req.getParameter("maxNumber"));
		
		Dormitory dormitory = new Dormitory();
		dormitory.setId(id);
		dormitory.setSn(sn);
		dormitory.setFloor(floor);
		dormitory.setBuildingId(buildingId);
		dormitory.setMaxNumber(maxNumber);
		
		DormitoryDao dormitoryDao = new DormitoryDao();
		String msg = "更新失败";
		if (dormitoryDao.update(dormitory)) {
			msg = "success";
		}
		
		dormitoryDao.closeConnection();
		try {
			resp.getWriter().write(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取宿舍列表
	 * */
	private void getDormitoryList(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub
		Map<String, Object> ret = new HashMap<String, Object>();
		
		// 查询所有的宿舍列表
		String from = req.getParameter("from");
		if ("combox".equals(from)) {
			returnByCombox(req, resp);
			return;
		}
		
		int pageNumber = Integer.parseInt(req.getParameter("page"));
		int pageSize = Integer.parseInt(req.getParameter("rows"));
		
		DormitoryDao dormitoryDao = new DormitoryDao();
		Page<Dormitory> page = new Page<Dormitory>(pageNumber, pageSize);
		
		String buildingId = req.getParameter("buildingId");
		if(!StringUtil.isEmpty(buildingId)) {
			page.getSearchOperties().add(new SearchProperty("building_id", buildingId, Operator.EQ));
		}
		
		// 判断当前用户是否是宿管
		int type = Integer.parseInt(req.getSession().getAttribute("userType").toString());
		if (type == 3) {
			// 如果是宿管只能查看自己所管宿舍
			DormitoryManager loginDormitory = (DormitoryManager) req.getSession().getAttribute("user");
			DormitoryDao dormitoryPageDao = new DormitoryDao();
			Page<Dormitory> dormitoryPage = new Page<Dormitory>(1, 10);
			dormitoryPage.getSearchOperties().add(new SearchProperty("dormitory_id", loginDormitory.getId(), Operator.EQ));
			dormitoryPage = dormitoryPageDao.findList(dormitoryPage);
			dormitoryPageDao.closeConnection();
			
			page.getSearchOperties().add(new SearchProperty("dormitory_id", dormitoryPage.getContent().get(0).getId(), Operator.EQ));
		}
		
		Page<Dormitory> findList = dormitoryDao.findList(page);
		ret.put("rows", findList.getContent());
		ret.put("total", findList.getTotal());
		
		dormitoryDao.closeConnection();
		
		resp.setCharacterEncoding("utf-8");
		try {
			resp.getWriter().write(JSONObject.toJSONString(ret));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取所有的宿舍列表
	 * @param req
	 * @param resp
	 */
	private void returnByCombox(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		DormitoryDao dormitoryDao = new DormitoryDao();
		Page<Dormitory> page = new Page<Dormitory>(1, 9999);
		
		Page<Dormitory> findList = dormitoryDao.findList(page);
		dormitoryDao.closeConnection();
		
		resp.setCharacterEncoding("utf-8");
		try {
			resp.getWriter().write(JSONObject.toJSONString(findList.getContent()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 添加宿舍
	 * @throws IOException 
	 * */
	private void addDormitory(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// TODO Auto-generated method stub
		String floor = req.getParameter("floor");
		String sn = req.getParameter("sn");
		int buildingId = 0;
		int maxNumber = 0;
		
		resp.setCharacterEncoding("utf-8");
		try {
			buildingId = Integer.parseInt(req.getParameter("buildingId"));
			maxNumber = Integer.parseInt(req.getParameter("maxNumber"));
		} catch (Exception e) {
			// TODO: handle exception
			resp.getWriter().write("楼宇不能为空！");
			return;
		}
		
		if (StringUtil.isEmpty(floor)) {
			resp.getWriter().write("楼层不能为空！");
			return;
		}
		
		Dormitory dormitory = new Dormitory();
		dormitory.setFloor(floor);
		dormitory.setBuildingId(buildingId);
		dormitory.setMaxNumber(maxNumber);
		dormitory.setSn(sn);
		
		DormitoryDao dormitoryDao = new DormitoryDao();
		String msg = "添加失败";
		if (dormitoryDao.add(dormitory)) {
			msg = "success";
		}
		dormitoryDao.closeConnection();
		resp.getWriter().write(msg);
	}

}

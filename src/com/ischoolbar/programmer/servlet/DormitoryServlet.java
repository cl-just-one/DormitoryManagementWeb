package com.ischoolbar.programmer.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ischoolbar.programmer.dao.DormitoryDao;
import com.ischoolbar.programmer.entity.Dormitory;
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
//			updateDormitory(req, resp);
		}
		if ("DeleteDormitory".equals(method)) {
//			deleteDormitory(req, resp);
		}
	}
	/**
	 * 获取宿舍列表
	 * */
	private void getDormitoryList(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 添加宿舍
	 * @throws IOException 
	 * */
	private void addDormitory(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// TODO Auto-generated method stub
		String floor = req.getParameter("floor");
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
		dormitory.setSn(StringUtil.generateSn("D", ""));
		
		DormitoryDao dormitoryDao = new DormitoryDao();
		String msg = "添加失败";
		if (dormitoryDao.add(dormitory)) {
			msg = "success";
		}
		dormitoryDao.closeConnection();
		resp.getWriter().write(msg);
	}

}

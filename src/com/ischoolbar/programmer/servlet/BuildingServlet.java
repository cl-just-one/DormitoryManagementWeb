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
import com.ischoolbar.programmer.dao.BuildingDao;
import com.ischoolbar.programmer.dao.BuildingDao;
import com.ischoolbar.programmer.dao.DormitoryManagerDao;
import com.ischoolbar.programmer.entity.Building;
import com.ischoolbar.programmer.entity.DormitoryManager;
import com.ischoolbar.programmer.util.StringUtil;

public class BuildingServlet extends HttpServlet {

	/**
	 * 序列化ID
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
		if ("toBuildingListView".equals(method)) {
			req.getRequestDispatcher("view/BuildingList.jsp").forward(req, resp);
		}
		if ("AddBuilding".equals(method)) {
			addBuilding(req, resp);
		}
		if ("BuildingList".equals(method)) {
			getBuildingtList(req, resp);
		}
		if ("EditBuilding".equals(method)) {
			updateBuilding(req, resp);
		}
		if ("DeleteBuilding".equals(method)) {
			deleteBuilding(req, resp);
		}
	}

	/**
	 * 删除楼宇
	 * @param req
	 * @param resp
	 */
	private void deleteBuilding(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		String msg = "";
		String[] ids = req.getParameterValues("ids[]");
		
		BuildingDao buildingDao = new BuildingDao();
		
		if (buildingDao.delete(ids)) {
			msg = "success";
		}
		buildingDao.closeConnection();
		try {
			resp.getWriter().write(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 更新楼宇
	 * @param req
	 * @param resp
	 */
	private void updateBuilding(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		int id = StringUtil.isEmpty(req.getParameter("id")) ? 0 : Integer.parseInt(req.getParameter("id"));
		String name = req.getParameter("name");
		String location = req.getParameter("location");
		int dormitoryManagerId = Integer.parseInt(req.getParameter("dormitoryManagerId"));
		
		Building building = new Building();
		building.setId(id);
		building.setName(name);
		building.setLocation(location);
		building.setDormitoryManagerId(dormitoryManagerId);
		
		BuildingDao buildingDao = new BuildingDao();
		String msg = "";
		if (buildingDao.update(building)) {
			msg = "success";
		}
		
		buildingDao.closeConnection();
		try {
			resp.getWriter().write(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取楼宇信息
	 * @param req
	 * @param resp
	 */
	private void getBuildingtList(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub
		Map<String, Object> ret = new HashMap<String, Object>();
		
		int pageNumber = Integer.parseInt(req.getParameter("page"));
		int pageSize = Integer.parseInt(req.getParameter("rows"));
		String name = req.getParameter("name") != null ? req.getParameter("name") : "";
		
		Building building = new Building();
		building.setName(name);
		
		BuildingDao buildingDao = new BuildingDao();
		Page<Building> page = new Page<Building>(pageNumber, pageSize);
		if (!"".equals(name)) {
			page.getSearchOperties().add(new SearchProperty("name", "%" + name + "%", Operator.LIKE));
		}
		
		String dormitoryManagerId = req.getParameter("dormitoryManagerId");
		if(!StringUtil.isEmpty(dormitoryManagerId)) {
			page.getSearchOperties().add(new SearchProperty("dormitory_manager_id", dormitoryManagerId, Operator.EQ));
		}
		
		// 判断当前用户是否是宿管
		int type = Integer.parseInt(req.getSession().getAttribute("userType").toString());
		if (type == 3) {
			// 如果是宿管只能查看自己的信息
			Building loginBuilding = (Building) req.getSession().getAttribute("user");
			page.getSearchOperties().add(new SearchProperty("id", loginBuilding.getId(), Operator.EQ));
		}
		
		Page<Building> findList = buildingDao.findList(page);
		ret.put("rows", findList.getContent());
		ret.put("total", findList.getTotal());
		
		buildingDao.closeConnection();
		
		resp.setCharacterEncoding("utf-8");
		try {
			resp.getWriter().write(JSONObject.toJSONString(ret));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 增加楼宇
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void addBuilding(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// TODO Auto-generated method stub
		String name = req.getParameter("name");
		String location = req.getParameter("location");
		
		int dormitoryManagerId = 0;
		resp.setCharacterEncoding("utf-8");
		try {
			dormitoryManagerId = Integer.parseInt(req.getParameter("dormitoryManagerId"));
		} catch (Exception e) {
			// TODO: handle exception
			resp.getWriter().write("所属宿管不能为空！");
		}
		
		if (StringUtil.isEmpty(name)) {
			resp.getWriter().write("名称不能为空！");
			return;
		}
		if (StringUtil.isEmpty(location)) {
			resp.getWriter().write("位置不能为空！");
			return;
		}
		
		Building building = new Building();
		building.setName(name);
		building.setLocation(location);
		building.setDormitoryManagerId(dormitoryManagerId);
		
		BuildingDao buildingDao = new BuildingDao();
		String msg = "添加失败";
		if (buildingDao.add(building)) {
			msg = "success";
		}
		buildingDao.closeConnection();
		resp.getWriter().write(msg);
	}

}

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
	 * ���л�ID
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
	 * ɾ��¥��
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
	 * ����¥��
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
	 * ��ȡ¥����Ϣ
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
		
		// �жϵ�ǰ�û��Ƿ����޹�
		int type = Integer.parseInt(req.getSession().getAttribute("userType").toString());
		if (type == 3) {
			// ������޹�ֻ�ܲ鿴�Լ�����Ϣ
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
	 * ����¥��
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
			resp.getWriter().write("�����޹ܲ���Ϊ�գ�");
		}
		
		if (StringUtil.isEmpty(name)) {
			resp.getWriter().write("���Ʋ���Ϊ�գ�");
			return;
		}
		if (StringUtil.isEmpty(location)) {
			resp.getWriter().write("λ�ò���Ϊ�գ�");
			return;
		}
		
		Building building = new Building();
		building.setName(name);
		building.setLocation(location);
		building.setDormitoryManagerId(dormitoryManagerId);
		
		BuildingDao buildingDao = new BuildingDao();
		String msg = "���ʧ��";
		if (buildingDao.add(building)) {
			msg = "success";
		}
		buildingDao.closeConnection();
		resp.getWriter().write(msg);
	}

}

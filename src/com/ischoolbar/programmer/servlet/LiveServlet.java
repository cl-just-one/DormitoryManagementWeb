package com.ischoolbar.programmer.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.ischoolbar.programmer.dao.DormitoryDao;
import com.ischoolbar.programmer.dao.LiveDao;
import com.ischoolbar.programmer.entity.Building;
import com.ischoolbar.programmer.entity.Dormitory;
import com.ischoolbar.programmer.entity.DormitoryManager;
import com.ischoolbar.programmer.entity.Live;
import com.ischoolbar.programmer.util.StringUtil;

/**
 * Servlet implementation class LiveServlet
 */
public class LiveServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LiveServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String method = req.getParameter("method");
		
		if ("toLiveListView".equals(method)) {
			req.getRequestDispatcher("view/LiveList.jsp").forward(req, resp);
		} else if("AddLive".equals(method)) {
			AddLive(req, resp);
		} else if("LiveList".equals(method)) {
			getLiveList(req, resp);
		} else if("EditLive".equals(method)) {
			EditLive(req, resp);
		}
	}
	
	/**
	 * 更新住宿信息
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void EditLive(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// TODO Auto-generated method stub
		int studentId = 0;
		int dormitoryId = 0;
		int oldDormitoryId = 0;
		int id = 0;

		resp.setCharacterEncoding("utf-8");
		try {
			studentId = Integer.parseInt(req.getParameter("studentId"));
			dormitoryId = Integer.parseInt(req.getParameter("dormitoryId"));
			oldDormitoryId = Integer.parseInt(req.getParameter("oldDormitoryId"));
			id = Integer.parseInt(req.getParameter("id"));
		} catch (Exception e) {
			// TODO: handle exception
			resp.getWriter().write("学生或楼宇不能为空！");
			return;
		}
		
		String msg = "success";
		DormitoryDao dormitoryDao = new DormitoryDao();
		if(dormitoryDao.isFull(dormitoryId)){
			msg = "该宿舍已经住满，请更换宿舍！";
			resp.getWriter().write(msg);
			return;
		}
		
		Live live = new Live();
		live.setId(id);
		live.setStudentId(studentId);
		live.setDormitoryId(dormitoryId);
		live.setLiveDate(new Date(System.currentTimeMillis()));
		
		LiveDao liveDao = new LiveDao();
		if (!liveDao.update(live)) {
			msg = "调整失败！";
		}
		liveDao.closeConnection();
		if (!dormitoryDao.updateLivedNumber(dormitoryId, 1)) {
			msg = "更新宿舍信息失败！";
			resp.getWriter().write(msg);
			return;
		}
		if (!dormitoryDao.updateLivedNumber(oldDormitoryId, -1)) {
			msg = "更新宿舍信息失败！";
			resp.getWriter().write(msg);
			return;
		}
		resp.getWriter().write(msg);
	}

	/**
	 * 获取宿舍列表
	 * @param req
	 * @param resp
	 */
	private void getLiveList(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		Map<String, Object> ret = new HashMap<String, Object>();
		
		int pageNumber = Integer.parseInt(req.getParameter("page"));
		int pageSize = Integer.parseInt(req.getParameter("rows"));
		String studentId = StringUtil.isEmpty(req.getParameter("studentId")) ? "" : req.getParameter("studentId");
		String dormitoryId = StringUtil.isEmpty(req.getParameter("dormitoryId")) ? "" : req.getParameter("dormitoryId");
		
		LiveDao liveDao = new LiveDao();
		Page<Live> page = new Page<Live>(pageNumber, pageSize);
		if (!"".equals(studentId)) {
			page.getSearchOperties().add(new SearchProperty("student_id", studentId, Operator.EQ));
		}
		if (!"".equals(dormitoryId)) {
			page.getSearchOperties().add(new SearchProperty("dormitory_id", dormitoryId, Operator.EQ));
		}
		
		// 判断当前用户是否是宿管
		int userType = Integer.parseInt(req.getSession().getAttribute("userType").toString());
		if (userType == 3) {
			// 如果是宿管 只能查看自己所管楼宇的住宿信息
			DormitoryManager loginDormitoryManager = (DormitoryManager) req.getSession().getAttribute("user");
			
			BuildingDao buildingDao = new BuildingDao();
			Page<Building> buildingPage = new Page<Building>(1, 10);
			buildingPage.getSearchOperties().add(new SearchProperty("dormitory_manager_id", loginDormitoryManager.getId(), Operator.EQ));
			Page<Building> buildingList = buildingDao.findList(buildingPage);
			buildingDao.closeConnection();
			
			DormitoryDao dormitoryDao = new DormitoryDao();
			Page<Dormitory> dormitoryPage = new Page<Dormitory>(1, 10);
			dormitoryPage.getSearchOperties().add(new SearchProperty("building_id", buildingList.getContent().get(0).getId(), Operator.EQ));
			List<Dormitory> dormitoryList = dormitoryDao.findList(dormitoryPage).getContent();
			
			String dormitoryIds = "";
			for(Dormitory dormitory: dormitoryList) {
				dormitoryIds += dormitory.getId() + ",";
			}
			dormitoryIds = dormitoryIds.substring(0, dormitoryIds.length() - 1);
			page.getSearchOperties().add(new SearchProperty("dormitory_id", dormitoryIds, Operator.IN));
		}
		
		Page<Live> findList = liveDao.findList(page);
		ret.put("rows", findList.getContent());
		ret.put("total", findList.getTotal());
		
		liveDao.closeConnection();
		resp.setCharacterEncoding("utf-8");
		try {
			resp.getWriter().write(JSONObject.toJSONString(ret));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * 添加住宿
	 * @throws IOException 
	 * */
	private void AddLive(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// TODO Auto-generated method stub
		int studentId = 0;
		int dormitoryId = 0;

		resp.setCharacterEncoding("utf-8");
		try {
			studentId = Integer.parseInt(req.getParameter("studentId"));
			dormitoryId = Integer.parseInt(req.getParameter("dormitoryId"));
		} catch (Exception e) {
			// TODO: handle exception
			resp.getWriter().write("学生或楼宇不能为空！");
			return;
		}
		
		Live live = new Live();
		live.setStudentId(studentId);
		live.setDormitoryId(dormitoryId);
		live.setLiveDate(new Date(System.currentTimeMillis()));
		
		LiveDao liveDao = new LiveDao();
		if (liveDao.isLived(studentId)) {
			resp.getWriter().write("该学生已入住，请勿重复添加入住信息！");
			return;
		}
		DormitoryDao dormitoryDao = new DormitoryDao();
		if (dormitoryDao.isFull(dormitoryId)) {
			resp.getWriter().write("该宿舍已入住满！");
			return;
		}
		String msg = "添加失败";
		if (liveDao.add(live)) {
			msg = "success";
		}
		liveDao.closeConnection();
		if (!dormitoryDao.updateLivedNumber(dormitoryId, 1)) {
			msg = "更新宿舍信息失败！";
		}
		resp.getWriter().write(msg);
	}

}

package com.ischoolbar.programmer.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ischoolbar.programmer.dao.LiveDao;
import com.ischoolbar.programmer.entity.Live;

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
		}
		
		Live live = new Live();
		live.setStudentId(studentId);
		live.setDormitoryId(dormitoryId);
		live.setLiveDate(new Date(System.currentTimeMillis()));
		
		LiveDao liveDao = new LiveDao();
		String msg = "添加失败";
		if (liveDao.add(live)) {
			msg = "success";
		}
		liveDao.closeConnection();
		resp.getWriter().write(msg);
	}

}

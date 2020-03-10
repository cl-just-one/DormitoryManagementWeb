package com.ischoolbar.programmer.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		if ("AddStudent".equals(method)) {
//			addStudent(req, resp);
		}
		if ("StudentList".equals(method)) {
//			getStudengtList(req, resp);
		}
		if ("EditStudent".equals(method)) {
//			updateStudent(req, resp);
		}
		if ("DeleteStudent".equals(method)) {
//			deleteStudent(req, resp);
		}
	}

}

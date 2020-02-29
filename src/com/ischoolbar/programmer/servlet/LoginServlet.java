package com.ischoolbar.programmer.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ischoolbar.programmer.bean.Operator;
import com.ischoolbar.programmer.bean.Page;
import com.ischoolbar.programmer.bean.SearchProperty;
import com.ischoolbar.programmer.dao.AdminDao;
import com.ischoolbar.programmer.dao.DormitoryManagerDao;
import com.ischoolbar.programmer.dao.StudentDao;
import com.ischoolbar.programmer.entity.Admin;
import com.ischoolbar.programmer.entity.DormitoryManager;
import com.ischoolbar.programmer.entity.Student;
import com.ischoolbar.programmer.util.StringUtil;

/**
 * ��¼ 
 */
public class LoginServlet extends HttpServlet {
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
		String name = req.getParameter("account");
		String password = req.getParameter("password");
		String vcode = req.getParameter("vcode");
		String msg = "success";
		if(StringUtil.isEmpty(name)) {
			msg = "�û�������Ϊ�գ�";
		}
		if(StringUtil.isEmpty(password)) {
			msg = "���벻��Ϊ�գ�";
		}
		if(StringUtil.isEmpty(vcode)) {
			msg = "��֤�벻��Ϊ�գ�";
		}
		if("success".equals(msg)) {
			Object LoginCpacha = req.getSession().getAttribute("LoginCpacha");
			if (LoginCpacha == null) {
				msg = "session�ѹ��ڣ���ˢ�����ԣ�";
			} else {
				if (!vcode.toUpperCase().equals(LoginCpacha.toString().toUpperCase())) {
					msg = "��֤�����";
				}
			}
		}
		if("success".equals(msg)) {
			String typeStr = req.getParameter("type");
			try {
				int type = Integer.parseInt(typeStr);
				if (type == 1) {
					// ��������Ա
					AdminDao adminDao = new AdminDao();
					Admin admin = adminDao.getAdmin(name);
					adminDao.closeConnection();
					if (admin == null) {
						msg = "�û������ڣ�";
					} else {
						if (!password.equals(admin.getPassword())) {
							msg = "�������";
						} else {
							if (admin.getStatus() == Admin.SYSTEM_STATUS_DISABLE) {
								msg = "���û�״̬�����ã�����ϵ����Ա��";
							} else {
								req.getSession().setAttribute("user", admin);
								req.getSession().setAttribute("userType", type);
							}
						}
					}
				} else if (type == 2) {
					// ѧ��
					StudentDao studentDao = new StudentDao();
					Page<Student> page = new Page<Student>(1, 10);
					page.getSearchOperties().add(new SearchProperty("name", name, Operator.EQ));
					Page<Student> studentPage = studentDao.findList(page);
					studentDao.closeConnection();
					
					if (studentPage.getContent().size() == 0) {
						msg = "�û������ڣ�";
					} else {
						Student student = studentPage.getContent().get(0);
						if (!password.equals(student.getPassword())) {
							msg = "�������";
						} else {
							req.getSession().setAttribute("user", student);
							req.getSession().setAttribute("userType", type);
						}
					}
				} else if (type == 3) {
					// �޹�
					DormitoryManagerDao dormitoryManagerDao = new DormitoryManagerDao();
					Page<DormitoryManager> page = new Page<DormitoryManager>(1, 10);
					page.getSearchOperties().add(new SearchProperty("name", name, Operator.EQ));
					Page<DormitoryManager> dormitoryManagerPage = dormitoryManagerDao.findList(page);
					dormitoryManagerDao.closeConnection();
					
					if (dormitoryManagerPage.getContent().size() == 0) {
						msg = "�û�������";
					} else {
						DormitoryManager dormitoryManager = dormitoryManagerPage.getContent().get(0);
						if (!password.equals(dormitoryManager.getPassword())) {
							msg = "�������";
						} else {
							req.getSession().setAttribute("user", dormitoryManager);
							req.getSession().setAttribute("userType", type);
						}
					}
				} else {
					
				}
			} catch (Exception e) {
				System.out.println(e);
				// TODO: handle exception
				msg = "�û����ʹ���";
			}
		}
		resp.setCharacterEncoding("utf-8");
		resp.getWriter().write(msg);
	}
}

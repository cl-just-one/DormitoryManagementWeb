package com.ischoolbar.programmer.dao;

import com.ischoolbar.programmer.entity.Student;

/**
 * 学生实体数据库操作
 * */
public class StudentDao extends BaseDao<Student> {
	
//	public boolean add(Student student) {
//		String sql = "insert into db_student(id, sn, name, password, sex) values(null, ?, ? , ?, ?)";
//		try {
//			PreparedStatement prepareStatement = con.prepareStatement(sql);
//			prepareStatement.setString(1, student.getSn());
//			prepareStatement.setString(2, student.getName());
//			prepareStatement.setString(3, student.getPassword());
//			prepareStatement.setString(4, student.getSex());
//			return prepareStatement.executeUpdate() > 0;
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return false;
//	}
	
//	public List<Student> findList(Student student, Page page) {
//		List<Student> ret = new ArrayList<Student>();
//		
//		String sql = "select * from db_student ";
//		System.out.println("--"+student.getName());
//		if (!StringUtil.isEmpty(student.getName())) {
//			sql += "where name like '%" + student.getName() + "%' ";
//		}
//		sql += "limit " + page.getOffset() + "," + page.getPageSize();
//		
//		PreparedStatement prepareStatement;
//		try {
//			prepareStatement = con.prepareStatement(sql);
//			ResultSet executeQuery = prepareStatement.executeQuery();
//			while (executeQuery.next()) {
//				Student st = new Student();
//				
//				st.setId(executeQuery.getInt("id"));
//				st.setName(executeQuery.getString("name"));
//				st.setPassword(executeQuery.getString("password"));
//				st.setSex(executeQuery.getString("sex"));
//				st.setSn(executeQuery.getString("sn"));
//				
//				ret.add(st);
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return ret;
//	}
//	
//	public int getTotal(Student student) {
//		String sql = "select count(id) as total from db_student ";
//		if (!StringUtil.isEmpty(student.getName())) {
//			sql += "name like '%" + student.getName() + "%' ";
//		}
//		PreparedStatement prepareStatement;
//		try {
//			prepareStatement = con.prepareStatement(sql);
//			ResultSet executeQuery = prepareStatement.executeQuery();
//			if (executeQuery.next()) {
//				return executeQuery.getInt("total");
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return 0;
//	}
}
 
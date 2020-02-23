package com.ischoolbar.programmer.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}

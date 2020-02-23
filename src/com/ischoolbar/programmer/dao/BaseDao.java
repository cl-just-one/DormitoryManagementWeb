package com.ischoolbar.programmer.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.ischoolbar.programmer.util.DbUtil;

/**
 *  数据库操作基本类
 *  利用泛型和反射机制来抽象数据库基本的增删改查的操作
 * */
public class BaseDao<T> {
	public Connection con = new DbUtil().getConnection();
	public void closeConnection() {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

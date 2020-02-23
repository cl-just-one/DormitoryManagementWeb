package com.ischoolbar.programmer.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

import com.ischoolbar.programmer.util.DbUtil;

/**
 *  数据库操作基本类
 *  利用泛型和反射机制来抽象数据库基本的增删改查的操作
 * */
public class BaseDao<T> {
	public final static int CRUD_ADD = 1;
	
	public final static int CRUD_DELETE = 2;
	
	public final static int CRUD_UPDATE = 3;
	
	public final static int CRUD_SELECT = 4;
	
	public Connection con = new DbUtil().getConnection();
	
	public boolean add(T t) {
		if (t == null) {
			return false;
		}
		String buildSql = buildSql(CRUD_ADD, t);
		System.out.println(buildSql);
		try {
			PreparedStatement prepareStatement = con.prepareStatement(buildSql);
			Field[] declaredFields = t.getClass().getDeclaredFields();
			for (int i = 1; i < declaredFields.length; i++) {
				declaredFields[i].setAccessible(true);
				prepareStatement.setObject(i, declaredFields[i].get(t));
			}
			return prepareStatement.executeUpdate() > 0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private String buildSql(int type, T t) {
		// TODO Auto-generated method stub
		String sql = "";
		switch (type) {
			case CRUD_ADD: {
				String sql1 = "insert into db_" + t.getClass().getSimpleName().toLowerCase() + "(";
				Field[] declaredFields = t.getClass().getDeclaredFields();
				for (Field field : declaredFields) {
					sql1 += field.getName() + ",";
				}
				sql1 = sql1.substring(0, sql1.length() - 1) + ")";
				String sql2 = " values(null, ";
				String[] params = new String[declaredFields.length - 1];
				Arrays.fill(params, "?");
				sql2 += StringUtils.join(params, ",") + ")";
				sql = sql1 + sql2;
				break;
			}
			default:
				break;
		}
		return sql;
	}

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

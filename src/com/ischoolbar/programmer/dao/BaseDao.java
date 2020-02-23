package com.ischoolbar.programmer.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.ischoolbar.programmer.util.DbUtil;

/**
 *  ���ݿ����������
 *  ���÷��ͺͷ���������������ݿ��������ɾ�Ĳ�Ĳ���
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

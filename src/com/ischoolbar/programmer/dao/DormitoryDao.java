package com.ischoolbar.programmer.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ischoolbar.programmer.entity.Dormitory;
/**
 * �������ݿ����
 * */
public class DormitoryDao extends BaseDao<Dormitory> {
	/**
	 * �ж������Ƿ�ס��
	 * @param dormitoryId
	 * @return
	 */
	public boolean isFull(int dormitoryId) {
		String sql = "select lived_number,max_number from db_dormitory id = " + dormitoryId;
		try {
			PreparedStatement prepareStatement = con.prepareStatement(sql);
			ResultSet executeQuery = prepareStatement.executeQuery();
			return executeQuery.getInt("lived_number") >= executeQuery.getInt("max_number");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}

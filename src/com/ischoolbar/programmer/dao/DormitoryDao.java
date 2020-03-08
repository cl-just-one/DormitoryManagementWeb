package com.ischoolbar.programmer.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ischoolbar.programmer.entity.Dormitory;
/**
 * 宿舍数据库操作
 * */
public class DormitoryDao extends BaseDao<Dormitory> {
	/**
	 * 判断宿舍是否住满
	 * @param dormitoryId
	 * @return
	 */
	public boolean isFull(int dormitoryId) {
		String sql = "select lived_number,max_number from db_dormitory where id = " + dormitoryId;
		try {
			PreparedStatement prepareStatement = con.prepareStatement(sql);
			ResultSet executeQuery = prepareStatement.executeQuery();
			if (executeQuery.next()) {
				return executeQuery.getInt("lived_number") >= executeQuery.getInt("max_number");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 更新入住的数量
	 * @return
	 */
	public boolean updateLivedNumber(int dormitoryId, int number) {
		String sql = " update db_dormitory set lived_number = lived_number + " + number + " where id = " + dormitoryId;
		try {
			PreparedStatement prepareStatement = con.prepareStatement(sql);
			int executeUpdate = prepareStatement.executeUpdate();
			return executeUpdate > 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}

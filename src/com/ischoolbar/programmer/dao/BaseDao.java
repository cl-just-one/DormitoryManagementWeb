package com.ischoolbar.programmer.dao;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.ischoolbar.programmer.bean.Page;
import com.ischoolbar.programmer.bean.SearchProperty;
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
	
	public final static int CRUD_TOTAL = 5;
	
	public Connection con = new DbUtil().getConnection();
	
	private Class<T> t;
	
	@SuppressWarnings("unchecked")
	public BaseDao() {
		Type genericSuperclass = getClass().getGenericSuperclass();
		if (genericSuperclass instanceof ParameterizedType) {
			Type[] actualTypeArguments = ((ParameterizedType)genericSuperclass).getActualTypeArguments();
			if (actualTypeArguments.length > 0) {
				t = (Class<T>) actualTypeArguments[0];
			}
		}
	}
	
	public boolean add(T t) {
		if (t == null) {
			return false;
		}
		String buildSql = buildSql(CRUD_ADD);
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
	
	public Page<T> findList(Page<T> page) {
		String sql = buildSql(CRUD_SELECT);
		sql += buildSearchSql(page);
		sql += " limit " + page.getOffset() + "," + page.getPageSize();
		try {
			PreparedStatement prepareStatement = con.prepareStatement(sql);
			prepareStatement = setParams(page, prepareStatement);
			ResultSet executeQuery = prepareStatement.executeQuery();
			List<T> content = page.getContent();
			while (executeQuery.next()) {
				T entity = t.newInstance();
				Field[] declaredFields = t.getDeclaredFields();
				for(Field field: declaredFields) {
					field.setAccessible(true);
					field.set(entity, executeQuery.getObject(field.getName()));
				}
				content.add(entity);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		page.setTotal(getTotal(page));
		return page;
	}
	
	public int getTotal(Page<T> page) {
		String sql = buildSql(CRUD_TOTAL);
		sql += buildSearchSql(page);
		try {
			PreparedStatement prepareStatement = con.prepareStatement(sql);
			prepareStatement = setParams(page, prepareStatement);
			ResultSet executeQuery = prepareStatement.executeQuery();
			if (executeQuery.next()) {
				return executeQuery.getInt("total");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	private PreparedStatement setParams(Page<T> page, PreparedStatement prepareStatement) {
		List<SearchProperty> searchOperties = page.getSearchOperties();
		int index = 1;
		for(SearchProperty searchProperty: searchOperties) {
			try {
				prepareStatement.setObject(index++, searchProperty.getValue());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return prepareStatement;
	}
	
	private String buildSearchSql(Page<T> page) {
		String sql = "";
		List<SearchProperty> searchOperties = page.getSearchOperties();
		for(SearchProperty searchProperty: searchOperties) {
			switch(searchProperty.getOperator()) {
				case GT: {
					sql += " and " + searchProperty.getKey() + " > ?";
				}
				case GTE: {
					sql += " and " + searchProperty.getKey() + " >= ?";
				}
				case EQ: {
					sql += " and " + searchProperty.getKey() + " = ?";
				}
				case LT: {
					sql += " and " + searchProperty.getKey() + " < ?";
				}
				case LTE: {
					sql += " and " + searchProperty.getKey() + " <= ?";
				}
				case LIKE: {
					sql += " and " + searchProperty.getKey() + " like ?";
				}
				case NEQ: {
					sql += " and " + searchProperty.getKey() + " <> ?";
				}
			}
		}
		return sql.replace("and", "where");
	}

	private String buildSql(int type) {
		// TODO Auto-generated method stub
		String sql = "";
		switch (type) {
			case CRUD_ADD: {
				String sql1 = "insert into db_" + t.getSimpleName().toLowerCase() + "(";
				Field[] declaredFields = t.getDeclaredFields();
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
			case CRUD_SELECT: {
				sql = "select * from db_" + t.getSimpleName().toLowerCase();
				break;
			}
			case CRUD_TOTAL: {
				sql = "select count(*) as total from db_" + t.getSimpleName().toLowerCase();
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

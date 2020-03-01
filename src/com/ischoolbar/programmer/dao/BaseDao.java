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
import com.ischoolbar.programmer.util.StringUtil;

/**
 *  数据库操作基本类
 *  利用泛型和反射机制来抽象数据库基本的增删改查的操作
 * */
/**
 * @author Administrator
 *
 * @param <T>
 */
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
	
	/**
	 * 增加实体操作
	 * @param t
	 * @return
	 */
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
	
	/**
	 * 更新实体操作
	 * @param t
	 * @return
	 */
	public boolean update(T t) {
		String buildSql = buildSql(CRUD_UPDATE);
		try {
			PreparedStatement prepareStatement = con.prepareStatement(buildSql);
			Field[] declaredFields = this.t.getDeclaredFields();
			for (int i = 1; i < declaredFields.length; i++) {
				Field field = declaredFields[i];
				field.setAccessible(true);
				prepareStatement.setObject(i, field.get(t));
			}
			declaredFields[0].setAccessible(true);
			prepareStatement.setObject(declaredFields.length, declaredFields[0].get(t));
			
			return prepareStatement.executeUpdate() > 0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 删除实体操作
	 * @param ids
	 * @return
	 */
	public boolean delete(String[] ids) {
		String sql = buildSql(CRUD_DELETE);
		sql += StringUtils.join(ids, ",") + ")";
		try {
			PreparedStatement prepareStatement = con.prepareStatement(sql);
			return prepareStatement.executeUpdate() > 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public Page<T> findList(Page<T> page) {
		String sql = buildSql(CRUD_SELECT);
		sql += buildSearchSql(page);
		sql += " limit " + page.getOffset() + "," + page.getPageSize();
		System.out.println(sql);
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
					field.set(entity, executeQuery.getObject(StringUtil.convertToUnderLine(field.getName())));
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
	
	/**
	 * 获取总条数
	 * @param page
	 * @return
	 */
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
	
	/**
	 * 构建搜索sql
	 * @param page
	 * @return
	 */
	private String buildSearchSql(Page<T> page) {
		String sql = "";
		List<SearchProperty> searchOperties = page.getSearchOperties();
		for(SearchProperty searchProperty: searchOperties) {
			switch(searchProperty.getOperator()) {
				case GT: {
					sql += " and " + StringUtil.convertToUnderLine(searchProperty.getKey()) + " > ?";
					break;
				}
				case GTE: {
					sql += " and " + StringUtil.convertToUnderLine(searchProperty.getKey()) + " >= ?";
					break;
				}
				case EQ: {
					sql += " and " + StringUtil.convertToUnderLine(searchProperty.getKey()) + " = ?";
					break;
				}
				case LT: {
					sql += " and " + StringUtil.convertToUnderLine(searchProperty.getKey()) + " < ?";
					break;
				}
				case LTE: {
					sql += " and " + StringUtil.convertToUnderLine(searchProperty.getKey()) + " <= ?";
					break;
				}
				case LIKE: {
					sql += " and " + StringUtil.convertToUnderLine(searchProperty.getKey()) + " like ?";
					break;
				}
				case NEQ: {
					sql += " and " + StringUtil.convertToUnderLine(searchProperty.getKey()) + " <> ?";
					break;
				}
			}
		}
		return sql.replaceFirst("and", "where");
	}

	
	/**
	 * 构建一般sql
	 * @param type
	 * @return
	 */
	private String buildSql(int type) {
		// TODO Auto-generated method stub
		String sql = "";
		switch (type) {
			case CRUD_ADD: {
				String sql1 = "insert into db_" + StringUtil.convertToUnderLine(t.getSimpleName()) + "(";
				Field[] declaredFields = t.getDeclaredFields();
				for (Field field : declaredFields) {
					sql1 += StringUtil.convertToUnderLine(field.getName()) + ",";
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
				System.out.println(t.getSimpleName());
				sql = "select * from db_" + StringUtil.convertToUnderLine(t.getSimpleName());
				break;
			}
			case CRUD_TOTAL: {
				sql = "select count(*) as total from db_" + StringUtil.convertToUnderLine(t.getSimpleName());
				break;
			}
			case CRUD_UPDATE: {
				sql = "update db_" + StringUtil.convertToUnderLine(t.getSimpleName()) + " set ";
				Field[] declaredFields = t.getDeclaredFields();
				for (Field field : declaredFields) {
					if(!"id".equals(field.getName())) {
						sql += StringUtil.convertToUnderLine(field.getName()) + "=?,";
					}
				}
				sql = sql.substring(0, sql.length() - 1) + " where id = ?";
				System.out.println(sql);
				break;
			}
			case CRUD_DELETE: {
				sql = "delete from db_" + StringUtil.convertToUnderLine(t.getSimpleName()) + " where id in(";
				break;
			}
			default:
				break;
		}
		return sql;
	}

	/**
	 * 关闭连接
	 */
	public void closeConnection() {
		if (con != null) {
			try {
				con.close();
				System.out.println(t.getSimpleName() + "Dao数据库连接已关闭！");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

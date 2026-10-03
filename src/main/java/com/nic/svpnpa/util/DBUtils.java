package com.nic.svpnpa.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nic.svpnpa.service.SVPNPAService;

@Service
public class DBUtils {

	private static final Logger logger = LogManager.getLogger(DBUtils.class);
	
	@Autowired
	public DataSource dataSource;


	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();

	}

	public int insertOrUpdate(String sql, Object[] list) {
		int recordCount = 0;
		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			if (list != null) {
				for (int i = 0; i < list.length; i++) {
					statement.setObject(i + 1, list[i]);
				}
			}
//			System.out.println("sql===" + statement);
			recordCount=statement.executeUpdate();
		} catch (SQLException e) {
			logger.error("Exception in DBUtils::insertOrUpdate::" + e.getMessage());
	        logger.error("Exception in DBUtils::insertOrUpdate::" + e.getCause());
	        logger.error("Exception in DBUtils::insertOrUpdate::" + e.getClass() + "::" + e.getLocalizedMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return recordCount;
	}

	public Object getDBValue(String sql, Object[] columnvalues) {
		Object val = new Object();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		try {
			connection = getConnection();
			ps = connection.prepareStatement(sql);
			if (columnvalues != null) {
				for (int ci = 0; ci < columnvalues.length; ci++) {
					ps.setObject(ci + 1, columnvalues[ci]);
				}
			}
//			System.out.println("sql===" + ps);
			rs = ps.executeQuery();
			if (rs.next()) {
				val = rs.getObject(1);
			}

		} catch (Exception e) {
			logger.error("Exception in DBUtils::getDBValue::" + e.getMessage());
	        logger.error("Exception in DBUtils::getDBValue::" + e.getCause());
	        logger.error("Exception in DBUtils::getDBValue::" + e.getClass() + "::" + e.getLocalizedMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return val;
	}

	public ArrayList<HashMap<String, Object>> getListMap(String sql, Object[] columnvalues) {
		
		ArrayList<HashMap<String, Object>> records = new ArrayList<HashMap<String, Object>>();
		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement ps = connection.prepareStatement(sql);
			if (columnvalues != null) {
				for (int ci = 0; ci < columnvalues.length; ci++) {
					ps.setObject(ci + 1, columnvalues[ci]);
				}
			}
//			System.out.println("sql===" + ps);
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rm = rs.getMetaData();
			int cols = rm.getColumnCount();
			if (rs.next()) {
				do {
					HashMap<String, Object> row = new java.util.HashMap<String, Object>(cols);
					for (int i = 1; i <= cols; i++) {
						String columnName = rm.getColumnName(i);
						Object columnValue = rs.getObject(i);
						row.put(columnName.trim(), columnValue);
					}
					records.add(row);
					row = null;
				} while (rs.next());
			}
		} catch (Exception e) {
			logger.error("Exception in DBUtils::getListMap::" + e.getMessage());
	        logger.error("Exception in DBUtils::getListMap::" + e.getCause());
	        logger.error("Exception in DBUtils::getListMap::" + e.getClass() + "::" + e.getLocalizedMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return records;
	}

	public Map<String, Object> getMap(String sql, Object[] columnvalues) {

		Connection connection = null;
		Map<String, Object> records = new HashMap<String, Object>();
		try {
			connection = getConnection();
			PreparedStatement ps = connection.prepareStatement(sql);
			if (columnvalues != null) {
				for (int ci = 0; ci < columnvalues.length; ci++) {
					ps.setObject(ci + 1, columnvalues[ci]);
				}
			}
//			System.out.println("sql===" + ps);
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rm = rs.getMetaData();
			int cols = rm.getColumnCount();
			if (rs.next()) {
				records = new HashMap<String, Object>(cols);
				for (int i = 1; i <= cols; i++) {
					String columnName = rm.getColumnName(i);
					Object columnValue = rs.getObject(i);
					records.put(columnName.trim(), columnValue);
				}
			}
		} catch (Exception e) {
			logger.error("Exception in DBUtils::getMap::" + e.getMessage());
	        logger.error("Exception in DBUtils::getMap::" + e.getCause());
	        logger.error("Exception in DBUtils::getMap::" + e.getClass() + "::" + e.getLocalizedMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return records;
	}

	public List<List<Object>> get2DList(String sql, Object[] columnvalues) {
		List<List<Object>> records = new ArrayList<List<Object>>();
		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement ps = connection.prepareStatement(sql);
			if (columnvalues != null) {
				for (int ci = 0; ci < columnvalues.length; ci++) {
					ps.setObject(ci + 1, columnvalues[ci]);
				}
			}
//			System.out.println("sql===" + ps);
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rm = rs.getMetaData();
			int cols = rm.getColumnCount();
			if (rs.next()) {
				do {
					ArrayList<Object> row = new ArrayList<Object>(cols);
					for (int i = 1; i <= cols; i++) {
						row.add(rs.getObject(i));
					}
					records.add(row);
					row = null;
				} while (rs.next());
			}
		} catch (Exception e) {
			logger.error("Exception in DBUtils::get2DList::" + e.getMessage());
	        logger.error("Exception in DBUtils::get2DList::" + e.getCause());
	        logger.error("Exception in DBUtils::get2DList::" + e.getClass() + "::" + e.getLocalizedMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return records;
	}

	public List<Object> getList(String sql, Object[] columnvalues) {
		List<Object> records = new ArrayList<Object>();
		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement ps = connection.prepareStatement(sql);
			if (columnvalues != null) {
				for (int ci = 0; ci < columnvalues.length; ci++) {
					ps.setObject(ci + 1, columnvalues[ci]);
				}
			}
//			System.out.println("sql===" + ps);
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rm = rs.getMetaData();
			int cols = rm.getColumnCount();
			records = new ArrayList<Object>();
			while (rs.next()) {
				records.add(rs.getObject(1));
			}
		} catch (Exception e) {
			logger.error("Exception in DBUtils::getList::" + e.getMessage());
	        logger.error("Exception in DBUtils::getList::" + e.getCause());
	        logger.error("Exception in DBUtils::getList::" + e.getClass() + "::" + e.getLocalizedMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return records;
	}

}

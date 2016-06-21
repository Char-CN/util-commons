package org.blazer.common.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBUtil {

	public static void main(String[] args) throws SQLException {
		DBUtil dbUtil = new DBUtil("jdbc:mysql://127.0.0.1:3306/DW_MetaData", "root", "root");

		List<String> sqls = new ArrayList<String>();
		sqls.add("insert into test(name) values('new test')");
		sqls.add("insert into test(name) values('new test1')");
		sqls.add("insert into test(name) values('new test2')");
		sqls.add("insert into test(name) values('new test3)");
		sqls.add("insert into test(name) values('new test4')");
		dbUtil.batchUpdateIgnoreError(sqls);

		List<HashMap<Object, Object>> list = dbUtil.select("select * from DW_MetaData.test");
		System.out.println(list);
		System.out.println(list.get(0).get(0));
		System.out.println(list.size());
	}

	private Connection conn = null;
	private PreparedStatement ps = null;
	private String url = null;
	private String userName = null;
	private String password = null;

	public DBUtil(String url, String userName, String password) {
		this.url = url;
		this.userName = userName;
		this.password = password;
	}

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void spawnConnection() throws SQLException {
		this.conn = DriverManager.getConnection(this.url, this.userName, this.password);
	}

	public void spawnPreparedStatement(String sql) throws SQLException {
		this.ps = this.conn.prepareStatement(sql);
	}

	public HashMap<Object, Object> selectUnique(String sql, Object... objs) throws SQLException {
		List<HashMap<Object, Object>> list = select(sql, objs);
		return list.size() == 0 ? new HashMap<Object, Object>() : list.get(0);
	}

	public HashMap<Object, Object> selectUniqueNoIndex(String sql, Object... objs) throws SQLException {
		List<HashMap<Object, Object>> list = selectNoIndex(sql, objs);
		return list.size() == 0 ? new HashMap<Object, Object>() : list.get(0);
	}

	public List<HashMap<Object, Object>> select(String sql, Object... objs) throws SQLException {
		List<HashMap<Object, Object>> list = new ArrayList<HashMap<Object, Object>>();
		spawnConnection();
		spawnPreparedStatement(sql);
		bindParams(objs);
		ResultSet rs = this.ps.executeQuery();
		ResultSetMetaData rsmd = rs.getMetaData();
		String[] columnNames = new String[rsmd.getColumnCount()];
		for (int i = 0; i < columnNames.length; i++) {
			columnNames[i] = rsmd.getColumnLabel(i + 1);
		}
		while (rs.next()) {
			HashMap<Object, Object> columnMap = new HashMap<Object, Object>();
			for (int i = 0; i < columnNames.length; i++) {
				columnMap.put(columnNames[i], rs.getObject(columnNames[i]));
				columnMap.put(i, rs.getObject(i + 1));
			}
			list.add(columnMap);
		}
		close();
		return list;
	}

	// public List<String> execute(String sql) throws SQLException {
	// List<String> list = new ArrayList<String>();
	// spawnConnection();
	// spawnPreparedStatement(sql);
	// ResultSet rs = this.ps.executeQuery();
	// while (rs.next()) {
	// rs.get
	// }
	// close();
	// return null;
	// }

	public List<HashMap<Object, Object>> selectNoIndex(String sql, Object... objs) throws SQLException {
		List<HashMap<Object, Object>> list = new ArrayList<HashMap<Object, Object>>();
		spawnConnection();
		spawnPreparedStatement(sql);
		bindParams(objs);
		ResultSet rs = this.ps.executeQuery();
		ResultSetMetaData rsmd = rs.getMetaData();
		String[] columnNames = new String[rsmd.getColumnCount()];
		for (int i = 0; i < columnNames.length; i++) {
			columnNames[i] = rsmd.getColumnLabel(i + 1);
		}
		while (rs.next()) {
			HashMap<Object, Object> columnMap = new HashMap<Object, Object>();
			for (int i = 0; i < columnNames.length; i++) {
				columnMap.put(columnNames[i], rs.getObject(columnNames[i]));
			}
			list.add(columnMap);
		}
		close();
		return list;
	}

	public void batchUpdateIgnoreError(List<String> sqls) throws SQLException {
		if (sqls == null || sqls.size() == 0) {
			return;
		}
		spawnConnection();
		this.conn.setAutoCommit(false);
		spawnPreparedStatement(sqls.get(0));
		try {
			this.ps.executeUpdate(sqls.get(0));
		} catch (SQLException e) {
		}
		for (int i = 1; i < sqls.size(); i++) {
			try {
				this.ps.executeUpdate(sqls.get(i));
			} catch (SQLException e) {
			}
		}
		this.conn.commit();
		close();
	}

	public void batchUpdate(List<String> sqls) throws SQLException {
		int errorRow = 0;
		String errorSql = null;
		try {
			if (sqls == null || sqls.size() == 0) {
				return;
			}
			spawnConnection();
			this.conn.setAutoCommit(false);
			errorSql = sqls.get(0);
			spawnPreparedStatement(sqls.get(0));
			this.ps.executeUpdate(sqls.get(0));
			for (int i = 1; i < sqls.size(); i++) {
				errorRow = i;
				errorSql = sqls.get(i);
				this.ps.executeUpdate(sqls.get(i));
			}
			this.conn.commit();
			close();
		} catch (SQLException e) {
			throw new SQLException("error row : " + errorRow + ", error sql : " + errorSql, e);
		}
	}

	public int insert(String sql, Object... objs) throws SQLException {
		return update(sql, objs);
	}

	public int delete(String sql, Object... objs) throws SQLException {
		return update(sql, objs);
	}

	public int update(String sql, Object... objs) throws SQLException {
		int num = 0;
		try {
			spawnConnection();
			this.conn.setAutoCommit(false);
			spawnPreparedStatement(sql);
			bindParams(objs);
			num = this.ps.executeUpdate();
			this.conn.commit();
			close();
		} catch (SQLException e) {
			throw new SQLException(StringUtil.union("error row : 0, error sql : ", sql, " parameter : ") + StringUtil.union(objs), e);
		}
		return num;
	}

	public int updateNoSpawnConn(String sql, Object... objs) throws SQLException {
		int num = 0;
		try {
			bindParams(objs);
			num = this.ps.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(StringUtil.union("error row : 0, error sql : ", sql, " parameter : ") + StringUtil.union(objs), e);
		}
		return num;
	}

	public Connection getConn() {
		return this.conn;
	}

	public void execute(String sql, Object... objs) throws SQLException {
		try {
			spawnConnection();
			this.conn.setAutoCommit(false);
			spawnPreparedStatement(sql);
			bindParams(objs);
			this.ps.execute();
			this.conn.commit();
			close();
		} catch (SQLException e) {
			throw new SQLException(StringUtil.union("error row : 0, error sql : ", sql, " parameter : ") + StringUtil.union(objs), e);
		}
	}

	public void updateIgnoreError(String sql, Object... objs) {
		try {
			spawnConnection();
			this.conn.setAutoCommit(false);
			spawnPreparedStatement(sql);
			bindParams(objs);
			this.ps.execute();
			this.conn.commit();
			close();
		} catch (SQLException e) {
			System.out.println("error sql : " + sql);
			e.printStackTrace();
		}
	}

	public synchronized void synUpdate(String sql) throws SQLException {
		try {
			spawnConnection();
			this.conn.setAutoCommit(false);
			spawnPreparedStatement(sql);
			this.ps.execute();
			this.conn.commit();
			close();
		} catch (SQLException e) {
			throw new SQLException("error row : " + 0 + ", error sql : " + sql, e);
		}
	}

	public void close() {
		try {
			if (this.conn != null) {
				this.conn.close();
			}
		} catch (SQLException e) {
			this.conn = null;
		}
		try {
			if (this.ps != null) {
				this.ps.close();
			}
		} catch (SQLException e) {
			this.conn = null;
		}
	}

	public void bindParams(Object... objs) throws SQLException {
		for (int i = 0; i < objs.length; i++) {
			if (objs[i] == null) {
				this.ps.setNull(i, Types.NULL);
			} else if (objs[i] instanceof java.util.Date) {
				// extends
			} else if (objs[i] instanceof byte[]) {
				this.ps.setBytes(i + 1, (byte[]) objs[i]);
			} else {
				this.ps.setObject(i + 1, objs[i]);
			}

		}
	}

	public boolean testConn() {
		try {
			List<HashMap<Object, Object>> list = this.select("select 1 from dual;");
			return list != null && list.size() == 1 && list.get(0).get(0).toString().equals("1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}

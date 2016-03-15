package Util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
 
public class DB {
 
	public Connection conn = null;
 
	public DB() {
		try {
				Class.forName("com.mysql.jdbc.Driver");
				String url ="jdbc:mysql://127.0.0.1:3306/Crawler?useUnicode=yes&characterEncoding=UTF-8";
				conn = DriverManager.getConnection(url, "root", "");//password ya razah
				System.out.println("Conn Built");//for check
		} catch (SQLException e) {
			System.out.println("Connection Error");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
 
	public ResultSet runSql(String sql) throws SQLException {
		Statement sta = conn.createStatement();
		return sta.executeQuery(sql);
	}
 
	public boolean runSql2(String sql) throws SQLException {
		Statement sta = conn.createStatement();
		return sta.execute(sql);
	}
 
	@Override
	protected void finalize() throws Throwable {
		if (conn != null || !conn.isClosed()) {
			conn.close();
		}
	}
}
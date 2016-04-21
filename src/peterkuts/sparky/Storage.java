package peterkuts.sparky;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.sql.*;

import peterkuts.sparky.SimpleGun.Prediction;

class Storage {
	
	Connection connection = null;
	
	Storage(String filepath) {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", filepath));
		} catch (SQLException e) {
			System.err.println(e);
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception ex) {
					System.err.println(ex);
				}
			}
		} catch (ClassNotFoundException e) {
			System.err.println(e);
		}
	}
	
	public boolean initDataBase() {
		if (connection != null) {
			try {
				_initDataBase();
				return true;
			} catch (Exception ex) {
				System.err.println(ex);				
			}
		}
		return false;
	}

	public void addBulletData(String srcTank, String dstTank, List<Prediction> predictions) {
		if (connection != null) {
			try {
				_addBulletData(srcTank, dstTank, predictions);
			} catch (SQLException ex) {
				System.err.println(ex);				
			}
		}
	}
	
	public void closeDataBase() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException ex) {
				System.err.println(ex);				
			}
			connection = null;
		}
	}
	
	void _addBulletData(String srcTank, String dstTank, List<Prediction> predictions) throws SQLException {
		Statement statement = connection.createStatement();
		statement.setQueryTimeout(30);
		ResultSet rs;
		int srcTankId;
		int dstTankId;
		try {
			statement.executeUpdate(uCreateTank(srcTank));
		} catch (SQLException ex) {}
		rs = statement.executeQuery(qTankId(srcTank));
		if (!rs.next()) {
			return;
		}
		srcTankId = rs.getInt("tankId");
		try {
			statement.executeUpdate(uCreateTank(dstTank));
		} catch (SQLException ex) {}
		rs = statement.executeQuery(qTankId(dstTank));
		if (!rs.next()) {
			return;
		}
		dstTankId = rs.getInt("tankId");
		String query = uInsertStamps(srcTankId, dstTankId, predictions);
		statement.executeUpdate(query);
	}
	
	void _initDataBase() throws SQLException {
		Statement statement = connection.createStatement();
		statement.setQueryTimeout(30);
		
		ResultSet rs;
		rs = statement.executeQuery(qCheckTableExists("Tanks"));
		if (rs.next() && rs.getInt("cnt") == 0) {
			statement.executeUpdate(uCreateTable_Tanks());
		}
		rs = statement.executeQuery(qCheckTableExists("BulletData"));
		if (rs.next() && rs.getInt("cnt") == 0) {
			statement.executeUpdate(uCreateTable_BulletData());
		}
	}
	
	static String uInsertStamps(final int srcTank, final int dstTank, List<Prediction> predictions) {
		return "INSERT INTO `BulletData` VALUES " 
				+ predictions.stream().map(new Function<Prediction, String>() {
					public String apply(Prediction t) {
						return String.format("(%d, %d, %f, %d)", srcTank, dstTank, t.bulletTime, t.didHit?1:0);
					}
				}).reduce("", new BinaryOperator<String>() {
					public String apply(String t, String u) {
						return t.isEmpty()?u:t+","+u;
					}
				});
	}

	static String uCreateTank(String name) {
		return String.format("INSERT INTO `Tanks` (`name`) VALUES ('%s')", name);
	}

	static String qTankId(String name) {
		return String.format("SELECT `tankId` FROM `Tanks` WHERE name='%s'", name);
	}

	static String qCheckTableExists(String name) {
		return String.format("SELECT count(*) as cnt FROM sqlite_master WHERE type='table' AND name='%s'", name);
	}
	
	static String uCreateTable_Tanks() {
		return "CREATE TABLE `Tanks` (\n"
				+ "	`tankId` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n"
				+ "	`name`	TEXT NOT NULL UNIQUE\n"
				+ ");\n";
	}

	static String uCreateTable_BulletData() {
		return "CREATE TABLE `BulletData` (\n"
				+ "	`srcTankId`	INTEGER NOT NULL,\n"
				+ "	`dstTankId`	INTEGER NOT NULL,\n"
				+ "	`time`	REAL,\n"
				+ "	`hit`	INTEGER,\n"
				+ "	FOREIGN KEY(`srcTankId`) REFERENCES Tanks ( tankId ),\n"
				+ "	FOREIGN KEY(`dstTankId`) REFERENCES Tanks ( tankId )\n"
				+ ");\n";
	}
}

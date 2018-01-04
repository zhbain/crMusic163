package util;

import constant.CacheConsts;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcConnector {

    private final static String JDBC_DRIVER = CacheConsts.JDBC_DRIVER;
    private final static String DATA_BASE_URL = CacheConsts.DATA_BASE_URL;
    private final static String USER_NAME = CacheConsts.USER_NAME;
    private final static String PASS_WORD = CacheConsts.PASS_WORD;

    private Connection connection = null;
    private Statement statement = null;

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(JDBC_DRIVER);
        this.connection = DriverManager.getConnection(DATA_BASE_URL, USER_NAME, PASS_WORD);

        // enable log
        DriverManager.setLogWriter(new PrintWriter(System.err));
        return this.connection;
    }

    public Statement getStatement() throws SQLException, ClassNotFoundException {
        if (this.connection == null) {
            this.connection = this.getConnection();
        }
        this.statement = this.connection.createStatement();
        this.statement.execute("CREATE TABLE IF NOT EXISTS play_list(\n" +
                        "pk_play_list_id INTEGER PRIMARY KEY AUTO_INCREMENT,\n" +
                        "title VARCHAR(255),\n" +
                        "create_time VARCHAR(255),\n" +
                        "collect INTEGER,\n" +
                        "share INTEGER,\n" +
                        "comment INTEGER,\n" +
                        "tag VARCHAR(255),\n" +
                        "description TEXT,\n" +
                        "play_count INTEGER\n" +
                        ")CHARSET=utf8;");
        this.statement.execute("CREATE TABLE IF NOT EXISTS song(\n" +
                "pk_song_id INTEGER PRIMARY KEY AUTO_INCREMENT,\n" +
                "list_title VARCHAR(255),\n" +
                "song_title VARCHAR(255),\n" +
                "duration VARCHAR(12),\n" +
                "singer VARCHAR(255),\n" +
                "album VARCHAR(255)\n" +
                ")CHARSET=utf8;");
        return this.statement;
    }

    public void close() throws SQLException {
        if (this.statement != null) {
            this.statement.close();
        }

        if (this.connection != null) {
            this.connection.close();
        }
    }

}

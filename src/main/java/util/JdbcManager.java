package util;



import entity.PlayListInfo;
import entity.SongInfo;

import java.sql.SQLException;
import java.sql.Statement;

public class JdbcManager {

    private JdbcConnector jdbcConnector;
    private Statement statement;

    public JdbcManager() throws SQLException, ClassNotFoundException {
        this.jdbcConnector = new JdbcConnector();
        this.statement = jdbcConnector.getStatement();
    }

    public void insertPlayListInfo(PlayListInfo playListInfo) throws SQLException {
        if (this.statement != null) {
            String sql = "INSERT INTO play_list\n" +
                    "(title, create_time, collect, share, comment, tag, description, play_count)\n" +
                    "VALUES\n" +
                    "('" + playListInfo.getTitle() + "','" + playListInfo.getCreateTime() + "','" +
                    playListInfo.getCollect() + "','" + playListInfo.getShare() + "','" +
                    playListInfo.getComment() + "','" + playListInfo.getTag() + "','" +
                    playListInfo.getDescription() + "','" + playListInfo.getPlayCount() + "');";
            System.out.println(sql);
            this.statement.execute(sql);
        } else {
            System.out.println("statement is null");
        }
    }

    public void insertSongInfo(SongInfo songInfo) throws SQLException {
        if (this.statement != null) {
            String sql = "INSERT INTO song\n" +
                    "(list_title, song_title, duration, singer, album)\n" +
                    "VALUES\n" +
                    "('" + songInfo.getListTitle() + "','" + songInfo.getSongTitle() + "','" +
                    songInfo.getDuration() + "','" + songInfo.getSinger() + "','" +
                    songInfo.getAlbum() + "'" + ");";
            System.out.println(sql);
            this.statement.execute(sql);
        } else {
            System.out.println("statement is null");
        }
    }

    public void close() throws SQLException {
        this.jdbcConnector.close();
    }
}

package constant;

public class CacheConsts {

    // mysql database config
    public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String DATA_BASE_URL = "jdbc:mysql://localhost:3306/crmusic163?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC";
    public static final String USER_NAME = "root";
    public static final String PASS_WORD = "123456";

    // redis database config
    public static final String REDIS_HOST = "localhost";
    public static final int REDIS_PORT = 6379;
    public static final String PLAY_LIST_KEY = "play_list_url";

    // URL
    // get proxy url
    public static final String GET_PROXY_URL = "http://127.0.0.1:5010/get";
    // host to be visit
    public static final String HOST = "http://music.163.com";
    public static final String PLAY_LIST = "http://music.163.com/discover/playlist";

    // Multithreading command
    public static final int GET_URL = 0;
    public static final int GET_DATA = 1;

    // Download type
    public static final int useJsoup = 0;
    public static final int useSelenium = 1;



    public static String numberNormalize(String s) {
        if (! s.matches("\\d+")) {
            s = "0";
        }
        return s;
    }

    public static String stringNormalize(String s) {
        return s.trim().replace("\'", "'").replace("'", "\\'").replace("\"", "\\\"" );
    }
}

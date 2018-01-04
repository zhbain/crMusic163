package util;


import constant.CacheConsts;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisManager {

    private final static String host = CacheConsts.REDIS_HOST;
    private final static int port = CacheConsts.REDIS_PORT;
    // POOL need to be closed when application shutdowns
    private static JedisPool POOL = new JedisPool(new JedisPoolConfig(), host, port);

    public static void rpushUrlToList(String listName, String url) {
        try (Jedis jedis = POOL.getResource()){
            jedis.rpush(listName, url);
            System.out.println("insert <" + url +"> into " + listName);
        }
    }

    public  static String rpopUrlFromList(String listName) {
        String url;
        try (Jedis jedis = POOL.getResource()){
            url = jedis.rpop(listName);
            System.out.println("rpop <" + url +"> from " + listName);
        }

        return url;
    }

    public static void close() {
        POOL.close();
        System.out.println("JedisPool closed!");
    }

}

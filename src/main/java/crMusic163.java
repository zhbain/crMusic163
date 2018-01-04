import constant.CacheConsts;
import org.jsoup.nodes.Document;
import parse.PlayListParse;
import util.BloomFilter;
import util.DownLoader;
import util.RedisManager;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

public class crMusic163 {

    public static void main(String[] args) throws InterruptedException {
        System.out.println(new Date() + " >> Boot...");
        // multithreading
        getUrlList();
        // multithreading
        getData();

        //close JedisPool
        RedisManager.close();
        System.out.println(new Date() +" >> All done!");
    }

    /**
     * get url list save into Redis
     * multithreading pool
     */
    private static void getUrlList() throws InterruptedException {
        // multithreading pool
        final int corePoolSize = 32;
        final int maximumPoolSize = 64;
        final long keepAliveTime = 10000L;
        final int queueSize = 1024;
        ExecutorService executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                keepAliveTime, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(queueSize));

        Document playListDoc = DownLoader.getDocByUrl(CacheConsts.PLAY_LIST, CacheConsts.useJsoup);
        List<String> categoryUrlList = PlayListParse.getCategoryUrls(playListDoc);
        // save url into Reids database for crawler
        assert categoryUrlList != null;
        for (String url: categoryUrlList) {
            while (url != null) {
                try {
                    // add worker to pool
                    Runnable worker = new WorkerThread<>(url, CacheConsts.GET_URL);
                    executor.execute(worker);
                    break;
                } catch (RejectedExecutionException e) {
                    e.printStackTrace();
                    Thread.sleep(200);
                }
            }
        }

        // shutdown pool
        executor.shutdown();
        // every 60 seconds check pool
        while (!executor.awaitTermination(60, TimeUnit.SECONDS)){
            System.out.println(new Date() + " >> threads still work in pool");
        }
        System.out.println(new Date() + " >> Finished all threads");

    }

    /**
     * read url from Redis database
     * use BloomFilter to ensure per url just visited one time
     * multithreading pool
     */
    private static void getData() throws InterruptedException {
        // multithreading
        final int corePoolSize = 32;
        final int maximumPoolSize = 32;
        final long keepAliveTime = 0L;
        final int queueSize = 1024;
        ExecutorService executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                keepAliveTime, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(queueSize));

        BloomFilter bloomFilter = new BloomFilter();
        String urlKey = CacheConsts.PLAY_LIST_KEY;
        String url = RedisManager.rpopUrlFromList(urlKey);
        while (url != null) {
            try {
                // if url had not be visited
                if (! bloomFilter.contains(url)) {
                    Runnable woker = new WorkerThread<>(url, CacheConsts.GET_DATA);
                    executor.execute(woker);
                    // add url to bloomFilter
                    bloomFilter.add(url);
                } else {
                    System.out.println("<"  + url + "> has been visited");
                    // get a new url
                    url = RedisManager.rpopUrlFromList(urlKey);
                }
            } catch (RejectedExecutionException e) {
                e.printStackTrace();
                // 15 minutes
                Thread.sleep(900000);

                // clean all processes of chrome.exe and chrome.driver
                // the fail thread will retry in DownLoader.getDocByUrl method
                try {
                    Runtime.getRuntime().exec("taskkill /f /im chrome.exe ");
                    Runtime.getRuntime().exec("taskkill /f /im chromedriver.exe ");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        // shutdown pool
        executor.shutdown();
        // every 60 seconds check pool
        while (! executor.awaitTermination(60, TimeUnit.SECONDS)){
            System.out.println(new Date() + " >> threads still work in pool");
        }
        System.out.println(new Date() + " >> Finished all threads");
    }
}

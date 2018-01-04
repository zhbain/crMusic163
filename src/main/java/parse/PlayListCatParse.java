package parse;


import constant.CacheConsts;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.RedisManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *  Parse documnet of url: https://music.163.com/discover/playlist/?cat=*
 *  and also the next page with the same templet
 */
public class PlayListCatParse {

    /**
     *  Extract urls from document, and save into redis
     * @return true for success, false for fail
     */
    public static boolean saveSongListUrls(Document document) {
        if (document == null) {
            return false;
        }

        final String key = CacheConsts.PLAY_LIST_KEY;
        int time = 5;
        while (time -- > 0) {
            System.out.println("remain " + time + " time to try saveSongListUrls ...");
            try {
                Element ul = document.selectFirst("ul#m-pl-container");
                Elements aTags = ul.select("li > p.dec > a");

                for(Element aTag: aTags) {
                    String url = aTag.attr("href");
                    RedisManager.rpushUrlToList(key, url);
                }
                return true;

            } catch (NullPointerException e) {
                System.out.println("catch NullpointerException...try again...");
            }
        }
        return false;
    }


    /**
     * Match example: <cat=%E6%97%A5%E8%AF%AD> from
     *     <http://music.163.com/discover/playlist/?cat=%E6%97%A5%E8%AF%AD>
     */
    public static String getCatValue(String url) {

        System.out.println("Regex with " + url);
        Matcher m = Pattern.compile("(cat=.+$)").matcher(url);

        if (m.find( )) {
            System.out.println("Found value: " + m.group(0));
            return m.group(0);
        } else {
            System.out.println("NO MATCH");
            return null;
        }

    }

}

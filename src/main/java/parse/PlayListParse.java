package parse;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 *  Parse documnet of url: https://music.163.com/discover/playlist
 */

public class PlayListParse {

    public static List<String> getCategoryUrls(Document document) {
        int time = 5;
        while (time -- > 0) {
            System.out.println("remain " + time + " time to try getCategoryUrls ...");
            try {
                List<String> urlList = new ArrayList<>();
                Elements dls = document.selectFirst("div#cateListBox")
                        .select("div.bd > dl");
                Elements aTags = dls.select("a");
                for(Element aTag: aTags) {
                    String url = aTag.attr("href");
                    urlList.add(url);
                }
                return urlList;

            } catch (NullPointerException e) {
                System.out.println("catch NullpointerException...try again...");
            }
        }

        return null;
    }
}

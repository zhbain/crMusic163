import constant.CacheConsts;
import org.jsoup.nodes.Document;
import parse.PlayListCatParse;
import parse.PlayListIdParse;
import util.DownLoader;

import java.sql.SQLException;

public class WorkerThread<T> implements Runnable{

    private T stuff;
    private int command;

    WorkerThread(T stuff, int command) {
        this.stuff = stuff;
        this.command = command;
    }

    @Override
    public void run() {

        // get all song list url
        if (this.command == CacheConsts.GET_URL) {
            // first page url like <http://music.163.com/discover/playlist/?cat=80%E5%90%8E&order=hot>
            Document firstPage = DownLoader.getDocByUrl(String.valueOf(this.stuff), CacheConsts.useJsoup);
            PlayListCatParse.saveSongListUrls(firstPage);
            // other nest urls like <http://music.163.com/discover/playlist/?order=hot&cat=80%E5%90%8E&limit=35&offset=35>
            // every next page url just <offset> value + 35
            int limit = 35;
            int offset = 35;
            String cat = PlayListCatParse.getCatValue(String.valueOf(this.stuff));
            String prefix = "http://music.163.com/discover/playlist/?order=hot&";
            String url = prefix + cat + "&limit=35&offset=" + offset;
            Document nextPage = DownLoader.getDocByUrl(url, CacheConsts.useJsoup);
            PlayListCatParse.saveSongListUrls(nextPage);
            boolean ahead = true;
            while (ahead) {
                offset +=  limit;
                url = prefix + cat + "&limit=35&offset=" + offset;
                System.out.println("while loop: url <" + url + ">");
                nextPage = DownLoader.getDocByUrl(url, CacheConsts.useJsoup);
                ahead = PlayListCatParse.saveSongListUrls(nextPage);
            }
            // else load url from redis database then extract data into mysql database
        } else if(this.command == CacheConsts.GET_DATA) {
            try {
                Document document;
                document = DownLoader.getDocByUrl(String.valueOf(this.stuff), CacheConsts.useSelenium);
                PlayListIdParse.saveAllSongsInfo(document);
                PlayListIdParse.savePlayListInfo(document);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }

        }

    }
}

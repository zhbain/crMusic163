package parse;

import constant.CacheConsts;
import entity.PlayListInfo;
import entity.SongInfo;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.JdbcManager;

import java.sql.SQLException;


/**
 *  Parse documnet of url: https://music.163.com/playlist?id=*
 */
public class PlayListIdParse {

    public static void savePlayListInfo(Document document) throws SQLException, ClassNotFoundException {
        if (document == null) {
            System.out.println("null document when savePlayListInfo");
            return;
        }

        JdbcManager jdbcManager = new JdbcManager();
        PlayListInfo playListInfo;
        try {
            Element cntcDiv = document.selectFirst("div.cnt > div.cntc");
            Element contentOperationDiv = cntcDiv.selectFirst("div#content-operation");
            String title = cntcDiv.selectFirst("div.hd > div.tit > h2").text();
            title = CacheConsts.stringNormalize(title);
            // createTime yyyy-mm-dd
            String createTime = cntcDiv.selectFirst("div.user > span.time").text().trim().substring(0, 10);
            String collect = contentOperationDiv.select("a[data-res-action=fav]").attr("data-count");
            collect = CacheConsts.numberNormalize(collect);
            String share = contentOperationDiv.select("a[data-res-action=share]").attr("data-count");
            share = CacheConsts.numberNormalize(share);
            String comment = contentOperationDiv.select("a[data-res-action=comment] > i > span#cnt_comment_count").text();
            comment = CacheConsts.numberNormalize(comment);
            String description = cntcDiv.select("p#album-desc-more").text().trim();
            description = CacheConsts.stringNormalize(description);
            String playCount = document.selectFirst("div.n-songtb").selectFirst("div.more > strong#play-count").text();
            playCount = CacheConsts.numberNormalize(playCount);
            // tag more than one
            StringBuilder tag = new StringBuilder();
            Elements tags = cntcDiv.select("div.tags").select("a.u-tag");
            for (Element e: tags) {
                tag.append(e.text()).append("|");
            }
            // del "|" in the end
            String tagString = tag.substring(0, tag.length()-1);

            playListInfo = new PlayListInfo(title, createTime, collect, share, comment,
                    tagString, description, playCount);
        } catch (NullPointerException e) {
            System.out.println("Catch NullPointerException when savePlayListInfo");
            System.out.println(document);
            return;
        }

        jdbcManager.insertPlayListInfo(playListInfo);
        jdbcManager.close();

    }

    public static void saveAllSongsInfo(Document document) throws SQLException, ClassNotFoundException {
        if (document == null) {
            System.out.println("null document when saveAllSongsInfo");
            return;
        }

        JdbcManager jdbcManager = new JdbcManager();
        SongInfo songInfo;
        try {
            Element divSongList = document.selectFirst("div.n-songtb").selectFirst("div#song-list-pre-cache");
            Elements trs = divSongList.select("table.m-table > tbody > tr");
            String listTitle = document.selectFirst("div.cnt > div.cntc").selectFirst("div.hd > div.tit > h2").text();
            listTitle = CacheConsts.stringNormalize(listTitle);
            // one row as a song info
            for(Element tr: trs) {
                Elements tds = tr.select("td");
                String songTitle = tds.get(1).selectFirst("span.txt").selectFirst("b").attr("title");
                songTitle = CacheConsts.stringNormalize(songTitle);
                String duration = tds.get(2).selectFirst("span.u-dur").text();
                String singer = tds.get(3).selectFirst("div.text").attr("title");
                singer = CacheConsts.stringNormalize(singer);
                String album = tds.get(4).selectFirst("div.text > a").attr("title");
                album = CacheConsts.stringNormalize(album);

                songInfo = new SongInfo(listTitle, songTitle, duration, singer, album);
                jdbcManager.insertSongInfo(songInfo);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("Catch NullPointerException when saveAllSongInfo");
            System.out.println(document);
            return;
        }

        jdbcManager.close();
    }

}

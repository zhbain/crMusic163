package entity;

public class SongInfo {

    private String listTitle;
    private String songTitle;
    private String duration;
    private String singer;
    private String album;

    public SongInfo(String listTitle, String songTitle, String duration, String singer, String album) {
        this.listTitle = listTitle;
        this.songTitle = songTitle;
        this.duration = duration;
        this.singer = singer;
        this.album = album;
    }

    public String getListTitle() {
        return listTitle;
    }

    public void setListTitle(String listTitle) {
        this.listTitle = listTitle;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}

package entity;

public class PlayListInfo {

    private String title;
    private String createTime;
    private String collect;
    private String share;
    private String comment;
    private String tag;
    private String description;
    private String playCount;


    public PlayListInfo(String title, String createTime, String collect, String share, String comment, String tag, String description, String playCount) {
        this.title = title;
        this.createTime = createTime;
        this.collect = collect;
        this.share = share;
        this.comment = comment;
        this.tag = tag;
        this.description = description;
        this.playCount = playCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCollect() {
        return collect;
    }

    public void setCollect(String collect) {
        this.collect = collect;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlayCount() {
        return playCount;
    }

    public void setPlayCount(String playCount) {
        this.playCount = playCount;
    }
}

package moyomoyoe.board.model.dto;

import java.util.Date;

public class PostDTO {

    private int postlistId;
    private String title;
    private String context;
    private String nickname;
    private Date postDate;
    private Boolean userOpen;
    private int regionCode;
    private int imageId;
    private int keywordId;
    private int userId;

    public PostDTO() {
    }

    public int getPostlistId() {
        return postlistId;
    }

    public void setPostlistId(int postlistId) {
        this.postlistId = postlistId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public Boolean getUserOpen() {
        return userOpen;
    }

    public void setUserOpen(Boolean userOpen) {
        this.userOpen = userOpen;
    }

    public int getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(int regionCode) {
        this.regionCode = regionCode;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(int keywordId) {
        this.keywordId = keywordId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "PostlistDTO{" +
                "postlistId=" + postlistId +
                ", title='" + title + '\'' +
                ", context='" + context + '\'' +
                ", nickname='" + nickname + '\'' +
                ", postDate=" + postDate +
                ", userOpen=" + userOpen +
                ", regionCode=" + regionCode +
                ", imageId=" + imageId +
                ", keywordId=" + keywordId +
                ", userId=" + userId +
                '}';
    }
}

package moyomoyoe.board.model.dto;

public class PostDTO {

    private int postId;
    private String title;
    private String context;
    private String nickname;
    private String postDate;
    private Boolean userOpen;
    private int regionCode;
    private int imageId;
    private int keywordId;
    private int userId;

    public PostDTO() {
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
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

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {this.postDate = postDate;}

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
        return "PostDTO{" +
                "postId=" + postId +
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

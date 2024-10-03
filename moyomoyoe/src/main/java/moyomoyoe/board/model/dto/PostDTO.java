package moyomoyoe.board.model.dto;

import java.time.LocalDate;

public class PostDTO {

    private int postId;
    private String title;
    private String context;
    private String nickname;
    private LocalDate postDate;
    private Boolean userOpen;
    private int regionCode;
    private int imageId;
    private int keywordId;
    private int userId;

    private String imageName;
    private String profileImage;

    public PostDTO() {
    }

    public PostDTO(int postId, String title, String context, String nickname, LocalDate postDate, Boolean userOpen, int regionCode, int imageId, int keywordId, int userId, String imageName, String profileImage) {
        this.postId = postId;
        this.title = title;
        this.context = context;
        this.nickname = nickname;
        this.postDate = postDate;
        this.userOpen = userOpen;
        this.regionCode = regionCode;
        this.imageId = imageId;
        this.keywordId = keywordId;
        this.userId = userId;
        this.imageName = imageName;
        this.profileImage = profileImage;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public int getPostId() {
        return postId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
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

    public LocalDate getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDate postDate) {this.postDate = postDate;}

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
                ", imageName='" + imageName + '\'' +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }
}

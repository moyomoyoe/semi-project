package moyomoyoe.board.model.dto;

import java.time.LocalDate;

public class CommentDTO {

    private int commentId;
    private int postId;
    private String nickname;
    private String comment;
    private LocalDate commentPostDate;
    private int userId;

    public CommentDTO() {
    }

    public CommentDTO(int postId, String comment) {
        this.postId = postId;
        this.comment = comment;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getCommentPostDate() {
        return commentPostDate;
    }

    public void setCommentPostDate(LocalDate commentPostDate) {
        this.commentPostDate = commentPostDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
                "commentId=" + commentId +
                ", postId=" + postId +
                ", nickname='" + nickname + '\'' +
                ", comment='" + comment + '\'' +
                ", commentPostDate=" + commentPostDate +
                ", userId=" + userId +
                '}';
    }
}

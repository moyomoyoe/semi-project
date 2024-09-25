package moyomoyoe.board.model.dto;

public class ImageDTO {

    private int keywordId;
    private String keywordName;

    public ImageDTO() {
    }

    public int getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(int keywordId) {
        this.keywordId = keywordId;
    }

    public String getKeywordName() {
        return keywordName;
    }

    public void setKeywordName(String keywordName) {
        this.keywordName = keywordName;
    }

    @Override
    public String toString() {
        return "ImageDTO{" +
                "keywordId=" + keywordId +
                ", keywordName='" + keywordName + '\'' +
                '}';
    }
}

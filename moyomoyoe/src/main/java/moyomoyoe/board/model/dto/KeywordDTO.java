package moyomoyoe.board.model.dto;

public class KeywordDTO {

    private int keywordId;
    private String keywordName;

    public KeywordDTO() {
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
        return "KeywordDTO{" +
                "keywordId=" + keywordId +
                ", keywordName='" + keywordName + '\'' +
                '}';
    }
}

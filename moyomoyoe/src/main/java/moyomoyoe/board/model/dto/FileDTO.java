package moyomoyoe.board.model.dto;

public class FileDTO {

    private String originalFileName;
    private String savedName;
    private String filePath;

    public FileDTO() {
    }

    public FileDTO(String originalFileName, String savedName, String filePath) {
        this.originalFileName = originalFileName;
        this.savedName = savedName;
        this.filePath = filePath;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getSavedName() {
        return savedName;
    }

    public void setSavedName(String savedName) {
        this.savedName = savedName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "FileDTO{" +
                "originalFileName='" + originalFileName + '\'' +
                ", savedName='" + savedName + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}

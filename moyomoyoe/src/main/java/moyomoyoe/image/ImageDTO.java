package moyomoyoe.image;

public class ImageDTO {

    private int imageId;
    private String imageName;

    public ImageDTO() {
    }

    public ImageDTO(int imageId, String imageName) {
        this.imageId = imageId;
        this.imageName = imageName;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Override
    public String toString() {
        return "ImageDTO{" +
                "imageId=" + imageId +
                ", image_name='" + imageName + '\'' +
                '}';
    }

}

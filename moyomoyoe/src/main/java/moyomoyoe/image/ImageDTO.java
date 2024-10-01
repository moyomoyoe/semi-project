package moyomoyoe.image;

public class ImageDTO {

    private int imageId;
    private String imageName;

    public ImageDTO() {
    }

    public ImageDTO(int imageId, String image_name) {
        this.imageId = imageId;
        this.imageName = image_name;
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

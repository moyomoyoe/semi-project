package moyomoyoe.reservation.model.dto;

public class StoreDTO {
    private int storeId;
    private String storeName;
    private String storeAddress;
    private String description;
    private String storeSort;
    private String businessNo;
    private int userId;
    private Integer imageId;

    public StoreDTO() {
    }

    public StoreDTO(int storeId, String storeName, String storeAddress, String storeSort, String businessNo, String description, int userId, Integer imageId) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeSort = storeSort;
        this.businessNo = businessNo;
        this.description = description;
        this.userId = userId;
        this.imageId = imageId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getStoreSort() {
        return storeSort;
    }

    public void setStoreSort(String storeSort) {
        this.storeSort = storeSort;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "StoreDTO{" +
                "storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", storeAddress='" + storeAddress + '\'' +
                ", storeSort='" + storeSort + '\'' +
                ", businessNo=" + businessNo +
                ", description='" + description + '\'' +
                ", userId=" + userId +
                ", imageId=" + imageId +
                '}';
    }
}
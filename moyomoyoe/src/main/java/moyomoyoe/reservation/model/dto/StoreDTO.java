package moyomoyoe.reservation.model.dto;

public class StoreDTO {
    private int storeId;
    private String storeName;
    private String storeAddress;
    private String storeSort;
    private String description;
    private int busyNo;
    private String imageIdStore;

    public StoreDTO() {
    }

    public StoreDTO(int storeId, String storeName, String storeAddress, String storeSort, String description, int busyNo, String imageIdStore) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeSort = storeSort;
        this.description = description;
        this.busyNo = busyNo;
        this.imageIdStore = imageIdStore;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBusyNo() {
        return busyNo;
    }

    public void setBusyNo(int busyNo) {
        this.busyNo = busyNo;
    }

    public String getImageIdStore() {
        return imageIdStore;
    }

    public void setImageIdStore(String imageIdStore) {
        this.imageIdStore = imageIdStore;
    }

    @Override
    public String toString() {
        return "StoreDTO{" +
                "storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", storeAddress='" + storeAddress + '\'' +
                ", storeSort='" + storeSort + '\'' +
                ", description='" + description + '\'' +
                ", busyNo=" + busyNo +
                ", imageIdStore='" + imageIdStore + '\'' +
                '}';
    }
}
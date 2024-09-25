package moyomoyoe.reservation.model.dto;

public class ScheduleDTO {
    private int scheduleId;
    private int storeId;
    private String storeName;
    private String resDate;
    private String startTime;  // Time으로 사용하지 않고 String으로 처리
    private String endTime;
    private String capacity;

    public ScheduleDTO() {
    }

    public ScheduleDTO(int scheduleId, int storeId, String storeName, String resDate, String startTime, String endTime, String capacity) {
        this.scheduleId = scheduleId;
        this.storeId = storeId;
        this.storeName = storeName;
        this.resDate = resDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
    }

    public ScheduleDTO(int storeId, String storeName, String date, String startTime, String endTime, String capacity) {
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
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

    public String getResDate() {
        return resDate;
    }

    public void setResDate(String resDate) {
        this.resDate = resDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "ScheduleDTO{" +
                "scheduleId=" + scheduleId +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", resDate='" + resDate + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", capacity='" + capacity + '\'' +
                '}';
    }
}
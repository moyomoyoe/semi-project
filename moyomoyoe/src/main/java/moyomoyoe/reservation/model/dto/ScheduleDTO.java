package moyomoyoe.reservation.model.dto;

import java.sql.Time;

public class ScheduleDTO {
    private int scheduleId;
    private int storeId;
    private Time startTime;  // Time으로 사용하지 않고 String으로 처리
    private Time endTime;
    private int capacity;

    public ScheduleDTO() {
    }

    public ScheduleDTO(int scheduleId, int storeId, Time startTime, Time endTime, int capacity) {
        this.scheduleId = scheduleId;
        this.storeId = storeId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
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

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "ScheduleDTO{" +
                "scheduleId=" + scheduleId +
                ", storeId=" + storeId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", capacity=" + capacity +
                '}';
    }
}
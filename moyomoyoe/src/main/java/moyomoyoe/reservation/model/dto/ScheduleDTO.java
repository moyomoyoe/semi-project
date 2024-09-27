package moyomoyoe.reservation.model.dto;

import java.sql.Time;
import java.util.Objects;

public class ScheduleDTO {
    private int scheduleId;
    private int storeId;
    private Time startTime;
    private Time endTime;
    private int capacity;
    private int bookedPeople ;
    //예약이 가능한 지 확인하기 위한 값

    public ScheduleDTO() {
    }

    public ScheduleDTO(int scheduleId, int storeId, Time startTime, Time endTime, int capacity, int bookedPeople) {
        this.scheduleId = scheduleId;
        this.storeId = storeId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.bookedPeople = bookedPeople;
    }

    public ScheduleDTO(int i, int storeId, Time sqlStartTime, Time sqlEndTime, int capacity) {
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

    public int getBookedPeople() {
        return bookedPeople;
    }

    public void setBookedPeople(int bookedPeople) {
        this.bookedPeople = bookedPeople;
    }

    @Override
    public String toString() {
        return "ScheduleDTO{" +
                "scheduleId=" + scheduleId +
                ", storeId=" + storeId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", capacity=" + capacity +
                ", bookedPeople=" + bookedPeople +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ScheduleDTO that = (ScheduleDTO) object;
        return storeId == that.storeId && capacity == that.capacity && Objects.equals(startTime, that.startTime) && Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, startTime, endTime, capacity);
    }
}
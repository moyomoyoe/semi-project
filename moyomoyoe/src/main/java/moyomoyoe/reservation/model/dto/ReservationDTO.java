package moyomoyoe.reservation.model.dto;

import java.sql.Date;

public class ReservationDTO {
    private int resId;
    private int userIdRes;
    private Date resDate;
    private int customerNum;
    private int scheduleId;
    private String userNameRes;

    public ReservationDTO() {
    }

    public ReservationDTO(int resId, int userIdRes, Date resDate, int customerNum, int scheduleId, String userNameRes) {
        this.resId = resId;
        this.userIdRes = userIdRes;
        this.resDate = resDate;
        this.customerNum = customerNum;
        this.scheduleId = scheduleId;
        this.userNameRes = userNameRes;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public int getUserIdRes() {
        return userIdRes;
    }

    public void setUserIdRes(int userIdRes) {
        this.userIdRes = userIdRes;
    }

    public Date getResDate() {
        return resDate;
    }

    public void setResDate(Date resDate) {
        this.resDate = resDate;
    }

    public int getCustomerNum() {
        return customerNum;
    }

    public void setCustomerNum(int customerNum) {
        this.customerNum = customerNum;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getUserNameRes() {
        return userNameRes;
    }

    public void setUserNameRes(String userNameRes) {
        this.userNameRes = userNameRes;
    }

    @Override
    public String toString() {
        return "ReservationDTO{" +
                "resId=" + resId +
                ", userIdRes=" + userIdRes +
                ", resDate=" + resDate +
                ", customerNum=" + customerNum +
                ", scheduleId=" + scheduleId +
                ", userNameRes='" + userNameRes + '\'' +
                '}';
    }
}
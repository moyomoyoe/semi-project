package moyomoyoe.reservation.model.dto;

import java.util.Date;

public class ReservationDTO {
    private int resId;
    private int userIdRes;
    private Date resDate;
    private String customerNum; // 수정된 필드명
    private int scheduleId;

    public ReservationDTO() {
    }

    public ReservationDTO(int resId, int userIdRes, Date resDate, String customerNum, int scheduleId) {
        this.resId = resId;
        this.userIdRes = userIdRes;
        this.resDate = resDate;
        this.customerNum = customerNum;
        this.scheduleId = scheduleId;
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

    public String getCustomerNum() {
        return customerNum; // 수정된 Getter
    }

    public void setCustomerNum(String customerNum) {
        this.customerNum = customerNum; // 수정된 Setter
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    @Override
    public String toString() {
        return "ReservationDTO{" +
                "resId=" + resId +
                ", userIdRes=" + userIdRes +
                ", resDate=" + resDate +
                ", customerNum='" + customerNum + '\'' + // 수정된 필드명 반영
                ", scheduleId=" + scheduleId +
                '}';
    }
}
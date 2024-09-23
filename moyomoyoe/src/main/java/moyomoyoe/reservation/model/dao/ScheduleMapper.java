package moyomoyoe.reservation.model.dao;

import moyomoyoe.reservation.model.dto.ScheduleDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;

@Mapper
public interface ScheduleMapper {

    @Insert("INSERT INTO tbl_schedule (store_id, reservation_id, res_date, res_time) VALUES (#{storeId}, #{reservationId}, #{resDate}, #{resTime})")
    void insertSchedule(ScheduleDTO scheduleDTO);
}
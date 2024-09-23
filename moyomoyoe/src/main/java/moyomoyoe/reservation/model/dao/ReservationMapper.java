package moyomoyoe.reservation.model.dao;

import moyomoyoe.reservation.model.dto.ReservationDTO;
import moyomoyoe.reservation.model.dto.ScheduleDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReservationMapper {
    void insertSchedule(ScheduleDTO scheduleDTO);
    void insertReservation(ReservationDTO reservationDTO);
    List<String> getReservedTimes(@Param("storeId") int storeId, @Param("date") String date);
    // 예약 일정 리스트 조회
    List<ReservationDTO> getAllReservations();
}
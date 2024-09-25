package moyomoyoe.reservation.model.dao;

import moyomoyoe.reservation.model.dto.ReservationDTO;
import moyomoyoe.reservation.model.dto.ScheduleDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReservationMapper {
    void insertReservation(ReservationDTO reservationDTO);

    List<String> getReservedTimes(@Param("storeId") int storeId, @Param("date") String date);

    List<ScheduleDTO> getAllReservations();

    List<ScheduleDTO> getReservationList();

    ScheduleDTO getScheduleById(int scheduleId);

    void insertSchedule(ScheduleDTO scheduleDTO);
}

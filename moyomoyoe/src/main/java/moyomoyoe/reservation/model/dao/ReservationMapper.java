package moyomoyoe.reservation.model.dao;

import moyomoyoe.reservation.model.dto.ReservationDTO;
import moyomoyoe.reservation.model.dto.ScheduleDTO;
import moyomoyoe.reservation.model.dto.StoreDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReservationMapper {
    void insertReservation(ReservationDTO reservationDTO);

    List<String> getReservedTimes(@Param("storeId") int storeId, @Param("date") String date);

    List<StoreDTO> getAllStores();

    StoreDTO getStoreById(int storeId);

    void insertSchedule(ScheduleDTO scheduleDTO);

    List<ScheduleDTO> getAllReservations();

    ScheduleDTO getScheduleById(int scheduleId); // 스케줄 상세 조회 추가

    void deleteReservation(int resId);
}
package moyomoyoe.reservation.model.dao;

import moyomoyoe.reservation.model.dto.ReservationDTO;
import moyomoyoe.reservation.model.dto.ScheduleDTO;
import moyomoyoe.reservation.model.dto.StoreDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReservationMapper {

    // 특정 스케줄 존재 여부 확인
    boolean scheduleExists(Map<String, Object> params); // Map 매개변수로 수정

    // 예약 저장
    void insertReservation(ReservationDTO reservationDTO);

    // 예약된 시간 조회
    List<String> getReservedTimes(int storeId, String date);

    // 모든 매장 정보 조회
    List<StoreDTO> getAllStores();

    // 특정 매장 정보 조회
    StoreDTO getStoreById(int storeId);

    // 특정 매장의 일정 조회
    List<ScheduleDTO> getSchedulesByStoreId(int storeId);

    // 예약 취소(업데이트)
    List<ScheduleDTO> getAllReservations();

    ScheduleDTO getScheduleById(int scheduleId);

    ReservationDTO getReservationById(int resId);

    // 사용자 ID를 이용해 사용자 이름 조회 메서드 추가
//    String getUserNameByUserId(@Param("userId") int userId);

    List<ReservationDTO> getUserReservations(int userId);

//    ReservationDTO getReservationDetail(int resId);

    // 예약 정보와 상점 정보를 조인해서 상세 조회
    Map<String, Object> getReservationDetailWithStore(int resId);

    // 사용자의 사업장 ID 조회
    Integer getStoreIdByUserId(int userId);

    // 사업장별 예약 조회 메서드 추가
    List<ReservationDTO> getReservationsByStore(@Param("storeId") int storeId);

    List<Integer> getResBySchedule(int id);

    void deleteReservation(@Param("resId") int resId);


}
package moyomoyoe.reservation.model.service;

import moyomoyoe.reservation.model.dao.ReservationMapper;
import moyomoyoe.reservation.model.dao.ScheduleMapper;
import moyomoyoe.reservation.model.dto.ReservationDTO;
import moyomoyoe.reservation.model.dto.ScheduleDTO;
import moyomoyoe.reservation.model.dto.StoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReservationService {
    private final ScheduleMapper dao;
    private final ReservationMapper reservationMapper;

    @Autowired
    public ReservationService(ScheduleMapper dao, ReservationMapper reservationMapper) {
        this.dao = dao;
        this.reservationMapper = reservationMapper;
    }

    @Transactional
    public void saveReservation(ReservationDTO reservationDTO, ScheduleDTO scheduleDTO) {
        // ScheduleDTO에서 일정 ID 가져오기
        int scheduleId = scheduleDTO.getScheduleId(); // 여기서 scheduleId를 가져옴
        int storeId = scheduleDTO.getStoreId();

        // 예약 DTO에 scheduleId 설정
        reservationDTO.setScheduleId(scheduleId); // 여기서 reservationDTO에 scheduleId 설정

        // 스케줄 존재 여부 확인
        Map<String, Object> params = new HashMap<>();
        params.put("scheduleId", scheduleId);
        params.put("storeId", storeId);

        boolean exists = reservationMapper.scheduleExists(params);
        if (!exists) {
            throw new IllegalArgumentException("해당 스케줄은 존재하지 않습니다.");
        }

        // 예약 저장
        reservationMapper.insertReservation(reservationDTO);
    }

    public List<String> getReservedTimes(int storeId, String date) {
        return reservationMapper.getReservedTimes(storeId, date);
    }

    public List<StoreDTO> getAllStores() {
        return reservationMapper.getAllStores();
    }

    public StoreDTO getStoreById(int storeId) {
        return reservationMapper.getStoreById(storeId);
    }


    // 모든 예약 일정 조회
    public List<ScheduleDTO> getAllReservations() {
        return reservationMapper.getAllReservations();
    }

    public ReservationDTO getReservationById(int resId) {
        return reservationMapper.getReservationById(resId);
    }

    // 특정 스케줄 상세 조회
    public ScheduleDTO getScheduleById(int scheduleId) {
        return reservationMapper.getScheduleById(scheduleId);
    }

    // 특정 매장의 일정 조회
    public List<ScheduleDTO> getSchedulesByStoreId(int storeId) {
        return reservationMapper.getSchedulesByStoreId(storeId);
    }

    // 사용자 예약 정보 조회
    public List<ReservationDTO> getUserReservations(int userId) {
        return reservationMapper.getUserReservations(userId);
    }

    // 예약 상세 정보 조회
//    public ReservationDTO getReservationDetail(int resId) {
//        return reservationMapper.getReservationDetail(resId);
//    }
//
//    // userId 가져와서 userName 조회
//    public String getUserNameByUserId(int userId) {
//        return reservationMapper.getUserNameByUserId(userId);
//    }

    // 상세조회(상점정보+예약정보)
    public Map<String, Object> getReservationDetailWithStore(int resId) {
        return reservationMapper.getReservationDetailWithStore(resId);
    }

    // 사용자의 사업장 ID 조회 메소드 추가
    public Integer getStoreIdByUserId(int userId) {
        return reservationMapper.getStoreIdByUserId(userId);
    }

    public List<ReservationDTO> getReservationsByStore(int storeId) {
        return reservationMapper.getReservationsByStore(storeId);
    }

    // 기타 예약 관련 서비스 로직들 추가
    public void saveReservation(ReservationDTO reservationDTO) {
        reservationMapper.insertReservation(reservationDTO);
    }

    // 예약 취소
    @Transactional
    public void cancelReservation(int resId) {
        reservationMapper.deleteReservation(resId);
    }
}
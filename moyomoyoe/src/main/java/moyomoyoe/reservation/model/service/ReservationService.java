package moyomoyoe.reservation.model.service;

import moyomoyoe.reservation.model.dao.ReservationMapper;
import moyomoyoe.reservation.model.dto.ReservationDTO;
import moyomoyoe.reservation.model.dto.ScheduleDTO;
import moyomoyoe.reservation.model.dto.StoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationMapper reservationMapper;

    @Autowired
    public ReservationService(ReservationMapper reservationMapper) {
        this.reservationMapper = reservationMapper;
    }

    // 스케줄 저장
    @Transactional
    public void saveSchedule(ScheduleDTO scheduleDTO) {
        reservationMapper.insertSchedule(scheduleDTO);
    }

    // 스케줄과 예약을 트랜잭션으로 저장
    @Transactional
    public void saveReservation(ReservationDTO reservationDTO, ScheduleDTO scheduleDTO) {
        reservationMapper.insertSchedule(scheduleDTO);
        reservationDTO.setScheduleId(scheduleDTO.getScheduleId());
        reservationMapper.insertReservation(reservationDTO);
    }

    // 예약된 시간 조회
    public List<String> getReservedTimes(int storeId, String date) {
        return reservationMapper.getReservedTimes(storeId, date);
    }

    // 모든 매장 정보 조회
    public List<StoreDTO> getAllStores() {
        return reservationMapper.getAllStores();
    }

    // 특정 매장 정보 조회
    public StoreDTO getStoreById(int storeId) {
        return reservationMapper.getStoreById(storeId);
    }

    // 모든 예약 일정 조회
    public List<ScheduleDTO> getAllReservations() {
        return reservationMapper.getAllReservations();
    }

    // 특정 스케줄 상세 조회
    public ScheduleDTO getScheduleById(int scheduleId) {
        return reservationMapper.getScheduleById(scheduleId);
    }

    // 특정 매장의 일정 조회
    public List<ScheduleDTO> getSchedulesByStoreId(int storeId) {
        return reservationMapper.getSchedulesByStoreId(storeId);
    }

    // 예약 취소
    public void cancelReservation(int resId) {
        reservationMapper.deleteReservation(resId);
    }
}
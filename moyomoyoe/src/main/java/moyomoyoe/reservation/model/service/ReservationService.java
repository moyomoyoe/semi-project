package moyomoyoe.reservation.model.service;

import moyomoyoe.reservation.model.dao.ReservationMapper;
import moyomoyoe.reservation.model.dto.ReservationDTO;
import moyomoyoe.reservation.model.dto.ScheduleDTO;
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

    @Transactional
    public void saveReservation(ReservationDTO reservationDTO, ScheduleDTO scheduleDTO) {
        // 1. tbl_schedule에 데이터 삽입 (Schedule 먼저 삽입)
        reservationMapper.insertSchedule(scheduleDTO);

        // 2. Schedule의 ID를 Reservation에 설정
        reservationDTO.setScheduleId(scheduleDTO.getScheduleId());

        // 3. tbl_reservation에 데이터 삽입 (Reservation 삽입)
        reservationMapper.insertReservation(reservationDTO);
    }

    // 예약된 시간 반환 메서드 추가
    public List<String> getReservedTimes(int storeId, String date) {
        return reservationMapper.getReservedTimes(storeId, date);
    }

    // 예약 리스트 조회 서비스
    @Transactional(readOnly = true)
    public List<ReservationDTO> getAllReservations() {
        return reservationMapper.getAllReservations();
    }
}
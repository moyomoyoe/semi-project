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
        reservationMapper.insertSchedule(scheduleDTO);
        reservationDTO.setScheduleId(scheduleDTO.getScheduleId());
        reservationMapper.insertReservation(reservationDTO);
    }

    public List<String> getReservedTimes(int storeId, String date) {
        return reservationMapper.getReservedTimes(storeId, date);
    }

    public List<ScheduleDTO> getAllReservations() {
        return reservationMapper.getAllReservations();
    }

    public List<ScheduleDTO> getReservationList() {
        return reservationMapper.getReservationList();
    }

    public ScheduleDTO getScheduleById(int scheduleId) {
        return reservationMapper.getScheduleById(scheduleId);
    }
}
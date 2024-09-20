package moyomoyoe.reservation.Service;

import moyomoyoe.reservation.DTO.ScheduleDTO;
import moyomoyoe.reservation.DTO.StoreDTO;
import moyomoyoe.reservation.mapper.ReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {
    ReservationMapper dao;
    @Autowired
    public ScheduleService(ReservationMapper dao) {
        this.dao = dao;
    }

    public StoreDTO getStoreAllInfo(int code) {
        return dao.getStoreAllInfo(code);
    }

    public List<ScheduleDTO> getSchedule(int code) {
        return dao.getSchedule(code);
    }

    public int registSchedule(List<ScheduleDTO> scheduleDTOS) {
        return dao.registSchedule(scheduleDTOS);
    }

    public int registStore(StoreDTO info) {
        return dao.registStore(info);
    }
}

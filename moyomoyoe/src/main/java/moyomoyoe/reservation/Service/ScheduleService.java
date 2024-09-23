package moyomoyoe.reservation.Service;

import moyomoyoe.reservation.DTO.ScheduleDTO;
import moyomoyoe.reservation.DTO.StoreDTO;
import moyomoyoe.reservation.mapper.ReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Transactional
    public void registSchedule(int code, List<ScheduleDTO> scheduleDTOS) {
        //기존의 일정
        List<ScheduleDTO> resent = dao.getSchedule(code);
        //전달받은 매개변수에, 기존의 일정과 "같은" 일정이 있는지 확인, 없는 애들은 삭제조치,
        List<Integer> deletedSchedules = new ArrayList<>();
        List<Integer> insertSchedules = new ArrayList<>();

        //삭제된 애들은 삭제조치
        for (ScheduleDTO schedule : resent) {
            if (!scheduleDTOS.contains(schedule)) {
                deletedSchedules.add(schedule.getScheduleId());
            }
        }

        //추가된 애들은 삽입 조치,
        for (ScheduleDTO schedule : scheduleDTOS) {
            if (!resent.contains(schedule)) {
                insertSchedules.add(schedule.getScheduleId());
            }
        }

        //삭제할 일정의 예약도 삭제해야함
        dao.deleteScheduleId(code,deletedSchedules);
        dao.registSchedule(scheduleDTOS);
    }

    //예약 가능한 일정인지 확인(현재 예약의 예약인 수 )
    public void curBookedPeople(int code) {
         dao.curBookedPeople(code);
    }

    @Transactional
    public void registStore(StoreDTO info) {
        //이미 등록된 가게라면 update
        System.out.println("서비스단 정보"+info);
        if(info.getStoreId()==0)
            dao.registStore(info);
        else
            dao.updateStore(info);

    }
}

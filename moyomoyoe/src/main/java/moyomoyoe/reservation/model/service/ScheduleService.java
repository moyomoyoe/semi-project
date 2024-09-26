package moyomoyoe.reservation.model.service;

import moyomoyoe.reservation.model.dto.ScheduleDTO;
import moyomoyoe.reservation.model.dto.StoreDTO;
import moyomoyoe.reservation.model.dao.ScheduleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleService {
    moyomoyoe.reservation.model.dao.ScheduleMapper dao;
    @Autowired
    public ScheduleService(ScheduleMapper dao) {
        this.dao = dao;
    }

    public Integer FindUserStore(int code){
        return dao.findUserStore(code);
    }

    public StoreDTO getStoreAllInfo(int code) {
        return dao.getStoreAllInfo(code);
    }

    public List<ScheduleDTO> getSchedule(int code) {
        return dao.getSchedule(code);
    }

    @Transactional
    public void registSchedule(int code, List<ScheduleDTO> scheduleDTOS) {
        // 새로운 일정 사라진 일정 반영,
        //1. 해당 일정에 예약이 있는 지 확인함
        // <쉬운방식 아무튼 예약 다 취소함.

        //어려운 방식 기존의 일정테이블을 가져온 뒤,
        // 삭제된 일정의 예약만 취소
        //2. 남아있는지 확인을 어떻게 하는가? equals 오버라이딩
        //그러면 필요한 것. 기존의 스케쥴과 보내준 스케쥴 리스트...


        //기존의 일정
        List<ScheduleDTO> resent = dao.getSchedule(code);
        //전달받은 매개변수에, 기존의 일정과 "같은" 일정이 있는지 확인, 없는 애들은 삭제조치,
        List<Integer> deletedSchedules = new ArrayList<>();
        List<ScheduleDTO> insertSchedules = new ArrayList<>();

        //삭제된 애들은 삭제조치
        for (ScheduleDTO schedule : resent) {
            if (!scheduleDTOS.contains(schedule)) {
                deletedSchedules.add(schedule.getScheduleId());
            }
        }

        //추가된 애들은 삽입 조치,
        for (ScheduleDTO schedule : scheduleDTOS) {
            if (!resent.contains(schedule)) {
                insertSchedules.add(schedule);
            }
        }

        //삭제할 일정의 예약도 삭제해야함 -> 예약 취소랑 통합 후 재 테스트 필요
        for(int id : deletedSchedules){
            System.out.println(id+ "삭제로직 구비중");
            dao.deleteScheduleId(id);
        }
        for(ScheduleDTO s:insertSchedules){
            System.out.println(s+"삽입로직 쿼리 준비중");
            dao.registSchedule(s);
        }

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
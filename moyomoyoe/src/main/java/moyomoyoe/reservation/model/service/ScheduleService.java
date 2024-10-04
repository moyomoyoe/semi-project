package moyomoyoe.reservation.model.service;

import moyomoyoe.image.ImageDTO;
import moyomoyoe.member.user.model.dao.UserMapper;
import moyomoyoe.reservation.model.dao.ReservationMapper;
import moyomoyoe.reservation.model.dto.ScheduleDTO;
import moyomoyoe.reservation.model.dto.StoreDTO;
import moyomoyoe.reservation.model.dao.ScheduleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleService {
    @Autowired
    ScheduleMapper dao;

    @Autowired
    ReservationMapper resDao;

    @Autowired
    UserMapper userDao;


    public Integer FindUserStore(int code){
        return dao.findUserStore(code);
    }

    public StoreDTO getStoreAllInfo(int code) {
        return dao.getStoreAllInfo(code);
    }

    public List<ScheduleDTO> getSchedule(int code) {
        return dao.getSchedule(code);
    }
    public ScheduleDTO getScheduleById(int code) {
        return dao.getScheduleById(code);
    }
    public List<Map<String,String>> getUserFullReserInfo(int code){
        return dao.getUserFullReserInfo(code);
    }

    @Transactional
    public void registSchedule(int code, List<ScheduleDTO> scheduleDTOS) {
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
            List<Integer> resList = dao.getResBySchedule(id);
                for(int rid : resList)
                    resDao.deleteReservation(rid);
            dao.deleteScheduleId(id);
        }
        for(ScheduleDTO s:insertSchedules){
            System.out.println(s+"삽입로직 쿼리 준비중");
            dao.registSchedule(s);
        }
    }

    @Transactional
    public void registStore(StoreDTO info) {
        //이미 등록된 가게라면 update, 이미 있는 이미지라면 비업로드
        System.out.println("서비스단 정보"+info);
        if(info.getStoreId()==0)
            dao.registStore(info);
        else
            dao.updateStore(info);
    }

    @Transactional
    public void deleteStore(int code) {
        registSchedule(code, new ArrayList<>());
        dao.deleteStore(code);
    }

    @Transactional
    public int registImage(ImageDTO newImage) {
        userDao.registImage(newImage);
       return dao.getImageId(newImage.getImageName());
    }

    public String getImageById(int i) {
        return dao.getImageById(i);
    }


}
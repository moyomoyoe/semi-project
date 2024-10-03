package moyomoyoe.reservation.model.dao;

import moyomoyoe.reservation.model.dto.StoreDTO;
import moyomoyoe.reservation.model.dto.ScheduleDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ScheduleMapper {
    StoreDTO getStoreAllInfo(int code);
    List<ScheduleDTO> getSchedule(int code);
    void registSchedule(List<ScheduleDTO> scheduleDTOS);
    void registStore(StoreDTO info);
    void deleteScheduleId(int id);
    void curBookedPeople(int code);

    void updateStore(StoreDTO info);
    void registSchedule(ScheduleDTO s);

    Integer findUserStore(int code);


    void deleteStore(int code);

    List<Integer> getResBySchedule(int id);

    int getImageId(String imageName);

    String getImageById(int i);

    ScheduleDTO getScheduleById(int code);

    List<Map<String,String>> getUserFullReserInfo(int code);

}
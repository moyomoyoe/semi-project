package moyomoyoe.reservation.mapper;

import moyomoyoe.reservation.DTO.ScheduleDTO;
import moyomoyoe.reservation.DTO.StoreDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReservationMapper {
    StoreDTO getStoreAllInfo(int code);
    List<ScheduleDTO> getSchedule(int code);
    void registSchedule(List<ScheduleDTO> scheduleDTOS);
    void registStore(StoreDTO info);
    void deleteScheduleId(int code, int id);
    void curBookedPeople(int code);

    void updateStore(StoreDTO info);
    void registSchedule(ScheduleDTO s);
}

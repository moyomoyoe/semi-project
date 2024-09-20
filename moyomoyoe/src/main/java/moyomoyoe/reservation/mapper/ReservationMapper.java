package moyomoyoe.reservation.mapper;

import moyomoyoe.reservation.DTO.ScheduleDTO;
import moyomoyoe.reservation.DTO.StoreDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReservationMapper {
    StoreDTO getStoreAllInfo(int code);
    List<ScheduleDTO> getSchedule(int code);
    int registSchedule(List<ScheduleDTO> scheduleDTOS);
    int registStore(StoreDTO info);
}

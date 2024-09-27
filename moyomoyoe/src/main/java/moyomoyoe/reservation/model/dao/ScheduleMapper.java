package moyomoyoe.reservation.model.dao;

import moyomoyoe.reservation.model.dto.StoreDTO;
import moyomoyoe.reservation.model.dto.ScheduleDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

<<<<<<< HEAD
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
    }
=======
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
}
>>>>>>> a7c70ede3a188cc83720b365b25fe12cccd26338

package moyomoyoe.reservation.model.dao;

import moyomoyoe.reservation.DTO.StoreDTO;
import moyomoyoe.reservation.model.dto.ScheduleDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;

import java.util.List;

    @Mapper
    public interface ScheduleMapper {
        StoreDTO getStoreAllInfo(int code);
        List<moyomoyoe.reservation.DTO.ScheduleDTO> getSchedule(int code);
        void registSchedule(List<moyomoyoe.reservation.DTO.ScheduleDTO> scheduleDTOS);
        void registStore(StoreDTO info);
        void deleteScheduleId(int code, int id);
        void curBookedPeople(int code);

        void updateStore(StoreDTO info);
        void registSchedule(moyomoyoe.reservation.DTO.ScheduleDTO s);
    }
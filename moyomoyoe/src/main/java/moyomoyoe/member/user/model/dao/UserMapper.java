package moyomoyoe.member.user.model.dao;

import moyomoyoe.member.auth.model.dto.UserDTO;
import moyomoyoe.member.user.model.dto.RegionDTO;
import moyomoyoe.member.user.model.dto.SignupDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    int regist(SignupDTO newUserInfo);

    UserDTO findByAccount(String account);

    List<RegionDTO> findAllRegion();
}

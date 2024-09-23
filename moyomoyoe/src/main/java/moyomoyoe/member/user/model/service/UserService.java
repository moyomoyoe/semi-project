package moyomoyoe.member.user.model.service;

import moyomoyoe.member.auth.model.dto.UserDTO;
import moyomoyoe.member.user.model.dao.UserMapper;
import moyomoyoe.member.user.model.dto.RegionDTO;
import moyomoyoe.member.user.model.dto.SignupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private PasswordEncoder encoder;
    private UserMapper userMapper;

    @Autowired
    public UserService(PasswordEncoder encoder, UserMapper userMapper) {
        this.encoder = encoder;
        this.userMapper = userMapper;
    }

    public Integer regist(SignupDTO newUserInfo) {

        System.out.println("암호화 전 = " + newUserInfo.getPassword());
        newUserInfo.setPassword(encoder.encode(newUserInfo.getPassword()));
        System.out.println("암호화 뾰로롱 = " + newUserInfo.getPassword());

        Integer result = null;

        try{
            result = userMapper.regist(newUserInfo);
        } catch(DuplicateKeyException e) {
            result = 0;
            e.printStackTrace();
        } catch(BadSqlGrammarException e) {
            result = 0;
            e.printStackTrace();
        }

        System.out.println("회원 가입 처리 결과 = " + result);


        return result;
    }

    public UserDTO findByAccount(String account) {

        UserDTO foundUser = userMapper.findByAccount(account);

        if(!Objects.isNull(foundUser)) {

            return foundUser;
        } else {
            return null;
        }

    }

    public List<RegionDTO> findAllRegion() {
        return userMapper.findAllRegion();
    }

}

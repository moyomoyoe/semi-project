package moyomoyoe.member.user.model.service;

import jakarta.annotation.PostConstruct;
import moyomoyoe.member.auth.model.dto.UserDTO;
import moyomoyoe.member.user.model.dao.UserMapper;
import moyomoyoe.member.user.model.dto.RegionDTO;
import moyomoyoe.member.user.model.dto.SignupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private PasswordEncoder encoder;
    private UserMapper userMapper;
    private final DummyDataGenerator dummyDataGenerator;

    @Autowired
    public UserService(PasswordEncoder encoder, UserMapper userMapper, DummyDataGenerator dummyDataGenerator) {
        this.encoder = encoder;
        this.userMapper = userMapper;
        this.dummyDataGenerator = dummyDataGenerator;
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

        System.out.println(userMapper.findAllRegion());

        return userMapper.findAllRegion();
    }

    @PostConstruct
    public void generate() {
        dummyDataGenerator.generate();
    }

    public RegionDTO getRegionByUserId(int id) {
        return userMapper.getRegionByUserId(id);

    }

}

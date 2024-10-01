package moyomoyoe.member.user.model.service;

import jakarta.annotation.PostConstruct;
import moyomoyoe.member.auth.model.dto.UserDTO;
import moyomoyoe.member.user.model.dao.UserMapper;
import moyomoyoe.image.ImageDTO;
import moyomoyoe.member.user.model.dto.RegionDTO;
import moyomoyoe.member.user.model.dto.SignupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
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

    public UserDTO getDistrictByAccount(String account) {

        UserDTO user = userMapper.findByAccount(account);
        RegionDTO region = userMapper.getRegionByUserId(user.getId());
        ImageDTO image = userMapper.getImageById(user.getId());

        System.out.println("[서비스] 왓니? ");

        if(region != null) {
            user.setRegion(region.getDistrict());
        }

        if(image != null) {
            user.setImage(image.getImageName());
        }

        System.out.println("[서비스] region = " + region);

        return user;
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

    public Integer update(UserDTO newUserInfo) {

        System.out.println("암호화 전 = " + newUserInfo.getPassword());
        newUserInfo.setPassword(encoder.encode(newUserInfo.getPassword()));
        System.out.println("암호화 뾰로롱 = " + newUserInfo.getPassword());

        Integer result = null;

        try{
            result = userMapper.update(newUserInfo);
        } catch(DuplicateKeyException e) {
            result = 0;
            e.printStackTrace();
        } catch(BadSqlGrammarException e) {
            result = 0;
            e.printStackTrace();
        }

        System.out.println("회원 정보 수정 처리 결과 = " + result);

        return result;
    }

    public void registImage(ImageDTO newImage) {
        userMapper.registImage(newImage);
    }

    public ImageDTO getImageById(int id) {
        System.out.println("[작동하니?]");
        return userMapper.getImageById(id);
    }
}

package moyomoyoe.member.user.controller;

import moyomoyoe.member.user.model.dto.RegionDTO;
import moyomoyoe.member.user.model.dto.SignupDTO;
import moyomoyoe.member.user.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/member/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public void signup() {}

    @PostMapping("/signup")
    public ModelAndView signup(ModelAndView mv,
                               @ModelAttribute SignupDTO newUserInfo,
                               @RequestParam String phone,
                               @RequestParam String email) {

        String phoneNum = phone.replaceAll(",", "-");
        System.out.println(phoneNum);

        newUserInfo.setPhone(phoneNum);

        String fullEmail = email.replaceAll(",", "@");
        System.out.println(fullEmail);

        newUserInfo.setEmail(fullEmail);

        Integer result = userService.regist(newUserInfo);

        String message = null;

        if(result == null) {
            message = "이미 가입 된 회원 정보 입니다.";
            System.out.println(message);

            mv.setViewName("member/user/signup");
        } else if(result == 0) {
            message = "회원 가입 실패 하였습니다. 다시 시도해주세요.";
            System.out.println(message);

            mv.setViewName("member/user/signup");
        } else if(result >= 1) {
            message = "회원 가입 성공하였습니다.";
            System.out.println(message);

            mv.setViewName("member/auth/login");
        } else {
            message = "알 수 없는 오류가 발생하였습니다. 다시 시도 해주세요.";
            System.out.println(message);

            mv.setViewName("member/user/signup");
        }
        System.out.println(result);
        return mv;
    }

    @GetMapping(value = "region", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<RegionDTO> findRegionList() {
        System.out.println("JS 내장 함수 fetch");
        return userService.findAllRegion();
    }

    @Autowired
    private DataSource dataSource;

    @GetMapping(value = "checkAccount", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Map<String, Object> checkAccount(@RequestParam String account) {

        System.out.println("아이디 중복 확인 fetch");

        Map<String, Object> resp = new HashMap<>();

        String query = "SELECT COUNT(*) FROM tbl_user WHERE account = ?";

        try (Connection connection = dataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, account);
            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()) {
                resp.put("exists", resultSet.getInt(1) > 0);
//                resp.put("empty", resultSet.getInt(1) == 0);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.put("error", "오류 발생 쉬먀");
        }

        return resp;
    }

}

package moyomoyoe.member.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import moyomoyoe.member.auth.model.dto.UserDTO;
import moyomoyoe.member.user.model.dto.RegionDTO;
import moyomoyoe.member.user.model.dto.SignupDTO;
import moyomoyoe.member.user.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.sql.DataSource;
import java.security.Principal;
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
        List<RegionDTO> region = userService.findAllRegion();
        System.out.println("컨트롤러에서 확인해용 region = " + region);
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

    @GetMapping("/myPage")
    public ModelAndView myPage(ModelAndView mv, Principal principal) {

        String account = principal.getName();

        UserDTO user = userService.getDistrictByAccount(account);

        mv.addObject("user", user);

        mv.setViewName("member/user/myPage");

        return mv;
    }

    @GetMapping("/userInfo")
    public ModelAndView userInfo(ModelAndView mv, Principal principal) {

        String account = principal.getName();

        UserDTO user = userService.getDistrictByAccount(account);

        mv.addObject("user", user);

        mv.setViewName("member/user/userInfo");

        return mv;
    }

    @GetMapping("/editInfo")
    public ModelAndView editInfo(ModelAndView mv, Principal principal) {

        String account = principal.getName();

        UserDTO user = userService.getDistrictByAccount(account);

        mv.addObject("user", user);

        mv.setViewName("member/user/editInfo");

        return mv;
    }

    @PostMapping("/updateInfo")
    public ModelAndView updateInfo(ModelAndView mv,
                                   @ModelAttribute UserDTO newUserInfo,
                                   @RequestParam String phone,
                                   @RequestParam String email,
                                   HttpServletRequest req,
                                   Principal principal) {

        System.out.println("회원 수정 되고 있니?");

        String account = principal.getName();
        newUserInfo.setAccount(account);

        UserDTO user = userService.getDistrictByAccount(account);

        String phoneNum = phone.replaceAll(",", "-");
        System.out.println(phoneNum);

        newUserInfo.setPhone(phoneNum);

        String fullEmail = email.replaceAll(",", "@");
        System.out.println(fullEmail);

        newUserInfo.setEmail(fullEmail);

        Integer result = userService.update(newUserInfo);

        String message = null;

        if(result == null) {
            message = "변경 사항이 없는건가?";
            System.out.println(message);

            mv.setViewName("member/user/editInfo");
        } else if(result == 0) {
            message = "회원 정보 수정에 실패 하였습니다. 다시 시도해주세요.";
            System.out.println(message);

            mv.setViewName("member/user/editInfo");
        } else if(result >= 1) {
            message = "회원 정보 수정 성공하였습니다.";
            System.out.println(message);

            mv.setViewName("redirect:/member/user/userInfo");

            UserDTO updatedUser = userService.findByAccount(account);

            req.getSession().removeAttribute("user");
            Map<String, Object> userSession = new HashMap<>();
            userSession.put("id", updatedUser.getId());
            userSession.put("username", updatedUser.getName());
            userSession.put("account", updatedUser.getAccount());
            userSession.put("nickname", updatedUser.getNickname());
            userSession.put("phone", updatedUser.getPhone());
            userSession.put("email", updatedUser.getEmail());

            RegionDTO region = userService.getRegionByUserId(updatedUser.getId());
            if(region != null) {
                userSession.put("region", region.getDistrict());
            }

            req.getSession().setAttribute("user", userSession);

            System.out.println("[회원 정보 수정 후] 세션 저장 확인 = " + userSession);

        } else {
            message = "알 수 없는 오류가 발생하였습니다. 다시 시도 해주세요.";
            System.out.println(message);

            mv.setViewName("member/user/editInfo");
        }

        mv.addObject("user", user);

        return mv;
    }

}

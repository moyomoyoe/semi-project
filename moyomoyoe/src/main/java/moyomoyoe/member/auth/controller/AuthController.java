package moyomoyoe.member.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import moyomoyoe.member.user.model.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/member/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/fail")
    public ModelAndView loginFail(ModelAndView mv,
                                  @RequestParam String message) {

        mv.addObject("message", message);
        mv.setViewName("member/auth/fail");

        return mv;
    }

    @GetMapping("/login")
    public void login() {}

//    @PostMapping("/login")
//    public String login(@RequestParam String account,
//                        @RequestParam String password,
//                        HttpServletRequest request) {
//
//        String nickname = userService.getNickname(account, password);
//        if(nickname != null) {
//            HttpSession session = request.getSession();
//            session.setAttribute("nickname", nickname);
//            return "redirect:/main/main";
//        }
//        return "redirect:/member/auth/fail?message";
//    }
}

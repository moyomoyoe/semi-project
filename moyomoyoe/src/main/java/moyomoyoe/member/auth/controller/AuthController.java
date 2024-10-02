package moyomoyoe.member.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/member/auth")
public class AuthController {

    @GetMapping("/fail")
    public ModelAndView loginFail(ModelAndView mv,
                                  @RequestParam String message) {

        System.out.println("[로그인실패쓰] 왓니?");

        mv.addObject("message", message);
        mv.setViewName("member/auth/fail");

        return mv;
    }

    @GetMapping("/login")
    public void login() {

        System.out.println("[로그인 페이지] 왓니?");

    }

    @GetMapping("/success")
    public ModelAndView loginSuccess(ModelAndView mv) {

        System.out.println("[로그인 성공] 됏니?");


        return mv;
    }

}
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

        mv.addObject("message", message);
        mv.setViewName("member/auth/fail");

        return mv;
    }

    @GetMapping("/login")
    public void login() {}

    @GetMapping("/success")
    public ModelAndView loginSuccess(ModelAndView mv) {
        return mv;
    }

}

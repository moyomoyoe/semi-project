
package moyomoyoe;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {
    @RequestMapping({"/", "/main"})
    public ModelAndView main(ModelAndView mv) {
         mv.setViewName("static/main");
        return mv;
    }
    @GetMapping("/admin/page")
    public ModelAndView adminPage(ModelAndView mv) {
        mv.setViewName("admin/admin");
        return mv;
    }
    @GetMapping("/user/page")
    public ModelAndView userPage(ModelAndView mv) {
        mv.setViewName("user/user");
        return mv;
    }
}


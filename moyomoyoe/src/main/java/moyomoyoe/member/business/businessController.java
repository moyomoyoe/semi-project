package moyomoyoe.member.business;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/member/business")
public class businessController {

    public void getSession(HttpServletRequest req, Model model) {

        Map<String, Object> userSession = (Map<String, Object>) req.getSession().getAttribute("user");

        model.addAttribute("userSession", userSession);

        System.out.println("userSession = " + userSession);
    }

    @GetMapping("/myPage")
    public String myPage(HttpServletRequest req, Model model) {

        getSession(req, model);

        return "/member/business/myPage";
    }

}

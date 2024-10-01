package moyomoyoe.member.business;

import moyomoyoe.member.auth.model.dto.UserDTO;
import moyomoyoe.member.user.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import java.security.Principal;

@Controller
@RequestMapping("/member/business")
public class businessController {

    @Autowired
    private UserService userService;

    @GetMapping("/myPage")
    public String myPage(ModelAndView mv, Principal principal) {

        String account = principal.getName();

        UserDTO user = userService.getDistrictByAccount(account);

        mv.addObject("user", user);

        return "redirect:/reservation/schedule/storeInfo/"+user.getId();
    }

}
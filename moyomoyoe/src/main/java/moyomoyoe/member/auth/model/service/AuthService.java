package moyomoyoe.member.auth.model.service;

import moyomoyoe.member.auth.model.dto.UserDTO;
import moyomoyoe.member.user.model.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthService implements UserDetailsService {

    private UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {

        UserDTO foundUser = userService.findByAccount(account);

        System.out.println("[foundUser] = " + foundUser);

        if(foundUser == null) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않거나 비활성화 된 계정입니다.");
        }

        if("N".equals(foundUser.getIsActive())) {

            System.out.println("[비활성화여부잘가져오냐????????]" + foundUser.getIsActive());

            throw new UsernameNotFoundException("비활성화 된 계정입니다.");
        }
//        if(Objects.isNull(foundUser)) {
//            throw new UsernameNotFoundException("회원 정보가 존재하지 않거나 비활성화 된 계정입니다.");
//        }

        System.out.println("회원 정보 foundUser = " + foundUser);

        return foundUser;
    }
}
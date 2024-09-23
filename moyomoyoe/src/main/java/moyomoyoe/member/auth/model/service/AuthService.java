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

        if(Objects.isNull(foundUser)) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        return foundUser;
    }
}

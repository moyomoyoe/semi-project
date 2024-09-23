package moyomoyoe.member.user.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DummyDataGenerator {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void generate() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String rawPassword = "admin";

        String hashedPassword = passwordEncoder.encode(rawPassword);

        String sql = "INSERT INTO tbl_user (username, account, password, nickname, email, phone, user_role) VALUES (?, ?,?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql, "관리자", "admin", hashedPassword, "관리자", "admin@moyomoyoe.com", "010-5656-5050", "ADMIN");

        System.out.println("원본이고용 : " + rawPassword);
        System.out.println("해싱이고용 : " + hashedPassword);
    }
}

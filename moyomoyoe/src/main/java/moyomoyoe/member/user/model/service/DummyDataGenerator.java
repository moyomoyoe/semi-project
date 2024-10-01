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

        String checkSql = "SELECT COUNT(*) FROM tbl_user WHERE account = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, new Object[]{"admin"}, Integer.class);

        if(count == 0) {
            String sql = "INSERT INTO tbl_user (username, account, password, nickname, email, phone, user_role, user_region, image_id) VALUES (?, ?,?, ?, ?, ?, ?, ?, '1')";

            jdbcTemplate.update(sql, "관리자", "admin", hashedPassword, "관리자", "admin@moyomoyoe.com", "010-5656-5050", "ADMIN", "5");

            System.out.println("원본이고용 : " + rawPassword);
            System.out.println("해싱이고용 : " + hashedPassword);
        } else {
            System.out.println("이미 존재하는 관리자 계정입니다.");
        }

    }
}
package moyomoyoe;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@MapperScan
public class MoyomoyoeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoyomoyoeApplication.class, args);
    }

}

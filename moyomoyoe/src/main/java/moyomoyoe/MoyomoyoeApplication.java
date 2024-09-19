package moyomoyoe;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@MapperScan
public class MoyomoyoeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoyomoyoeApplication.class, args);
    }

}

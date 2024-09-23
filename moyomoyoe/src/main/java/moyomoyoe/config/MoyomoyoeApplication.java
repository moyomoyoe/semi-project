package moyomoyoe.config;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "moyomoyoe")
@MapperScan(basePackages = "moyomoyoe", annotationClass = Mapper.class)
public class MoyomoyoeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoyomoyoeApplication.class, args);
    }

}

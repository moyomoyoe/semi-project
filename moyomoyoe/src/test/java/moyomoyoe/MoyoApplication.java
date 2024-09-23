package moyomoyoe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "moyomoyoe.reservation.model.dao")  // 매퍼 패키지 경로 설정
public class MoyoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoyoApplication.class, args);
    }
}
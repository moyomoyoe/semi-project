package moyomoyoe;

import org.springframework.boot.SpringApplication;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan(basePackages = "moyomoyoe.reservation.model.dao")  // 매퍼 패키지 경로 설정
public class MoyoTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoyoTestApplication.class, args);
    }
}
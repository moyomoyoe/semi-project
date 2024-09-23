package moyomoyoe;

import moyomoyoe.member.user.model.service.DummyDataGenerator;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "moyomoyoe")
@MapperScan(basePackages = "moyomoyoe", annotationClass = Mapper.class)
public class MoyomoyoeApplication implements CommandLineRunner {

    @Autowired
    private DummyDataGenerator dummyDataGenerator;

    public static void main(String[] args) {
        SpringApplication.run(moyomoyoe.MoyomoyoeApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        dummyDataGenerator.generate();
    }
}

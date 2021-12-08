package medical.education;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@ComponentScan(basePackages = {"spring.backend","medical.education"})
@SpringBootApplication
public class MedicalEducationApplication {

  public static void main(String[] args) {
    SpringApplication.run(MedicalEducationApplication.class, args);
  }

}

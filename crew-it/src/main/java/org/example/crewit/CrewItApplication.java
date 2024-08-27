package org.example.crewit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@Slf4j
public class CrewItApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(CrewItApplication.class, args);

        } catch (Exception e) {
            log.debug("실행 에러 ::: "+e.getMessage());
        }
    }

}

package hu.progmasters.fundraiser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//TODO - Review:
// - AutoFormatot használjátok!
// - SonarLintet néha nézzetek!
// - Az IDEA kód analizálója néha egész más jellegű hibákat talál meg, azt is érdemes megnézni
// - Ugyanitt, az IDEA tud kód-duplikációkat keresni ( egész okosan )
@SpringBootApplication
@EnableScheduling
public class AngularFundraiserApplication {

    public static void main(String[] args) {
        SpringApplication.run(AngularFundraiserApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

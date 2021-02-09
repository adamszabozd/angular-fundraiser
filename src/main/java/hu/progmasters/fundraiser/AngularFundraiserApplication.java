package hu.progmasters.fundraiser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//TODO - Review: Bátran vegyetek fel újabb packageket, mert pl a DTO már átláthatatlan ( már most )
// és csoportosítsátok az összetartozó elemeket, erre egy másik/kiegészítő megoldás, ha például elkezditek
// feature-önként csoportosítani az osztályokat.

//TODO - Review: Ez mondjuk inkább szőrszálhasogatás, illetve az én saját rigolyám, de használjatok egységes code-stylet
// Ez nagyobb projektnél tud fejfájást okozni ( pl valakinél tab van, valakinél space, ezt pedig az IDEA minden
// commitnál oda vissza állítgatja... Ha ez egységes, akkor nem lesz ilyen gáz...
// Ennek a beállítását már egyszer mutattam (mondjuk lehet nem nektek... :D :D ) de szívesen megmutatom mégegyszer
@SpringBootApplication
public class AngularFundraiserApplication {

    public static void main(String[] args) {
        SpringApplication.run(AngularFundraiserApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

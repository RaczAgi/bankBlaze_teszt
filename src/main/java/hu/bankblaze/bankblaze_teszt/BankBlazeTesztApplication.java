package hu.bankblaze.bankblaze_teszt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BankBlazeTesztApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankBlazeTesztApplication.class, args);

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("alma");
        System.out.println(encodedPassword);
    }

}

package tech.mars.tengen.era;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author majunyang
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
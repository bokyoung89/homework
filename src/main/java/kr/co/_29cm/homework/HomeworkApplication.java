package kr.co._29cm.homework;

import kr.co._29cm.homework.presentation.controller.MainController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;

@SpringBootApplication
public class HomeworkApplication {

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = SpringApplication.run(HomeworkApplication.class, args);
        MainController controller = context.getBean(MainController.class);
        controller.start();
        BufferedReader bufferedReader = context.getBean(BufferedReader.class);
        bufferedReader.close();
    }
}

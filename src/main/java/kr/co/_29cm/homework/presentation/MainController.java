package kr.co._29cm.homework.presentation;

import kr.co._29cm.homework.presentation.view.ConsolePrinter;
import kr.co._29cm.homework.presentation.view.ItemPrinter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MainController {
    private final BufferedReader bufferedReader;
    private final ConsolePrinter consolePrinter;
    private final ItemPrinter itemPrinter;

    public void start() throws IOException {
        do {
            consolePrinter.start(); //입력 시작
            String input = bufferedReader.readLine();
            if(input.equals("q")){ //입력 종료
                consolePrinter.quit();
                break;
            }
            if(input.equals("o")){ //주문 시작
                itemPrinter.itemList();
            }
        } while(true);
    }
}

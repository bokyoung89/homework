package kr.co._29cm.homework.presentation.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ConsolePrinter {

    private final BufferedReader bufferedReader;

    public void start() {
        System.out.print("입력(o[order]: 주문, q[quit]: 종료) : ");
    }

    public void inputOrderId() throws IOException {
        System.out.print("상품번호 : ");
    }

    public void inputOrderQuantity() throws IOException {
        System.out.print("수량 : ");
    }

    public void quit() {
        System.out.print("고객님의 주문 감사합니다.");
    }

    public void wrongInput() {
        System.out.println("잘못된 입력입니다.");
    }
}

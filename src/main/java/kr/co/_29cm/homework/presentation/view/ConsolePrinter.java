package kr.co._29cm.homework.presentation.view;

import org.springframework.stereotype.Component;

@Component
public class ConsolePrinter {

    public void start() {
        System.out.print("입력(o[order]: 주문, q[quit]: 종료) : ");
    }

    public void quit() {
        System.out.print("고객님의 주문 감사합니다.");
    }
}

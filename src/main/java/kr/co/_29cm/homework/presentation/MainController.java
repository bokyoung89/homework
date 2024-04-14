package kr.co._29cm.homework.presentation;

import kr.co._29cm.homework.application.OrderService;
import kr.co._29cm.homework.exception.ItemNotFountException;
import kr.co._29cm.homework.exception.SoldOutException;
import kr.co._29cm.homework.presentation.dto.OrderItemRequestDto;
import kr.co._29cm.homework.presentation.view.ConsolePrinter;
import kr.co._29cm.homework.presentation.view.ItemPrinter;
import kr.co._29cm.homework.presentation.view.OrderPrinter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@RequiredArgsConstructor
public class MainController {
    private final BufferedReader bufferedReader;
    private final ConsolePrinter consolePrinter;
    private final ItemPrinter itemPrinter;
    private final OrderPrinter orderPrinter;
    private final OrderService orderService;

    public void start() throws IOException {
        do {
            consolePrinter.start(); // 입력 시작
            String input = bufferedReader.readLine();

            if (input.equals("q")) { // 입력 종료
                consolePrinter.quit();
                break;
            }

            if (input.equals("o")) { // 주문 시작
                List<OrderItemRequestDto> orderItems = receiveOrder();
                processOrder(orderItems);
            } else {
                consolePrinter.wrongInput();
            }
        } while (true);
    }

    private List<OrderItemRequestDto> receiveOrder() throws IOException {
        List<OrderItemRequestDto> orderItems = new CopyOnWriteArrayList<>();
        itemPrinter.itemList();

        while (true) {
            try {
                consolePrinter.inputOrderId(); // 상품 입력
                String itemIdInput = bufferedReader.readLine().trim();

                consolePrinter.inputOrderQuantity(); // 수량 입력
                String countInput = bufferedReader.readLine().trim();

                if (itemIdInput.isEmpty() || countInput.isEmpty()) {
                    break;
                }

                Long itemId = Long.valueOf(itemIdInput);
                int count = Integer.parseInt(countInput);
                orderItems.add(new OrderItemRequestDto(itemId, count));
            } catch (NumberFormatException e) {
                System.out.println("숫자가 아닌 값이 입력되었습니다. 다시 입력해주세요.");
            }
        }
        return orderItems;
    }

    private void processOrder(List<OrderItemRequestDto> orderItems) {
        try {
            // 상품번호와 수량으로 주문 생성
            Long orderId = orderService.order(orderItems.toArray(new OrderItemRequestDto[0]));

            // 주문 조회
            orderPrinter.printOrderResult(orderId);
        } catch (SoldOutException | ItemNotFountException e) {
            System.out.println(e.getMessage());
        }
    }
}

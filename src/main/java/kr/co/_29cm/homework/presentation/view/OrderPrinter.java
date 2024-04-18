package kr.co._29cm.homework.presentation.view;

import kr.co._29cm.homework.application.OrderService;
import kr.co._29cm.homework.domain.entity.OrderItem;
import kr.co._29cm.homework.presentation.dto.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class OrderPrinter {
    private final OrderService orderService;

    //무료 배송 금액
    private static final int FREE_DELIVERY_AMOUNT = 50000;

    public void printOrderResult(Long orderId) {
        OrderResponseDto order = orderService.getOrder(orderId);
        System.out.println("주문 내역 :");
        System.out.println("--------------------");

        List<OrderItem> orderItems = order.getOrderItems();
        if(!orderItems.isEmpty()) {
            for (OrderItem orderItem : orderItems) {
                System.out.println(orderItem.getItem().getName() + " - " + orderItem.getCount() + "개");
            }
        } else {
            System.out.println("주문 내역이 없습니다.");
        }
        System.out.println("--------------------");

        int totalPrice = order.getTotalPrice();
        System.out.println("주문금액: " + formatPrice(totalPrice) + "원");
        System.out.println("--------------------");

        /**
         * 주문 금액이 50000원 이하인 경우 배송비 display
         */
        int deliveryFee = order.getDeliveryFee();
        if (totalPrice < FREE_DELIVERY_AMOUNT && totalPrice > 0) {
            System.out.println("배송비: " + formatPrice(deliveryFee) + "원");
            System.out.println("--------------------");
        }

        if (totalPrice > 0) {
            System.out.println("지불금액: " + (formatPrice(totalPrice + deliveryFee)) + "원");
            System.out.println("--------------------");
        }
    }

    private String formatPrice(int price) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(price);
    }
}

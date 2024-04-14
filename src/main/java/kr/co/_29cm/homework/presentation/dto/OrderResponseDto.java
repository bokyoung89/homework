package kr.co._29cm.homework.presentation.dto;

import kr.co._29cm.homework.domain.entity.Order;
import kr.co._29cm.homework.domain.entity.OrderItem;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class OrderResponseDto {

    private List<OrderItem> orderItems = new ArrayList<>();

    private int totalPrice;

    public OrderResponseDto(Order entity) {
        this.orderItems = entity.getOrderItems();
        this.totalPrice = entity.getTotalPrice();
    }
}
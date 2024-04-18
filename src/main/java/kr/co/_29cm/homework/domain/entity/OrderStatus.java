package kr.co._29cm.homework.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderStatus {

    ORDER("주문");

    private final String OrderStatusText;
}

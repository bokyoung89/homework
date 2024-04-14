package kr.co._29cm.homework.presentation.dto;

import kr.co._29cm.homework.domain.entity.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class OrderRequestDto {

    private Long id;

    private int  stockQuantity;



}

package kr.co._29cm.homework.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderItemRequestDto {

    private Long itemId;
    private int count;

    public OrderItemRequestDto(Long itemId, int count) {
        this.itemId = itemId;
        this.count = count;
    }
}

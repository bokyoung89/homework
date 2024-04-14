package kr.co._29cm.homework.presentation.dto;

import kr.co._29cm.homework.domain.entity.Item;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class ItemResponseDto {
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    public ItemResponseDto(Item entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.price = entity.getPrice();
        this.stockQuantity = entity.getStockQuantity();
    }
}
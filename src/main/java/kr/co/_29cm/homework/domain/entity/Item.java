package kr.co._29cm.homework.domain.entity;

import jakarta.persistence.*;
import kr.co._29cm.homework.exception.SoldOutException;
import lombok.*;

import java.util.regex.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Item {
    @Id
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @Builder
    public Item(Long id, String name, int price, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    /**
     * 재고 증가
     */
    public void increaseStock(int count) {
        this.stockQuantity += count;
    }

    /**
     * 재고 감소
     */
    public void decreaseStock(int count, Long itemId) {
        int remainStock = this.stockQuantity - count;
        if (remainStock < 0) {
            throw new SoldOutException("SoldOutException 발생. 주문한 상품량이 재고량보다 큽니다. - 상품번호 : "  + itemId);
        }
        this.stockQuantity = remainStock;
    }
}

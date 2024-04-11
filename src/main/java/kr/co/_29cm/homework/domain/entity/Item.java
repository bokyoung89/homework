package kr.co._29cm.homework.domain.entity;

import jakarta.persistence.*;
import kr.co._29cm.homework.exception.SoldOutException;
import lombok.Getter;

@Getter
@Entity
public class Item {
    @Id
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    /**
     * 재고 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * 재고 감소
     */
    public void removeStock(int quantity) {
        int remainStock = this.stockQuantity - quantity;
        if (remainStock < 0) {
            throw new SoldOutException("SoldOutException 발생. 주문한 상품량이 재고량보다 큽니다.");
        }
        this.stockQuantity = remainStock;
    }
}

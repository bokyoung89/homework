package kr.co._29cm.homework.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int count;

    private int price;

    @Builder
    public OrderItem(Long id, Item item, Order order, int count, int price) {
        this.id = id;
        this.item = item;
        this.order = order;
        this.count = count;
        this.price = price;
    }

    public static OrderItem createOrderItem(Item item, int price, int count) {
        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .price(price)
                .count(count)
                .build();
        item.decreaseStock(count, item.getId());

        return orderItem;
    }

    public OrderItem updateOrder(Order order) {
        this.order = order;

        return this;
    }

    //주문상품 전체 가격 계산
    public int orderItemTotalPrice() {
        return getPrice() * getCount();
    }

    //주문 취소 시 재고 원복
    public void cancel() {
        getItem().increaseStock(count);
    }
}

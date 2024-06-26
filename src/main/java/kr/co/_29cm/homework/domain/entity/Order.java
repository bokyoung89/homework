package kr.co._29cm.homework.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "orders")
@SQLDelete(sql = "UPDATE orders SET deleted_at = NOW() WHERE order_id = ?")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    private int totalPrice;

    private OrderStatus orderStatus;

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    //배송비
    private int delivery_fee;

    @Builder
    public Order(Long id, List<OrderItem> orderItems, int totalPrice, OrderStatus orderStatus, LocalDateTime createdAt, LocalDateTime deletedAt, int delivery_fee) {
        this.id = id;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.delivery_fee = delivery_fee;
    }

    /**
     * 주문상품 생성
     */
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    /**
     * 주문 생성
     */
    public static Order createOrder(OrderItem... orderItems) {
        Order order = new Order();

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        order.setOrderStatus(OrderStatus.ORDER);
        order.setCreatedAt(LocalDateTime.now());
        order.calculateTotalPrice();

        return order;
    }

    /**
     * 전체 주문 금액
     */
    public int calculateTotalPrice() {
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.orderItemTotalPrice();
        }
        //주문 총액이 50,000원 이하일 때 배송비 컬럼 추가
        if (totalPrice < 50000) {
            delivery_fee = 2500;
        }
        return totalPrice;
    }
}

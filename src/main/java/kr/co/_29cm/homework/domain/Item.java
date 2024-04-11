package kr.co._29cm.homework.domain;

import jakarta.persistence.*;

@Entity
public class Item {
    @Id
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;
}

CREATE TABLE item
(
    item_id bigint NOT NULL PRIMARY KEY,
    name varchar(255) NOT NULL,
    price integer NOT NULL,
    stock_quantity integer NOT NULL
);

CREATE TABLE orders
(
    order_id bigint	NOT NULL AUTO_INCREMENT PRIMARY KEY,
    total_price integer	NOT NULL,
    order_status varchar(255) NOT NULL,
    created_At datetime NOT NULL,
    deleted_At datetime
);

CREATE TABLE order_item
(
    order_item_id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    item_id bigint NOT NULL,
    order_id bigint NOT NULL,
    count integer NOT NULL,
    price integer NOT NULL,
    FOREIGN KEY (item_id) REFERENCES item(item_id),
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);
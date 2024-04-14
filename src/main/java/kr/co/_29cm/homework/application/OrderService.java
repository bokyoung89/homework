package kr.co._29cm.homework.application;

import kr.co._29cm.homework.domain.entity.Item;
import kr.co._29cm.homework.domain.entity.Order;
import kr.co._29cm.homework.domain.entity.OrderItem;
import kr.co._29cm.homework.domain.repository.ItemRepository;
import kr.co._29cm.homework.domain.repository.OrderRepository;
import kr.co._29cm.homework.exception.ItemNotFountException;
import kr.co._29cm.homework.presentation.dto.OrderItemRequestDto;
import kr.co._29cm.homework.presentation.dto.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문 생성
     */
    @Transactional
     public Long order(OrderItemRequestDto... orderItemRequestDtos) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequestDto orderItemRequestDto : orderItemRequestDtos) {
            Item item = itemRepository.findByIdWithPessimisticLock(orderItemRequestDto.getItemId());

            if (item == null) {
                throw new ItemNotFountException("상품을 찾을 수 없습니다. 상품번호" + "(" + orderItemRequestDto.getItemId() + ")" + "를 확인해주세요.");
            }

            OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), orderItemRequestDto.getCount());
            orderItems.add(orderItem);
        }

        Order order = Order.createOrder(orderItems.toArray(new OrderItem[0]));
        orderRepository.saveAndFlush(order);

        return order.getId();
    }

    /**
     * 주문 조회
     */
    public OrderResponseDto getOrder(Long orderId) {
        Order entity = orderRepository.findById(orderId).orElseThrow(() ->
                new NoSuchElementException("주문을 찾을 수 없습니다."));

        return new OrderResponseDto(entity);
    }
}

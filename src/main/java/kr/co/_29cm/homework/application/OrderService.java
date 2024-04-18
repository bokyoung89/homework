package kr.co._29cm.homework.application;

import kr.co._29cm.homework.domain.entity.Item;
import kr.co._29cm.homework.domain.entity.Order;
import kr.co._29cm.homework.domain.entity.OrderItem;
import kr.co._29cm.homework.domain.repository.ItemRepository;
import kr.co._29cm.homework.domain.repository.OrderRepository;
import kr.co._29cm.homework.domain.exception.ItemNotFoundException;
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
     public Long placeOrder(List<OrderItemRequestDto> orderItemRequestDtos) {

        //주문상품 생성
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequestDto orderItemRequestDto : orderItemRequestDtos) {
            Item item = findItemOrThrowException(orderItemRequestDto.getItemId());

            OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), orderItemRequestDto.getCount());
            orderItems.add(orderItem);
        }

        //주문 생성
        Order order = Order.createOrder(orderItems.toArray(new OrderItem[0]));

        //주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 상품 엔티티 조회
     */
    private Item findItemOrThrowException(Long itemId) {
        return itemRepository.findByIdWithPessimisticLock(itemId).orElseThrow(() ->
            new ItemNotFoundException("상품을 찾을 수 없습니다. - 상품번호 : " + itemId));
        }

    /**
     * 주문 조회
     */
    public OrderResponseDto getOrder(Long orderId) {
        return new OrderResponseDto(orderRepository.findById(orderId).orElseThrow(() ->
                new NoSuchElementException("주문내역을 찾을 수 없습니다.")));
    }
}

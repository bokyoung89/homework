package kr.co._29cm.homework.application;

import kr.co._29cm.homework.domain.entity.Item;
import kr.co._29cm.homework.domain.entity.Order;
import kr.co._29cm.homework.domain.repository.ItemRepository;
import kr.co._29cm.homework.domain.repository.OrderRepository;
import kr.co._29cm.homework.domain.exception.ItemNotFoundException;
import kr.co._29cm.homework.domain.exception.SoldOutException;
import kr.co._29cm.homework.presentation.dto.OrderItemRequestDto;
import kr.co._29cm.homework.presentation.dto.OrderResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Test
    public void 한번에_여러개_주문_가능() {
        //given
        Long itemId1 = 768848L;
        Long itemId2 = 748943L;
        int count1 = 2;
        int count2 = 2;

        List<OrderItemRequestDto> orderItemRequestDtos = Arrays.asList(
                new OrderItemRequestDto(itemId1, count1),
                new OrderItemRequestDto(itemId2, count2)
        );

        //when
        Long orderId = orderService.placeOrder(orderItemRequestDtos);

        //then
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new NoSuchElementException("주문내역을 찾을 수 없습니다."));

        assertThat(order.getOrderItems().size()).isEqualTo(2);
    }

    @Test
    public void 주문_금액_5만원_미만인_경우_배송료_부과() {
        //given
        Long itemId = 768848L;
        int count = 1;
        int afterTotalPrice = 21000 + 2500; //상품가격 + 배송료(2500원)

        List<OrderItemRequestDto> orderItemRequestDtos = Arrays.asList(
                new OrderItemRequestDto(itemId, count)
        );

        //when
        Long orderId = orderService.placeOrder(orderItemRequestDtos);

        //then
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new NoSuchElementException("주문내역을 찾을 수 없습니다."));
        assertThat(order.getTotalPrice() + order.getDelivery_fee()).isEqualTo(afterTotalPrice);
    }

    @Test
    public void 주문_수량만큼_재고감소() {
        //given
        Long itemId = 768848L;
        int count = 10;
        int afterStockQuantity = 35; //주문 후 재고 : 45 - 10

        List<OrderItemRequestDto> orderItemRequestDtos = Arrays.asList(
                new OrderItemRequestDto(itemId, count)
        );

        //when
        orderService.placeOrder(orderItemRequestDtos);

        //then
        Item item = itemRepository.findByIdWithPessimisticLock(itemId).orElseThrow(() ->
                new ItemNotFoundException("상품을 찾을 수 없습니다. - 상품번호 : " + itemId));
        assertThat(item.getStockQuantity()).isEqualTo(afterStockQuantity);
    }

    @Test
    public void 재고가_부족한_경우_SoldOutException_발생() {
        //given
        Long itemId = 768848L;
        int count = 100;

        List<OrderItemRequestDto> orderItemRequestDtos = Arrays.asList(
                new OrderItemRequestDto(itemId, count)
        );

        //when & then
        assertThrows(SoldOutException.class, () -> orderService.placeOrder(orderItemRequestDtos));
    }

    @Test
    public void 주문_조회_성공() {
        //given
        Long itemId1 = 768848L;
        Long itemId2 = 748943L;
        int count1 = 2;
        int count2 = 2;

        List<OrderItemRequestDto> orderItemRequestDtos = Arrays.asList(
                new OrderItemRequestDto(itemId1, count1),
                new OrderItemRequestDto(itemId2, count2)
        );

        //when
        Long orderId = orderService.placeOrder(orderItemRequestDtos);
        OrderResponseDto order = orderService.getOrder(orderId);

        //then
        assertThat(order.getOrderItems()).hasSize(2);
    }

    @Test
    public void 멀티쓰레드_요청으로_99개중_99개_주문시_SoldOutException_정상_동작() throws InterruptedException {
        //given
        int threadCount = 99;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        Long itemId = 213341L;
        int count = 1;
        int afterStockQuantity = 0; //재고가 99개인 상품을 1개씩 99번 주문. 재고수량 : 99개 -> 0개

        List<OrderItemRequestDto> orderItemRequestDtos = Arrays.asList(
                new OrderItemRequestDto(itemId, count)
        );

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    orderService.placeOrder(orderItemRequestDtos);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        Item item = itemRepository.findById(itemId).orElseThrow();
        //99 - (1 * 99) = 0
        assertThat(item.getStockQuantity()).isEqualTo(afterStockQuantity);
    }
}
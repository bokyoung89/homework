package kr.co._29cm.homework.application;

import kr.co._29cm.homework.domain.entity.Item;
import kr.co._29cm.homework.domain.entity.Order;
import kr.co._29cm.homework.domain.repository.ItemRepository;
import kr.co._29cm.homework.domain.repository.OrderRepository;
import kr.co._29cm.homework.exception.SoldOutException;
import kr.co._29cm.homework.presentation.dto.OrderItemRequestDto;
import kr.co._29cm.homework.presentation.dto.OrderResponseDto;
import org.glassfish.jaxb.core.v2.TODO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import java.util.Optional;
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
    public void 한번에_여러개_주문_성공() {
        //given
        Long itemId1 = 768848L;
        Long itemId2 = 748943L; // Assuming another item ID
        int count1 = 2;
        int count2 = 2;

        //when
        Long orderId = orderService.order(
                new OrderItemRequestDto(itemId1, count1),
                new OrderItemRequestDto(itemId2, count2)
        );

        //then
        Optional<Order> order = orderRepository.findById(orderId);
        assertThat(order).isPresent();
    }

    @Test
    public void 주문_조회() {
        //given
        Long itemId1 = 768848L;
        Long itemId2 = 748943L; // Assuming another item ID
        int count1 = 2;
        int count2 = 2;

        //when
        Long orderId = orderService.order(
                new OrderItemRequestDto(itemId1, count1),
                new OrderItemRequestDto(itemId2, count2)
        );

        //when
        OrderResponseDto order = orderService.getOrder(orderId);

        //then
        assertThat(order.getOrderItems()).hasSize(2);
    }

    @Test
    public void 멀티쓰레드_요청으로_SoldOutException_정상_동작() throws InterruptedException {
        //given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        Long itemId = 213341L;
        int count = 1;

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    orderService.order(new OrderItemRequestDto(itemId, count));
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        Item item = itemRepository.findById(itemId).orElseThrow();
        assertThat(item.getStockQuantity()).isEqualTo(0);
        //TODO : soldoutexception 확인
    }
}
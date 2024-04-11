package kr.co._29cm.homework.application;

import kr.co._29cm.homework.domain.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired ItemService itemService;
    @Autowired ItemRepository itemRepository;

    @Test
    public void 상품이_존재하지_않을_경우_예외발생() {
        //given
        Long itemId = 100L;

        //when & then
        assertThrows(NoSuchElementException.class, () -> itemService.findOne(itemId));
    }
}
package kr.co._29cm.homework.application;

import kr.co._29cm.homework.domain.repository.ItemRepository;
import kr.co._29cm.homework.domain.exception.ItemNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
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
        assertThrows(ItemNotFoundException.class, () -> itemService.getOneItem(itemId));
    }
}
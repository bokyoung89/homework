package kr.co._29cm.homework.application;

import kr.co._29cm.homework.domain.entity.Item;
import kr.co._29cm.homework.domain.repository.ItemRepository;
import kr.co._29cm.homework.presentation.dto.ItemResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemResponseDto findOne(Long itemId) {
        Item entity = itemRepository.findById(itemId).orElseThrow(() ->
                new NoSuchElementException("상품이 존재하지 않습니다."));

        return new ItemResponseDto(entity);
    }

    public List<ItemResponseDto> findAll() {
        return itemRepository.findAllDesc().stream()
                .map(ItemResponseDto::new)
                .collect(Collectors.toList());
    }
}

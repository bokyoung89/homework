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

    /**
     * 상품 단건 조회
     */
    public ItemResponseDto findOne(Long itemId) {
        Item entity = itemRepository.findById(itemId).orElseThrow(() ->
                new NoSuchElementException("상품을 찾지 못했습니다."));

        return new ItemResponseDto(entity);
    }

    /**
     * 상품 전체 조회
     */
    public List<ItemResponseDto> findAll() {
        return itemRepository.findAllDesc().stream()
                .map(ItemResponseDto::new)
                .collect(Collectors.toList());
    }
}

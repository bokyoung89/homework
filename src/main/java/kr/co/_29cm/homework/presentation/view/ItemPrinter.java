package kr.co._29cm.homework.presentation.view;

import kr.co._29cm.homework.application.ItemService;
import kr.co._29cm.homework.presentation.dto.ItemResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemPrinter {

    private final ItemService itemService;

    private static final String ITEM_FORMAT = "%-10s%-50s%-10s%-10s\n";

    public void itemList() {
        List<ItemResponseDto> items = itemService.getAllItem();
        printItemHeader(); // 헤더 출력
        for(ItemResponseDto item : items) {
            printItem(item); // 상품 정보 출력
        }
    }

    private void printItemHeader() {
        System.out.printf(ITEM_FORMAT, "상품번호", "상품명", "가격", "재고수량");
    }

    private void printItem(ItemResponseDto item) {
        System.out.printf(ITEM_FORMAT, item.getId(), item.getName(), item.getPrice(), item.getStockQuantity());
    }


}

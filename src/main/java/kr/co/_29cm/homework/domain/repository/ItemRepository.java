package kr.co._29cm.homework.domain.repository;

import kr.co._29cm.homework.domain.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i ORDER BY i.id DESC")
    List<Item> findAllDesc();
}

package kr.co._29cm.homework.domain.repository;

import jakarta.persistence.LockModeType;
import kr.co._29cm.homework.domain.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i ORDER BY i.id DESC")
    List<Item> findAllDesc();

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Item i WHERE i.id = :id")
    Optional<Item> findByIdWithPessimisticLock(@Param("id") Long id);
}

package ru.practicum.shareit.request.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    @Query(" select ir from ItemRequest ir where ir.requestor.id = ?1 order by ir.created desc")
    List<ItemRequest> findAllByUserId(long userId);

    @Query("select ir from ItemRequest ir where ir.requestor.id <> ?1")
    List<ItemRequest> findOtherRequestor(Long userId, Pageable pageable);
}

package com.mate.academy.demo.repository;

import com.mate.academy.demo.model.OrderItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query(value = "from OrderItem itm join fetch itm.order o"
            + " join fetch o.user u"
            + " join fetch itm.book bk"
            + " where o.id = :orderId"
            + " and u.id = :userId"
            + " and itm.id = :itemId")
    Optional<OrderItem> findByIdAndOrderIdAndUserid(Long userId, Long orderId, Long itemId);
}

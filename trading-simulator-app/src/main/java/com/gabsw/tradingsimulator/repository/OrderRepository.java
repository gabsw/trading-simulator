package com.gabsw.tradingsimulator.repository;

import com.gabsw.tradingsimulator.domain.enums.OrderType;
import com.gabsw.tradingsimulator.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    @Query("""
        SELECT o FROM OrderEntity o
        WHERE o.instrument.id = :instrumentId
          AND o.type = :oppositeType
          AND o.status IN ('NEW', 'PARTIALLY_FILLED')
          AND (
            (:incomingType = 'BUY' AND o.price <= :price)
            OR
            (:incomingType = 'SELL' AND o.price >= :price)
          )
        ORDER BY o.price ASC, o.createdAt ASC
    """)
    List<OrderEntity> findMatchingOrders(
            @Param("instrumentId") UUID instrumentId,
            @Param("oppositeType") OrderType oppositeType,
            @Param("incomingType") String incomingType,
            @Param("price") BigDecimal price
    );

    @Query("""
    SELECT o FROM OrderEntity o
    WHERE o.instrument.id = :instrumentId
      AND o.type = :oppositeType
      AND o.status IN ('NEW', 'PARTIALLY_FILLED')
      AND o.price <= :price
    ORDER BY o.price ASC, o.createdAt ASC
""")
    List<OrderEntity> findSellOrdersAtOrBelowPrice(
            @Param("instrumentId") UUID instrumentId,
            @Param("oppositeType") OrderType oppositeType,
            @Param("price") BigDecimal price
    );

    @Query("""
    SELECT o FROM OrderEntity o
    WHERE o.instrument.id = :instrumentId
      AND o.type = :oppositeType
      AND o.status IN ('NEW', 'PARTIALLY_FILLED')
      AND o.price >= :price
    ORDER BY o.price ASC, o.createdAt ASC
""")
    List<OrderEntity> findBuyOrdersAtOrAbovePrice(
            @Param("instrumentId") UUID instrumentId,
            @Param("oppositeType") OrderType oppositeType,
            @Param("price") BigDecimal price
    );

}

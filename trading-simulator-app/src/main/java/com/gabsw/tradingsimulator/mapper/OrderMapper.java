package com.gabsw.tradingsimulator.mapper;

import com.gabsw.tradingsimulator.dto.OrderDTO;
import com.gabsw.tradingsimulator.domain.Order;
import com.gabsw.tradingsimulator.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    // DTO <-> Domain
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "NEW")
    Order toDomain(OrderDTO dto);

    OrderDTO toDto(Order order);

    List<Order> toDomainList(List<OrderDTO> dtoList);
    List<OrderDTO> toDtoList(List<Order> domainList);

    // Entity -> Domain
    @Mapping(source = "instrument.ticker", target = "ticker")
    @Mapping(source = "price", target = "price") // BigDecimal -> double
    Order toDomain(OrderEntity entity);

    // Domain -> Entity
    @Mapping(source = "price", target = "price") // double -> BigDecimal
    @Mapping(target = "instrument", ignore = true)
    @Mapping(target = "createdAt", expression = "java(System.currentTimeMillis())")
    @Mapping(target = "updatedAt", expression = "java(System.currentTimeMillis())")
    OrderEntity toEntity(Order domain);

    default BigDecimal map(double value) {
        return BigDecimal.valueOf(value);
    }

    default double map(BigDecimal value) {
        return value.doubleValue();
    }
}


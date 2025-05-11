package com.gabsw.tradingsimulator.mapper;

import com.gabsw.tradingsimulator.dto.OrderDTO;
import com.gabsw.tradingsimulator.model.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toEntity(OrderDTO dto);
    OrderDTO toDto(Order order);

    List<Order> toEntityList(List<OrderDTO> dtoList);
    List<OrderDTO> toDtoList(List<Order> entityList);
}

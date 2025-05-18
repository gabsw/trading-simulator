package com.gabsw.tradingsimulator.repository;

import com.gabsw.tradingsimulator.entity.TradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TradeRepository extends JpaRepository<TradeEntity, UUID> {
}

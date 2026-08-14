package com.duntalk.domain.item.repository;

import com.duntalk.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface ItemRepository extends JpaRepository<Item, String> {
    boolean existsByItemName(String itemName);

    Optional<Item> findByItemName(String itemName);

    List<Item> findByItemNameContaining(String itemName);

    List<Item> findTop20ByItemNameContaining(String itemName);
}

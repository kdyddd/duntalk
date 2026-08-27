package com.duntalk.domain.item.repository;

import com.duntalk.domain.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ItemRepository extends JpaRepository<Item, String> {
    boolean existsByItemName(String itemName);

    Optional<Item> findByItemName(String itemName);

    Page<Item> findByItemNameContainingOrderByItemNameAsc(String itemName, Pageable pageable);

    List<Item> findTop20ByItemNameContaining(String itemName);
}

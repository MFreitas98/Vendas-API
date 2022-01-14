package io.github.marcelofreitas.domain.repository;

import io.github.marcelofreitas.domain.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItensPedido extends JpaRepository<ItemPedido, Integer> {
}

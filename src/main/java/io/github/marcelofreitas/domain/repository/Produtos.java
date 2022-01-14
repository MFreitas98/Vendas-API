package io.github.marcelofreitas.domain.repository;

import io.github.marcelofreitas.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Produtos extends JpaRepository<Produto,Integer> {

}

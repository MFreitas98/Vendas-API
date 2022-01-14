package io.github.marcelofreitas.domain.repository;

import io.github.marcelofreitas.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface Clientes extends JpaRepository<Cliente, Integer> {

    @Query(value = "select * from Cliente c where c.nome like '%:nome%' ", nativeQuery = true)
    List<Cliente> findByNomeLike(@Param("nome") String nome);

    /*List<Cliente> findByNomeOrId(String nome, String id);*/

    boolean existsByNome(String nome);

    @Query(" select c from Cliente c left join fetch c.pedidos where c.id = :id")
    Cliente findClientFetchPedidos(@Param("id") Integer id);



}

package io.github.marcelofreitas.service;

import io.github.marcelofreitas.domain.entity.Pedido;
import io.github.marcelofreitas.domain.enums.StatusPedido;
import io.github.marcelofreitas.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {

    Pedido salvar(PedidoDTO dto);

    void atualizaStatus(Integer id, StatusPedido statusPedido);

    Optional<Pedido> obterPedidoCompleto(Integer id);
}

package io.github.marcelofreitas.service.impl;


import io.github.marcelofreitas.domain.entity.Cliente;
import io.github.marcelofreitas.domain.entity.ItemPedido;
import io.github.marcelofreitas.domain.entity.Pedido;
import io.github.marcelofreitas.domain.entity.Produto;
import io.github.marcelofreitas.domain.enums.StatusPedido;
import io.github.marcelofreitas.domain.repository.Clientes;
import io.github.marcelofreitas.domain.repository.ItensPedido;
import io.github.marcelofreitas.domain.repository.Pedidos;
import io.github.marcelofreitas.domain.repository.Produtos;
import io.github.marcelofreitas.exception.PedidoNaoEncontradoException;
import io.github.marcelofreitas.exception.RegraNegocioException;
import io.github.marcelofreitas.rest.dto.ItemPedidoDTO;
import io.github.marcelofreitas.rest.dto.PedidoDTO;
import io.github.marcelofreitas.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final Pedidos repository;
    private final Clientes clientesRepository;
    private final Produtos produtosRepository;
    private final ItensPedido itensPedidoRepository;


    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Integer idCliente = dto.getCliente();
        Cliente cliente = clientesRepository
                .findById(idCliente)
                .orElseThrow( () -> new RegraNegocioException("Codigo de cliente invalido."));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itensPedido = converterItens(pedido,dto.getItens());
        repository.save(pedido);
        itensPedidoRepository.saveAll(itensPedido);
        pedido.setItens(itensPedido);
        return pedido;
    }

    @Override
    @Transactional
    public void atualizaStatus(Integer id, StatusPedido statusPedido) {
        repository
                .findById(id)
                .map( pedido -> {
                    pedido.setStatus(statusPedido);
                    return repository.save(pedido);
                }).orElseThrow( () -> new PedidoNaoEncontradoException() );
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return repository.findByIdFetchItens(id);
    }


    private List<ItemPedido> converterItens(Pedido pedido, List<ItemPedidoDTO> itens)  {
        if(itens.isEmpty()) {
            throw  new RegraNegocioException("Nao e possivel realizar um pedido sem itens.");
        }

        return itens
                .stream()
                .map( dto -> {
                    Integer idProduto= dto.getProduto();
                  Produto produto =  produtosRepository
                            .findById(idProduto)
                            .orElseThrow(
                                    () -> new RegraNegocioException("Codigo de protduto invalido. "
                                     + idProduto)
                            );

                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);
                    return itemPedido;
                }).collect(Collectors.toList());

    }
}

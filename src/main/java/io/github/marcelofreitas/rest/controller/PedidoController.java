package io.github.marcelofreitas.rest.controller;

import io.github.marcelofreitas.domain.entity.ItemPedido;
import io.github.marcelofreitas.domain.entity.Pedido;
import io.github.marcelofreitas.domain.enums.StatusPedido;
import io.github.marcelofreitas.rest.dto.AtualizacaoStatusPedidoDTO;
import io.github.marcelofreitas.rest.dto.InformacaoItemPedidoDTO;
import io.github.marcelofreitas.rest.dto.InformacoesPedidoDTO;
import io.github.marcelofreitas.rest.dto.PedidoDTO;
import io.github.marcelofreitas.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer save(@RequestBody @Valid PedidoDTO dto) {
        Pedido pedido = service.salvar(dto);
        return  pedido.getId();

    }

    @GetMapping("/get/{id}")
   public InformacoesPedidoDTO getById(@PathVariable Integer id) {
         return service
                 .obterPedidoCompleto(id)
                 .map(  p -> conveter(p))
                 .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido nao encontrado." ));
   }


   @PatchMapping(path = "{id}")
   @ResponseStatus(HttpStatus.NO_CONTENT)
   public void updateStatus(@PathVariable Integer id,
                            @RequestBody @Valid AtualizacaoStatusPedidoDTO dto) {
       String novoStatus = dto.getNovoStatus();
       service.atualizaStatus(id, StatusPedido.valueOf(novoStatus));

   }

   private  InformacoesPedidoDTO conveter(Pedido pedido) {
         return  InformacoesPedidoDTO
                 .builder()
                .codigo(pedido.getId())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .cpf(pedido.getCliente().getCpf())
                .nomeCliente(pedido.getCliente().getNome())
                .total(pedido.getTotal())
                 .status(pedido.getStatus().name())
                .itens(conveter(pedido.getItens()))
                .build();


   }

   private List<InformacaoItemPedidoDTO> conveter(List<ItemPedido> itens) {
        if(CollectionUtils.isEmpty(itens)) {
            return Collections.emptyList();
        }

        return itens.stream().map(
                item -> InformacaoItemPedidoDTO
                        .builder().descricaoProduto(item.getProduto().getDescricao())
                        .precoUnitario(item.getProduto().getPreco())
                        .build()
        ).collect(Collectors.toList());

   }

}

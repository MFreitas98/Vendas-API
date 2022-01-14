package io.github.marcelofreitas.rest.controller;

import io.github.marcelofreitas.domain.entity.Cliente;
import io.github.marcelofreitas.domain.entity.Produto;
import io.github.marcelofreitas.domain.repository.Produtos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {


    private Produtos produtos;

    @Autowired
    public ProdutoController(Produtos produtos) {
        this.produtos = produtos;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produto save(@RequestBody @Valid Produto produto) {
        return produtos.save(produto);
    }

   @GetMapping
   public List<Produto> find(Produto filtro) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING);
        Example example = Example.of(filtro,matcher);
        return produtos.findAll(example);
   }


    @GetMapping("/{id}")
    public Produto getProdutoById(@PathVariable Integer id) {
        return produtos
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto nao encontrado."));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update( @PathVariable Integer id, @RequestBody @Valid Produto produto ){
        produtos
                .findById(id)
                .map( p -> {
                    produto.setId(p.getId());
                    produtos.save(produto);
                    return produto;
                }).orElseThrow( () ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Produto não encontrado."));
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        produtos.findById(id)
                .map( produto -> {
                    produtos.delete(produto);
                    return produto;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto nao encontrado."));
    }

}




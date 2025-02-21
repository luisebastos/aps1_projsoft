package com.example.demo.produto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produto")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    // Criar novo produto
    @PostMapping
    public Produto criarProduto(@RequestBody Produto produto) {
        return produtoService.criarProduto(produto);
    }

    // Consultar se um produto existe pelo ID
    @GetMapping("/{id}")
    public Produto buscarProduto(@PathVariable String id) {
        return produtoService.buscarProdutoPorId(id);
    }

    // Diminuir quantidade no estoque
    @PutMapping("/{id}/diminuir/{quantidade}")
    public void diminuirEstoque(@PathVariable String id, @PathVariable Integer quantidade) {
        produtoService.diminuirQuantidadeEstoque(id, quantidade);
    }

    // Listar todos os produtos disponíveis
    @GetMapping
    public List<Produto> listarProdutos() {
        return produtoService.listarProdutos();
    }
}


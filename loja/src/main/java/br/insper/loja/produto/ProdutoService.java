package br.insper.loja.produto;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.client.RestTemplate;

@Service
public class ProdutoService {

    private final RestTemplate restTemplate;

    public ProdutoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Produto getProduto(String id) {
        try {
            return restTemplate
                    .getForEntity("http://54.232.246.117:8082/api/produto/" + id, Produto.class)
                    .getBody();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");
        }
    }

    public boolean temEstoqueSuficiente(int quantidade, String id) {
        Produto produto = getProduto(id);
        return produto.getQuantidadeEstoque() >= quantidade;
    }

    public void reduzirEstoque(int quantidade, String id) {
        Produto produto = getProduto(id);

        if (produto.getQuantidadeEstoque() < quantidade) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estoque insuficiente");
        }

        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - quantidade);
        atualizarProduto(produto);
    }

    public Produto atualizarProduto(Produto produto) {
        try {
            return restTemplate
                    .postForObject("http://54.232.246.117:8082/api/produto/atualizar", produto, Produto.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao atualizar o produto");
        }
    }
}


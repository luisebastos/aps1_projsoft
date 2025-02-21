package br.insper.loja.compra;

import br.insper.loja.evento.EventoService;
import br.insper.loja.produto.Produto;
import br.insper.loja.produto.ProdutoService;
import br.insper.loja.usuario.Usuario;
import br.insper.loja.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private EventoService eventoService;

    public Compra salvarCompra(Compra compra) {
        Usuario usuario = usuarioService.getUsuario(compra.getUsuario());

        for (Produto produto : compra.getProdutos()) {
            Produto produtoBanco = produtoService.getProduto(produto.getId());
            if (produtoBanco == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto com ID " + produto.getId() + " não encontrado.");
            }
            if (!produtoBanco.temEstoqdueSuficiente(produto.getQuantidadeEstoque())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estoque insuficiente para o produto: " + produtoBanco.getNome());
            }
            // Atualizar estoque
            produtoBanco.reduzirEstoque(produto.getQuantidadeEstoque());
            produtoService.atualizarProduto(produtoBanco);
        }
        compra.setNome(usuario.getNome());
        compra.setDataCompra(LocalDateTime.now());

        eventoService.salvarEvento(usuario.getEmail(), "Compra realizada");
        return compraRepository.save(compra);
    }

    public List<Compra> getCompras() {
        return compraRepository.findAll();
    }
}


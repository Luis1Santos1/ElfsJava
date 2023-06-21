package br.com.elfs.loja.dao;

import java.util.List;

import br.com.elfs.loja.modelo.Pagamento;

public interface PagamentoIDAO {
    void cadastrar(Pagamento pagamento);
    Pagamento buscarPorId(Long id);
    List<Pagamento> buscarTodos();
    void atualizar(Pagamento pagamento);
    void excluir(Pagamento pagamento);
}

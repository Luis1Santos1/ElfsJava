package br.com.elfs.loja.dao;

import java.util.List;

import br.com.elfs.loja.modelo.Tipo;

public interface TipoIDAO {
    void cadastrar(Tipo tipo);
    Tipo buscarPorId(Long id);
    Tipo buscarPorNome(String nome);
    List<Tipo> buscarTodos();
    void atualizar(Tipo tipo);
    void remover(Tipo tipo);
}

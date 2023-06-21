package br.com.elfs.loja.dao;

import java.util.List;
import br.com.elfs.loja.modelo.Camisa;

public interface CamisaIDAO {

    void cadastrar(Camisa camisa);

    Camisa buscarPorId(Long id);

    List<Camisa> buscarTodos();

    List<Camisa> buscarPorNome(String nome);

    List<Camisa> buscarPorTipo(String tipo);

    void atualizar(Camisa camisa);

    void excluir(Camisa camisa);
}

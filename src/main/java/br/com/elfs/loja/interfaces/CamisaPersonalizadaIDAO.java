package br.com.elfs.loja.interfaces;

import java.util.List;
import br.com.elfs.loja.modelo.CamisaPersonalizada;

public interface CamisaPersonalizadaIDAO {

    void cadastrar(CamisaPersonalizada camisaPersonalizada);

    CamisaPersonalizada buscarPorId(Long id);

    List<CamisaPersonalizada> buscarTodos();

    List<CamisaPersonalizada> buscarPorNome(String nome);

    List<CamisaPersonalizada> buscarPorTipo(String tipo);

    void atualizar(CamisaPersonalizada camisaPersonalizada);

    void excluir(CamisaPersonalizada camisaPersonalizada);
}

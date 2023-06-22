package br.com.elfs.loja.interfaces;

import java.util.List;

import br.com.elfs.loja.modelo.Usuario;

public interface UsuarioIDAO {

    void cadastrar(Usuario usuario);

    Usuario buscarPorId(Long id);

    List<Usuario> buscarTodos();

    Usuario buscarUsuarioLogado();

    Usuario buscarPorNomeUsuario(String nomeUsuario);

    boolean verificarCredenciais(String nomeUsuario, String senha);

    void atualizar(Usuario usuario);

    void excluir(Usuario usuario);

}
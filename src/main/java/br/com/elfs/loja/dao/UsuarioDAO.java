package br.com.elfs.loja.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.elfs.loja.modelo.Usuario;
import br.com.elfs.loja.util.JPAUtil;
import br.com.elfs.loja.util.PasswordUtil;

public class UsuarioDAO {

    private EntityManager em;

    public UsuarioDAO(EntityManager em) {
        this.em = em;
    }

    public void cadastrar(Usuario usuario) {
        this.em.persist(usuario);
    }

    public Usuario buscarPorId(Long id) {
        return em.find(Usuario.class, id);
    }

    public Usuario buscarPorNomeUsuario(String nomeUsuario) {
        TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.nomeUsuario = :nomeUsuario",
                Usuario.class);
        query.setParameter("nomeUsuario", nomeUsuario);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public boolean verificarCredenciais(String nomeUsuario, String senha) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Usuario usuario = buscarPorNomeUsuario(nomeUsuario);
            if (usuario != null) {
                // Verifica se a senha é válida utilizando BCrypt
                if (PasswordUtil.verificarSenha(senha, usuario.getSenha())) {
                    return true;
                }
            }
        } catch (IllegalArgumentException e) {
            // Redefine a senha do usuário se ocorrer uma exceção
            Usuario usuario = buscarPorNomeUsuario(nomeUsuario);
            usuario.setSenha(PasswordUtil.criptografarSenha(senha));
            em.getTransaction().begin();
            em.merge(usuario);
            em.getTransaction().commit();
            return true;
        } finally {
            em.close();
        }
        return false;
    }

}

package br.com.elfs.loja.dao;


import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import br.com.elfs.loja.interfaces.UsuarioIDAO;
import br.com.elfs.loja.modelo.Usuario;
import br.com.elfs.loja.util.JPAUtil;
import br.com.elfs.loja.util.PasswordUtil;
import java.util.List;

public class UsuarioDAO implements UsuarioIDAO {

    private EntityManager em;

    public UsuarioDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public void cadastrar(Usuario usuario) {
        this.em.persist(usuario);
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return em.find(Usuario.class, id);
    }

    @Override
    public List<Usuario> buscarTodos() {
        String jpql = "SELECT u FROM Usuario u";
        TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);
        return query.getResultList();
    }

    @Override
    public Usuario buscarUsuarioLogado() {
        TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.logado = true", Usuario.class);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
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

    @Override
    public boolean verificarCredenciais(String nomeUsuario, String senha) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Usuario usuario = buscarPorNomeUsuario(nomeUsuario);
            if (usuario != null) {
                if (PasswordUtil.verificarSenha(senha, usuario.getSenha())) {
                    return true;
                }
            }
        } catch (IllegalArgumentException e) {
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

    @Override
    public void atualizar(Usuario usuario) {
        em.getTransaction().begin();
        em.merge(usuario);
        em.getTransaction().commit();
    }

    @Override
    public void excluir(Usuario usuario) {
        em.getTransaction().begin();
        em.remove(usuario);
        em.getTransaction().commit();
    }
}

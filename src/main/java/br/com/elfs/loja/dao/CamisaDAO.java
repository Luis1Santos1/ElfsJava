package br.com.elfs.loja.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.elfs.loja.modelo.Camisa;

public class CamisaDAO {

    private EntityManager em;

    public CamisaDAO(EntityManager em) {
        this.em = em;
    }

    public void cadastrar(Camisa camisa) {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
        em.persist(camisa);
        em.getTransaction().commit();
    }

    public Camisa buscarPorId(Long id) {
        return em.find(Camisa.class, id);
    }

    public List<Camisa> buscarTodos() {
        String jpql = "SELECT c FROM Camisa c";
        return em.createQuery(jpql, Camisa.class).getResultList();
    }

    public List<Camisa> buscarPorNome(String nome) {
        String jpql = "SELECT c FROM Camisa c WHERE c.nome = :nome";
        return em.createQuery(jpql, Camisa.class).setParameter("nome", nome).getResultList();
    }

    public List<Camisa> buscarPorTipo(String tipo) {
        String jpql = "SELECT c FROM Camisa c WHERE c.tipo.nome = :tipo";
        return em.createQuery(jpql, Camisa.class).setParameter("tipo", tipo).getResultList();
    }

    public void atualizar(Camisa camisa) {
        em.getTransaction().begin();
        em.merge(camisa);
        em.getTransaction().commit();
    }

    public void excluir(Camisa camisa) {
        em.getTransaction().begin();
        em.remove(camisa);
        em.getTransaction().commit();
    }
}

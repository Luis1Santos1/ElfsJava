package br.com.elfs.loja.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.elfs.loja.modelo.Camisa;


public class CamisaDAO {

    private EntityManager em;

    public CamisaDAO(EntityManager em) {
        this.em = em;
    }

    public void cadastrar(Camisa camisa) {
        this.em.persist(camisa);
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

    public List<Camisa> buscarPorNomeDaCategoria(String nome) {
        String jpql = "SELECT c FROM Camisa c WHERE c.tipo.nome = :nome";
        return em.createQuery(jpql, Camisa.class).setParameter("nome", nome).getResultList();
    }
    
}

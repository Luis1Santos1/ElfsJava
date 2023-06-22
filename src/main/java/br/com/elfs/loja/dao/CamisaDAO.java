package br.com.elfs.loja.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import br.com.elfs.loja.interfaces.CamisaIDAO;
import br.com.elfs.loja.modelo.Camisa;

public class CamisaDAO implements CamisaIDAO {

    private EntityManager em;

    public CamisaDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public void cadastrar(Camisa camisa) {
            em.persist(camisa);
    }

    @Override
    public Camisa buscarPorId(Long id) {
        return em.find(Camisa.class, id);
    }

    @Override
    public List<Camisa> buscarTodos() {
        String jpql = "SELECT c FROM Camisa c";
        TypedQuery<Camisa> query = em.createQuery(jpql, Camisa.class);
        return query.getResultList();
    }

    @Override
    public List<Camisa> buscarPorNome(String nome) {
        String jpql = "SELECT c FROM Camisa c WHERE c.nome = :nome";
        TypedQuery<Camisa> query = em.createQuery(jpql, Camisa.class);
        query.setParameter("nome", nome);
        return query.getResultList();
    }

    @Override
    public List<Camisa> buscarPorTipo(String tipo) {
        String jpql = "SELECT c FROM Camisa c WHERE c.tipo.nome = :tipo";
        TypedQuery<Camisa> query = em.createQuery(jpql, Camisa.class);
        query.setParameter("tipo", tipo);
        return query.getResultList();
    }

    @Override
    public void atualizar(Camisa camisa) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(camisa);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public void excluir(Camisa camisa) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.remove(camisa);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
}

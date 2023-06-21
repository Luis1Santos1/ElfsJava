package br.com.elfs.loja.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import br.com.elfs.loja.modelo.CamisaPersonalizada;

public class CamisaPersonalizadaDAO implements CamisaIDAO {

    private EntityManager em;

    public CamisaPersonalizadaDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public void cadastrar(CamisaPersonalizada camisaPersonalizada) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(camisaPersonalizada);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public CamisaPersonalizada buscarPorId(Long id) {
        return em.find(CamisaPersonalizada.class, id);
    }

    @Override
    public List<CamisaPersonalizada> buscarTodos() {
        String jpql = "SELECT c FROM CamisaPersonalizada c";
        TypedQuery<CamisaPersonalizada> query = em.createQuery(jpql, CamisaPersonalizada.class);
        return query.getResultList();
    }

    @Override
    public List<CamisaPersonalizada> buscarPorNome(String nome) {
        String jpql = "SELECT c FROM CamisaPersonalizada c WHERE c.nome = :nome";
        TypedQuery<CamisaPersonalizada> query = em.createQuery(jpql, CamisaPersonalizada.class);
        query.setParameter("nome", nome);
        return query.getResultList();
    }

    @Override
    public List<CamisaPersonalizada> buscarPorTipo(String tipo) {
        String jpql = "SELECT c FROM CamisaPersonalizada c WHERE c.tipo.nome = :tipo";
        TypedQuery<CamisaPersonalizada> query = em.createQuery(jpql, CamisaPersonalizada.class);
        query.setParameter("tipo", tipo);
        return query.getResultList();
    }

    @Override
    public void atualizar(CamisaPersonalizada camisaPersonalizada) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(camisaPersonalizada);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public void excluir(CamisaPersonalizada camisaPersonalizada) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.remove(camisaPersonalizada);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
}

package br.com.elfs.loja.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.elfs.loja.interfaces.PagamentoIDAO;
import br.com.elfs.loja.modelo.Pagamento;

public class PagamentoDAO implements PagamentoIDAO {
    private EntityManager em;

    public PagamentoDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public void cadastrar(Pagamento pagamento) {
        em.persist(pagamento);
    }

    @Override
    public Pagamento buscarPorId(Long id) {
        return em.find(Pagamento.class, id);
    }

    @Override
    public List<Pagamento> buscarTodos() {
        TypedQuery<Pagamento> query = em.createQuery("SELECT p FROM Pagamento p", Pagamento.class);
        return query.getResultList();
    }

    @Override
    public void atualizar(Pagamento pagamento) {
        em.merge(pagamento);
    }

    @Override
    public void excluir(Pagamento pagamento) {
        em.remove(pagamento);
    }
}

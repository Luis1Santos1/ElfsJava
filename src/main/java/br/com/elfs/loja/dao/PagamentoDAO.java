package br.com.elfs.loja.dao;

import javax.persistence.EntityManager;

import br.com.elfs.loja.modelo.Pagamento;

public class PagamentoDAO {
    private EntityManager em;

    public PagamentoDAO(EntityManager em) {
        this.em = em;
    }

    public void cadastrar(Pagamento pagamento) {
        em.persist(pagamento);
    }

    public Pagamento buscarPorId(Long id) {
        return em.find(Pagamento.class, id);
    }

    public void atualizar(Pagamento pagamento) {
        em.merge(pagamento);
    }

    public void excluir(Pagamento pagamento) {
        em.remove(pagamento);
    }
}

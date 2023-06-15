package br.com.elfs.loja.dao;

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
    
}

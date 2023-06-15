package br.com.elfs.loja.dao;

import javax.persistence.EntityManager;

import br.com.elfs.loja.modelo.Tipo;

public class TipoDAO {
	
	private EntityManager em;

	public TipoDAO(EntityManager em) {
		this.em = em;
	}
	
	public void cadastrar(Tipo categoria) {
		this.em.persist(categoria);
	}

}








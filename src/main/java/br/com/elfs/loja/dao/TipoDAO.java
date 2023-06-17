package br.com.elfs.loja.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

import br.com.elfs.loja.modelo.Tipo;

public class TipoDAO {

	private EntityManager em;

	public TipoDAO(EntityManager em) {
		this.em = em;
	}

	public void cadastrar(Tipo tipo) {
		this.em.persist(tipo);
	}

	public Tipo buscarPorId(Long id) {
		return em.find(Tipo.class, id);
	}

	public Tipo buscarPorNome(String nome) {
		String jpql = "SELECT t FROM Tipo t WHERE t.nome = :nome";
		TypedQuery<Tipo> query = em.createQuery(jpql, Tipo.class);
		query.setParameter("nome", nome);

		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Tipo> buscarTodos() {
		String jpql = "SELECT t FROM Tipo t";
		TypedQuery<Tipo> query = em.createQuery(jpql, Tipo.class);
		return query.getResultList();
	}

	public void atualizar(Tipo tipo) {
		em.getTransaction().begin();
		em.merge(tipo);
		em.getTransaction().commit();
	}

	public void remover(Tipo tipo) {
		em.getTransaction().begin();
		tipo = em.merge(tipo);
		em.remove(tipo);
		em.getTransaction().commit();
	}
}

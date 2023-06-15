package br.com.elfs.loja.testes;
import java.math.BigDecimal;

import javax.persistence.EntityManager;

import br.com.elfs.loja.dao.CamisaDAO;
import br.com.elfs.loja.dao.TipoDAO;
import br.com.elfs.loja.modelo.Camisa;
import br.com.elfs.loja.modelo.Tipo;
import br.com.elfs.loja.util.JPAUtil;

public class App {
    public static void main(String[] args) {
        Tipo mangacurta = new Tipo("MangaCurta");
        BigDecimal preco = new BigDecimal("15.90");
        Camisa camisa = new Camisa("Camisa Milan 22/23", "GG", preco, "Milan", mangacurta);

        EntityManager em = JPAUtil.getEntityManager();
        CamisaDAO camisaDAO = new CamisaDAO(em);
        TipoDAO tipoDAO = new TipoDAO(em);

        em.getTransaction().begin();

        tipoDAO.cadastrar(mangacurta);
        camisaDAO.cadastrar(camisa);

        em.getTransaction().commit();
        em.close();



    }
}
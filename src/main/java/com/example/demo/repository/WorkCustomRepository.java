package com.example.demo.repository;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Obra;

@Repository
public class WorkCustomRepository {
	
	private final String SQL_FILTER_WORK = " SELECT "
												+ " WRK.ID,  "
												+ " WRK.NOME, "
												+ " WRK.DESCRICAO, "
												+ " WRK.IMAGEM, "
												+ " WRK.DATA_PUBLICACAO, "
												+ " WRK.DATA_EXPOSICAO "
										+ " FROM OBRA WRK ";

	private final EntityManager em;
	
	public WorkCustomRepository(EntityManager em) {
        this.em = em;
    }
	
	@SuppressWarnings("unchecked")
	public Page<Obra> filterNativeQuery(PageRequest pageRequest, String nome, String descricao) {

		StringBuilder query = new StringBuilder();
        StringBuilder filter = new StringBuilder();

        query.append(SQL_FILTER_WORK);

        String condition = " WHERE ";

        if (nome != null) {
        	filter.append(" " + condition + " WRK.NOME LIKE :nome ");
            condition = " AND ";
        }

        if (descricao != null) {
        	filter.append(" " + condition + " WRK.DESCRICAO LIKE :descricao ");
            condition = " AND ";
        }

        query.append(filter);
        query.append(" ORDER BY WRK.NOME ASC ");
        
        Query qCount = em.createNativeQuery(query.toString());
        Query q = em.createQuery(query.toString(), Obra.class);
        
        if (nome != null) {
            q.setParameter("nome", "%" + nome + "%");
        }

        if (descricao != null) {
            q.setParameter("descricao", "%" + descricao + "%");
        }
        
        BigInteger totalObjetos = (BigInteger) qCount.getSingleResult();
        List<Obra> resultObras = (List<Obra>) q.setFirstResult((int)pageRequest.getOffset())
																.setMaxResults(pageRequest.getPageSize())
																.getResultList();
        
        return new PageImpl<>(resultObras, pageRequest, totalObjetos.longValue());

    }
	
	@SuppressWarnings("unchecked")
	public Page<Obra> filterObject(PageRequest pageRequest, String nome, String descricao) {

		StringBuilder query = new StringBuilder();
        StringBuilder filter = new StringBuilder();

        query.append("SELECT o FROM Obra o ");
        String condition = " WHERE ";

        if (nome != null) {
        	filter.append(" " + condition + " o.nome LIKE :nome ");
            condition = " AND ";
        }
        if (descricao != null) {
        	filter.append(" " + condition + " o.descricao LIKE :descricao ");
            condition = " AND ";
        }
        
        query.append(filter);
        query.append(" ORDER BY o.id ASC  ");
        
        Query qCount = em.createNativeQuery("SELECT COUNT(o) FROM Obra o " + filter.toString());
        Query q = em.createQuery(query.toString(), Obra.class);
        
        if (nome != null) {
            q.setParameter("nome", "%" + nome + "%");
            qCount.setParameter("nome", "%" + nome + "%");
        }
        if (descricao != null) {
            q.setParameter("descricao", "%" + descricao + "%");
            qCount.setParameter("descricao", "%" + descricao + "%");
        }
        
        BigInteger totalObjetos = (BigInteger) qCount.getSingleResult();
        List<Obra> resultObras = (List<Obra>) q.setFirstResult((int)pageRequest.getOffset())
																.setMaxResults(pageRequest.getPageSize())
																.getResultList();
        
        Page<Obra> page = new PageImpl<>(resultObras, pageRequest, totalObjetos.longValue());
        return page;

    }

}

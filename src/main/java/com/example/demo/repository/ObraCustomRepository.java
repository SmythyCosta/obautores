package com.example.demo.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Obra;

@Repository
public class ObraCustomRepository {

	private final EntityManager em;
	
	public ObraCustomRepository(EntityManager em) {
        this.em = em;
    }
	
	@SuppressWarnings("unchecked")
	public Page<Obra> find(PageRequest pageRequest, String nome, String descricao) {

        StringBuilder query = new StringBuilder();
        query.append("SELECT o FROM Obra o ");
        
        String condition = " WHERE ";

        if (nome != null) {
            query.append(" " + condition + " o.nome LIKE :nome ");
            condition = " AND ";
        }

        if (descricao != null) {
            query.append(" " + condition + " o.descricao LIKE :descricao ");
            condition = " AND ";
        }

        Query q = em.createQuery(query.toString(), Obra.class);

        if (nome != null) {
            q.setParameter("nome", "%" + nome + "%");
        }

        if (descricao != null) {
            q.setParameter("descricao", "%" + descricao + "%");
        }

        List<Obra> resultObras = (List<Obra>) q.getResultList();        
        Page<Obra> page = new PageImpl<>(resultObras, pageRequest, resultObras.size());
        
        return page;
    }

}

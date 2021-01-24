package com.example.demo.repository;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Autor2;

@Repository
public class ActorCustomRepository {
	
	
	private String ordem = "id";
	private String filtro = "";
	private Integer maximosObjetos = 2;
	private Integer posicaoAtual = 0;
	private long  totalObjetoss = 0;
	
	
	
	private final EntityManager em;
	
	public ActorCustomRepository(EntityManager em) {
        this.em = em;
    }
	
	@SuppressWarnings("unchecked")
	public Page<Autor2> filter(PageRequest pageRequest, String id, String nome, String cpf) {

        StringBuilder query = new StringBuilder();
        StringBuilder filter = new StringBuilder();
        
        query.append("SELECT a.id, a.nome, a.cpf FROM autor a  ");
        
        String condition = " WHERE ";

        if (nome != null) {
        	filter.append(" " + condition + " a.nome LIKE :nome ");
            condition = " AND ";
        }

        if (cpf != null) {
        	filter.append(" " + condition + " a.cpf LIKE :cpf ");
            condition = " AND ";
        }
        
        
        query.append(filter);
        query.append(" ORDER BY a.id ASC  ");
        

        Query qCount = em.createNativeQuery("SELECT COUNT(a) FROM autor a " + filter.toString());
        Query q = em.createNativeQuery(query.toString(), Autor2.class);
        
        
        if (nome != null) {
            q.setParameter("nome", "%" + nome + "%");
            qCount.setParameter("nome", "%" + nome + "%");
        }

        if (cpf != null) {
            q.setParameter("cpf", "%" + cpf + "%");
            qCount.setParameter("cpf", "%" + cpf + "%");
        }
        
        
        BigInteger totalObjetos = (BigInteger) qCount.getSingleResult();
        List<Autor2> resultObras = (List<Autor2>) q.setFirstResult((int)pageRequest.getOffset())
        											.setMaxResults(pageRequest.getPageSize())
        											.getResultList();

        
        Page<Autor2> page = new PageImpl<>(resultObras, pageRequest, totalObjetos.longValue());
        
        return page;
    }

}

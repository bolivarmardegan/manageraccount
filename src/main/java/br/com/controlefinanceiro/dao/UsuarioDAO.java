package br.com.controlefinanceiro.dao;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.controlefinanceiro.core.AbstractDAO;
import br.com.controlefinanceiro.model.Usuario;

@Stateless
public class UsuarioDAO extends AbstractDAO<Usuario>{

	public Usuario buscarUsuarioPorLoginSenha(Usuario usuario){
		String sql = "SELECT usuario FROM Usuario usuario WHERE usuario.login = :login AND usuario.senha = :senha";
		Query query = em.createQuery(sql);
		query.setParameter("login", usuario.getLogin());
		query.setParameter("senha", usuario.getSenha());
		try{
			Usuario usuarioResp = (Usuario)query.getSingleResult();
			return usuarioResp;
		}catch(Exception e){
			return null;
		}
	}

}

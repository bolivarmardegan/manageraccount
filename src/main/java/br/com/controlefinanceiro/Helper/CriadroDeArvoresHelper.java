package br.com.controlefinanceiro.Helper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import br.com.controlefinanceiro.dao.FinancaDAO;
import br.com.controlefinanceiro.model.CategoriaFinanca;
import br.com.controlefinanceiro.model.Financa;
import br.com.controlefinanceiro.model.TipoFinanca;

@Stateless
public class CriadroDeArvoresHelper implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8756856662270616170L;

	public TreeNode geradorDeArvore(List<CategoriaFinanca> categoriaList, FinancaDAO financaDAO, UsuarioSessionControler usuSession){
		//DefaultTreeNode root = new DefaultTreeNode(new Financa(null,"", new BigDecimal("0"), Calendar.getInstance(), "" , true, true,null), null);
		DefaultTreeNode root = new DefaultTreeNode(new Financa(null, null, null, null, null, null, null), null);
//		root = new DefaultTreeNode(new Financa(categoriaFinanca.getNome()), null);
		for (CategoriaFinanca categoriaFinanca : categoriaList) {
//			TreeNode categoria = new DefaultTreeNode(new Financa(categoriaFinanca.getNome(), new BigDecimal("0"), Calendar.getInstance(), "" , true, true), root);
			TreeNode categoria = new DefaultTreeNode(new Financa(null,categoriaFinanca.getNome(), categoriaFinanca, new BigDecimal("0"), Calendar.getInstance(), "" , null), root);
			List<Financa> financas = financaDAO.buscarFinancasPorCategoria(categoriaFinanca, usuSession.getUsuarioLogado());
			for (Financa financa : financas) {
				new DefaultTreeNode(new Financa(financa.getId(),financa.getNome(),financa.getCategoriaFinanca(), financa.getValor(), financa.getDataVencimento(), financa.getTipoFinanca(),  financa.getIdUsuario()), categoria);
			}
		}
		return root;
	}
	
//	TreeNode documents = new DefaultTreeNode(new Document("Documents", "-", "Folder"), root);
//    TreeNode pictures = new DefaultTreeNode(new Document("Pictures", "-", "Folder"), root);
//    TreeNode movies = new DefaultTreeNode(new Document("Movies", "-", "Folder"), root);
//     
//    TreeNode work = new DefaultTreeNode(new Document("Work", "-", "Folder"), documents);
	
	
	
	
	
	
	
	
	
	
}

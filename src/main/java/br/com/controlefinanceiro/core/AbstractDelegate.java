package br.com.controlefinanceiro.core;

import java.util.List;

public abstract class AbstractDelegate<T>{
	public AbstractDelegate() {
		// TODO Auto-generated constructor stub
	}
	
	public abstract AbstractDAO<T> getDaoInstance();
	
	public void inserir(T bean){		
		getDaoInstance().inserir(bean);
	}
	
	public void simplesInsert(T bean){
		getDaoInstance().simpleInsert(bean);
	}
	
	public void alterar(T bean){		
		getDaoInstance().alterar(bean);		
	}
	
	public void deletar(T bean){		
		getDaoInstance().deletar(bean);		
	}	
	
	public List<T> buscarTodos(){
		return getDaoInstance().buscarTodos();
	}
	
	public List<T> buscarTodos(String ordem){
		return getDaoInstance().buscarTodos(ordem);
	}
	
	public T buscarPorId(Integer id){
		return getDaoInstance().buscarPorId(id);
	}
	
	public T buscar (T t){
		return getDaoInstance().buscar(t);
	}
}

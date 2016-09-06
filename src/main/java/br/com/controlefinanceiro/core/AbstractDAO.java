package br.com.controlefinanceiro.core;

	import java.util.List;
	import javax.persistence.EntityManager;
	import javax.persistence.PersistenceContext;
	import javax.persistence.Query;
	import org.apache.log4j.Logger;

	public abstract class AbstractDAO<T> {

	Logger log = Logger.getLogger(getNomeClasse());
		
		
		@PersistenceContext
		protected EntityManager em;	
		
		public String getNomeClasse(){
			 return getClass().getSimpleName().replace("DAO","");
		}
		
		public void cleanSession(){
			em.clear();
			em.flush();
		}
		public void inserir(T t) {  
		    em.persist(em.merge(t));
		   // inserirLog(Constants.INCLUSAO,usuario.getNome(), usuario.getIdUnidade(), t);
		    log.info(t);
		}
		
		public void simpleInsert(T t){
			em.persist(t);
			log.info(t);
		}
		
		public void alterar(T t){
			em.merge(t);
			//inserirLog(Constants.ALTERACAO,"F0001", new Long(1), t);
			log.info(t);
		}
		
		public void deletar(T t){		
			em.remove(em.merge(t));
			//inserirLog(Constants.EXCLUSAO,"F0001", new Long(1), t);
			log.info(t);
			
		}
		
		public List<T> buscarTodos(){
			String sql = "SELECT t FROM " + getNomeClasse() + " t";
			Query query = em.createQuery(sql);
			return query.getResultList();
			
		}
		
		public List<T> buscarTodos(String ordem){
			String sql = "SELECT t FROM " + getNomeClasse() + " t ORDER by t." + ordem;
			Query query = em.createQuery(sql);
			return query.getResultList();
		}
		
//		protected void inserirLog(Integer operacao, String matricula, Long unidade, T t){
//			Log log = new Log();
//			log.setData(new Date());
//			log.setOperacao(operacao);
//			log.setMatricula(matricula);
//			log.setUnidade(unidade);
//			log.setDescricao(t.toString());
//			em.persist(log);
//		}
		
		public T buscarPorId(Integer id){
			String sql = "SELECT t FROM " + getNomeClasse() + " t WHERE t.id = " +id;
			Query createQuery = em.createQuery(sql);
			return (T) createQuery.getSingleResult();
		}
		
		public T buscar (T t){
			return (T) em.getReference(t.getClass(), t);
		}

}

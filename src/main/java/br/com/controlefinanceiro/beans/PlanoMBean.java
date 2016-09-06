package br.com.controlefinanceiro.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import br.com.controlefinanceiro.core.AbstractDelegate;
import br.com.controlefinanceiro.core.AbstractManagedBean;
import br.com.controlefinanceiro.dao.MenuDAO;
import br.com.controlefinanceiro.dao.PlanoDAO;
import br.com.controlefinanceiro.delegate.PerfilDelegate;
import br.com.controlefinanceiro.delegate.PlanoDelegate;
import br.com.controlefinanceiro.model.Menu;
import br.com.controlefinanceiro.model.Perfil;
import br.com.controlefinanceiro.model.Plano;
import br.com.controlefinanceiro.util.Constants;

@Named
@SessionScoped
public class PlanoMBean extends AbstractManagedBean<Plano> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8094662000240450086L;
		@Inject
		private PlanoDelegate planoDelegate;
		@Inject
		private MenuDAO menuDAO;
		@Inject
		private Plano plano;
		@Inject
		private PlanoDAO planoDAO;
		@Inject
		private Menu menu;
		@Inject
		private PerfilDelegate perfilDelegate;
		private List<Perfil> perfilList;
		private List<Plano> planosList;
		private List<Menu> menuListInit;
		private List<Menu> menuListSelected;
		private Integer fluxoDePagina;
		
		
		@Override
		public AbstractDelegate<Plano> getDelegateInstance() {
			return this.planoDelegate;
		}

		@PostConstruct
		public void init() {
			this.perfilList = new ArrayList<Perfil>();
			this.perfilList = this.perfilDelegate.buscarTodos();
			this.planosList = new ArrayList<Plano>();
			this.planosList = this.planoDelegate.buscarTodos();
			this.menuListInit = new ArrayList<Menu>();
			this.menuListInit = this.menuDAO.buscarTodos();
			this.menuListSelected = new ArrayList<Menu>();
			
		}
		
		public void salvar(){
			CollectionUtils.find(this.menuListSelected, new Predicate() {
				@Override
				public boolean evaluate(Object object) {
					Menu menu =(Menu)object;
					menu.setStatus(true);
					menuDAO.alterar(menu);
					return false;
				}
			});
			Perfil perfil = this.perfilDelegate.buscarPorId(this.plano.getPerfil().getId());
			this.plano.setPerfil(perfil);
			this.plano.setMenuList(this.menuListSelected);
			this.planoDelegate.inserir(this.plano);
			this.plano = new Plano();
			this.menuListSelected = new ArrayList<Menu>();
			this.planosList = this.planoDelegate.buscarTodos();
			this.setFluxoDePagina(Constants.VISUALIZACAO);
			
		}
		
		public void vincularMenu(){
			CollectionUtils.find(menuListInit, new Predicate() {
				@Override
				public boolean evaluate(Object arg0) {
					Menu menuRefe = (Menu)arg0;
					if(menuRefe.getId().equals(menu.getId())){
						menuListSelected.add(menuRefe);
					}
					return false;
				}
			});
			this.setFluxoDePagina(Constants.INCLUSAO);
		}
		
		public void carregarPlanoEdit(){
			CollectionUtils.find(this.planosList, new Predicate() {
				@Override
				public boolean evaluate(Object arg0) {
					Plano planoFind = (Plano)arg0;
					if(planoFind.equals(plano)){
						menuListSelected.addAll(planoDAO.buscarMenusDoPlano(planoFind));
					}
					return false;
				}
			});
			this.setFluxoDePagina(Constants.ALTERACAO);
		}
		
		public void desvincularMenu(){
			this.menuListSelected.remove(this.menu);
			this.plano.setMenuList(menuListSelected);
			this.planoDAO.alterar(this.plano);
			this.atualizarMenuAposDesvincular(this.menu);
			this.plano = new Plano();
			this.setFluxoDePagina(Constants.VISUALIZACAO);
			
		}
		
		public void atualizarMenuAposDesvincular(Menu menu){
			List<Plano> planosDoMenu = this.menuDAO.buscarPlanosDoMenu(menu);
			if(planosDoMenu.isEmpty()){
				menu.setStatus(false);
				this.menuDAO.alterar(menu);
		}
		}
		
		
		public void excluir(){
			List<Menu> menusDoPlano = planoDAO.buscarMenusDoPlano(plano);
			this.planoDelegate.deletar(plano);
			this.atualizarStatusDoMenu(menusDoPlano);
			this.planosList = this.planoDelegate.buscarTodos();
			this.planosList = this.planoDelegate.buscarTodos();
			this.menuListSelected = new ArrayList<Menu>();
			this.plano = new Plano();
			this.setFluxoDePagina(Constants.VISUALIZACAO);
		}
		
		public void atualizarStatusDoMenu(List<Menu>menusDoPlano){
			for (Menu menu : menusDoPlano) {
				List<Plano> planosDoMenu = this.menuDAO.buscarPlanosDoMenu(menu);
				if(!planosDoMenu.contains(menu)){
					menu.setStatus(false);
					this.menuDAO.alterar(menu);
				}
			}
		}
		
		public void alterar(){
			List<Menu> menusDoPlano = planoDAO.buscarMenusDoPlano(plano);
			int tamanhoInit = menusDoPlano.size();
			int novoTamanho = this.menuListSelected.size();
			this.plano.setMenuList(this.menuListSelected);
			this.planoDelegate.alterar(plano);
			if(tamanhoInit != novoTamanho){
				this.atualizarStatusDoMenu(menusDoPlano);
			}
			this.planosList = this.planoDelegate.buscarTodos();
			this.menuListSelected = new ArrayList<Menu>();
			this.plano = new Plano();
			this.setFluxoDePagina(Constants.VISUALIZACAO);
		}
		
		public void clean(){
			this.plano = new Plano();
			this.menuListSelected = new ArrayList<Menu>();
			
		}

		public PlanoDelegate getPlanoDelegate() {
			return planoDelegate;
		}

		public void setPlanoDelegate(PlanoDelegate planoDelegate) {
			this.planoDelegate = planoDelegate;
		}

		public Plano getPlano() {
			return plano;
		}

		public void setPlano(Plano plano) {
			this.plano = plano;
		}

		public Menu getMenu() {
			return menu;
		}

		public void setMenu(Menu menu) {
			this.menu = menu;
		}

		public List<Plano> getPlanosList() {
			return planosList;
		}

		public void setPlanosList(List<Plano> planosList) {
			this.planosList = planosList;
		}

		public List<Menu> getMenuListInit() {
			return menuListInit;
		}
		
		public void setMenuListInit(List<Menu> menuListInit) {
			this.menuListInit = menuListInit;
		}
		
		public List<Menu> getMenuListSelected() {
			return menuListSelected;
		}
		
		public void setMenuListSelected(List<Menu> menuListSelected) {
			this.menuListSelected = menuListSelected;
		}
		
		public Integer getFluxoDePagina() {
			return fluxoDePagina;
		}
		public void setFluxoDePagina(Integer fluxoDePagina) {
			this.fluxoDePagina = fluxoDePagina;
		}
		
		public List<Perfil> getPerfilList() {
			return perfilList;
		}
		
		public void setPerfilList(List<Perfil> perfilList) {
			this.perfilList = perfilList;
		}
}

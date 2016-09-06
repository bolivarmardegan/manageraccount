package br.com.controlefinanceiro.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.controlefinanceiro.core.AbstractDelegate;
import br.com.controlefinanceiro.core.AbstractManagedBean;
import br.com.controlefinanceiro.dao.MenuDAO;
import br.com.controlefinanceiro.dao.SubMenusDAO;
import br.com.controlefinanceiro.delegate.MenuDelegate;
import br.com.controlefinanceiro.delegate.TiposMenusDelegate;
import br.com.controlefinanceiro.model.Menu;
import br.com.controlefinanceiro.model.SubMenus;
import br.com.controlefinanceiro.model.TiposMenus;
import br.com.controlefinanceiro.util.Constants;

@Named
@SessionScoped
public class MenuMBean extends AbstractManagedBean<Menu> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7208038740336440121L;
	@Inject
	private MenuDelegate menuDelegate;
	@Inject
	private Menu menu;
	@Inject
	private SubMenusDAO subMenusDAO;
	@Inject
	private SubMenus subMenus;
	@Inject
	private MenuDAO menuDAO;
	private List<Menu> menuList;
	private List<SubMenus> subMenusList;
	private Integer fluxoDePagina;
	@Inject
	private TiposMenus tipoMenu;
	private List<TiposMenus> tipoMenuList;
	@Inject
	private TiposMenusDelegate tiposMenusDelegate;
	
	@Override
	public AbstractDelegate<Menu> getDelegateInstance() {
		return this. menuDelegate;
	}
	
	public MenuMBean() {
	}

	@PostConstruct
	public void init() {
		this.tipoMenuList = new ArrayList<TiposMenus>();
		this.tipoMenuList = this.tiposMenusDelegate.buscarTodos();
		this.menuList = new ArrayList<Menu>();
		this.subMenusList = new ArrayList<SubMenus>();
		this.menuList = this.menuDelegate.buscarTodos();
		this.setFluxoDePagina(Constants.INCLUSAO);
		
	}
	
	public void salvar(){
		this.menu.setTipoMenu(this.tipoMenu);
		this.menuDelegate.inserir(this.menu);
		Menu menuSalvo = menuDAO.buscarMenuPorNome(this.menu);
		for (SubMenus subMenu : subMenusList) {
			subMenu.setMenu(menuSalvo);
			this.subMenusDAO.inserir(subMenu);
		}
		this.menuList = this.menuDelegate.buscarTodos();
		this.menu = new Menu();
		this.subMenus = new SubMenus();
		this.subMenusList = new ArrayList<SubMenus>();
		this.setFluxoDePagina(Constants.INCLUSAO);
	}
	
	public void editar(){
		this.menu.setTipoMenu(this.tipoMenu);
		this.menuDelegate.alterar(this.menu);
		for (SubMenus subMenu : subMenusList) {
			if(subMenu.getId() == null){
				subMenu.setMenu(this.menu);
				this.subMenusDAO.inserir(subMenu);
			}else{
				subMenu.setMenu(this.menu);
				this.subMenusDAO.alterar(subMenu);
			}
		}
		//this.menuDelegate.alterar(this.menu);
		this.menuList = this.menuDelegate.buscarTodos();
		this.menu = new Menu();
		this.subMenus = new SubMenus();
		this.subMenusList = new ArrayList<SubMenus>();
		this.setFluxoDePagina(Constants.INCLUSAO);
	}
	
	public void excluir(){
		this.subMenusList = this.subMenusDAO.buscarSubMenusPorMenu(this.menu);
		for (SubMenus subMenu : this.subMenusList) {
			this.subMenusDAO.deletar(subMenu);
		}
		this.subMenusList = new ArrayList<SubMenus>();
		this.menuDelegate.deletar(this.menu);
		this.menuList = this.menuDelegate.buscarTodos();
		this.menu = new Menu();
		this.setFluxoDePagina(Constants.INCLUSAO);
	}
	
	public void vincularSubMenus(){
		this.menuList = new ArrayList<Menu>();
		this.menuList.add(this.menu);
		this.subMenusList.add(this.subMenus);
		this.subMenus = new SubMenus();
	}
	
	public void carregarSubMenuEdit(){
		this.subMenusList.remove(this.subMenus);
		this.setFluxoDePagina(Constants.ALTERACAO);
		
	}
	public void carregarMenuEdit(){
		this.menuList = new ArrayList<Menu>();
		this.subMenusList =  this.subMenusDAO.buscarSubMenusPorMenu(this.menu);
		this.menuList.add(this.menu);
		this.setFluxoDePagina(Constants.ALTERACAO);
	}
	
	public void limparSubMenus(){
		Integer id = this.getSubMenus().getId();
		if(id != null){
				this.subMenusDAO.deletar(this.subMenus);
		}
		this.subMenusList.remove(this.subMenus);
		this.subMenus = new SubMenus();
		this.setFluxoDePagina(Constants.ALTERACAO);
		
	}
	
	public void clear(){
		this.subMenusList = new ArrayList<>();
		this.menu = new Menu();
		this.subMenus = new SubMenus();
		this.menuList = this.menuDelegate.buscarTodos();
		this.setFluxoDePagina(Constants.INCLUSAO);
	}
		
	public Menu getMenu() {
		return menu;
	}
	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	
	public List<Menu> getMenuList() {
		return menuList;
	}
	
	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}
	
	
	public Integer getFluxoDePagina() {
		return fluxoDePagina;
	}
	public void setFluxoDePagina(Integer fluxoDePagina) {
		this.fluxoDePagina = fluxoDePagina;
	}
	
	public List<SubMenus> getSubMenusList() {
		return subMenusList;
	}
	
	public void setSubMenusList(List<SubMenus> subMenusList) {
		this.subMenusList = subMenusList;
	}
	
	public SubMenus getSubMenus() {
		return subMenus;
	}
	
	public void setSubMenus(SubMenus subMenus) {
		this.subMenus = subMenus;
	}
	
	public TiposMenus getTipoMenu() {
		return tipoMenu;
	}
	
	public void setTipoMenu(TiposMenus tipoMenu) {
		this.tipoMenu = tipoMenu;
	}
	
	public List<TiposMenus> getTipoMenuList() {
		return tipoMenuList;
	}
	
	public void setTipoMenuList(List<TiposMenus> tipoMenuList) {
		this.tipoMenuList = tipoMenuList;
	}

}

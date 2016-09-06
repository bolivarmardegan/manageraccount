package br.com.controlefinanceiro.strategyImpl;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import br.com.controlefinanceiro.dao.SubMenusDAO;
import br.com.controlefinanceiro.model.Menu;
import br.com.controlefinanceiro.model.SubMenus;
import br.com.controlefinanceiro.model.Usuario;
import br.com.ppraonline.strategy.InterfaceMenu;



public class CarregarMenuEsquerdo implements InterfaceMenu{
	
	private InterfaceMenu proximoGrupoMenu;

	@Override
	public void carregarMenu(Usuario usuario, final SubMenusDAO subMenuDAO, List<Menu> menusPlano,
			List<MenuModel> menusCarregados) {
		final MenuModel menuEmpresas = new DefaultMenuModel();
		CollectionUtils.find(menusPlano, new Predicate() {
		@Override
		public boolean evaluate(Object object) {
			Menu menuBase = (Menu) object;
			if(menuBase.getTipoMenu().getNome().toLowerCase().equals("comum")){
			DefaultSubMenu menuPrincipal = new DefaultSubMenu(menuBase.getNome());
				List<SubMenus> subMenus = subMenuDAO.buscarSubMenusPorMenu(menuBase);
				for (SubMenus subMenu12: subMenus) {
					DefaultMenuItem subMenu = new DefaultMenuItem(subMenu12.getNome());
					subMenu.setUrl(subMenu12.getUrl());
					menuPrincipal.addElement(subMenu);
				}
				menuEmpresas.addElement(menuPrincipal);
			}
			return false;
		}
	});
		menusCarregados.add(menuEmpresas);
		proximoGrupoMenu.carregarMenu(usuario, subMenuDAO, menusPlano, menusCarregados);
		
	}

	@Override
	public void setProximaCargaMenu(InterfaceMenu proximoGrupoMenu) {
		this.proximoGrupoMenu = proximoGrupoMenu;
		
	}

	

}

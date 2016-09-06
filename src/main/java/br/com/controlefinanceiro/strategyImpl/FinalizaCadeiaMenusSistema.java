package br.com.controlefinanceiro.strategyImpl;

import java.util.List;

import org.primefaces.model.menu.MenuModel;

import br.com.controlefinanceiro.dao.SubMenusDAO;
import br.com.controlefinanceiro.model.Menu;
import br.com.controlefinanceiro.model.Usuario;
import br.com.ppraonline.strategy.InterfaceMenu;

public class FinalizaCadeiaMenusSistema implements InterfaceMenu{

	@Override
	public void carregarMenu(Usuario usuario, SubMenusDAO subMenuDAO, List<Menu> menusPlano,
			List<MenuModel> menusCarregados) {
		
	}

	@Override
	public void setProximaCargaMenu(InterfaceMenu proximoGrupoMenu) {
		
	}
}

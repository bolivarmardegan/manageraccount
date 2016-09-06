package br.com.ppraonline.strategy;

import java.util.List;

import org.primefaces.model.menu.MenuModel;

import br.com.controlefinanceiro.dao.SubMenusDAO;
import br.com.controlefinanceiro.model.Menu;
import br.com.controlefinanceiro.model.Usuario;

public interface InterfaceMenu {
	
	public void carregarMenu(Usuario usuario, SubMenusDAO subMenuDAO, List<Menu> menusPlano, List<MenuModel> menusCarregados);
	public void setProximaCargaMenu(InterfaceMenu proximoGrupoMenu);
}

package br.com.controlefinanceiro.util;

import java.util.List;

import javax.ejb.Stateless;

import org.primefaces.model.menu.MenuModel;

import br.com.controlefinanceiro.dao.SubMenusDAO;
import br.com.controlefinanceiro.model.Menu;
import br.com.controlefinanceiro.model.Usuario;
import br.com.controlefinanceiro.strategyImpl.CarregarMenuConfiguracoes;
import br.com.controlefinanceiro.strategyImpl.CarregarMenuEsquerdo;
import br.com.controlefinanceiro.strategyImpl.FinalizaCadeiaMenusSistema;

@Stateless
public class CarregaMenusSistema {
	
	public List<MenuModel> carregaMenusSistema(Usuario usuario, SubMenusDAO subMenuDAO, List<Menu> menusPlano, List<MenuModel> menusCarregados){
		
		CarregarMenuEsquerdo carregarMenuEsquerdo = new CarregarMenuEsquerdo();
		CarregarMenuConfiguracoes carregarMenuConfiguracoes = new CarregarMenuConfiguracoes();
		FinalizaCadeiaMenusSistema fim = new FinalizaCadeiaMenusSistema();
		carregarMenuEsquerdo.setProximaCargaMenu(carregarMenuConfiguracoes);
		carregarMenuConfiguracoes.setProximaCargaMenu(fim);
		carregarMenuEsquerdo.carregarMenu(usuario, subMenuDAO, menusPlano, menusCarregados);
		return menusCarregados;
	}
	
}

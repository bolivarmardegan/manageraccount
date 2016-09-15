package br.com.controlefinanceiro.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

import br.com.controlefinanceiro.Helper.UsuarioSessionControler;
import br.com.controlefinanceiro.core.AbstractDelegate;
import br.com.controlefinanceiro.core.AbstractManagedBean;
import br.com.controlefinanceiro.dao.MenuDAO;
import br.com.controlefinanceiro.dao.PlanoDAO;
import br.com.controlefinanceiro.dao.SubMenusDAO;
import br.com.controlefinanceiro.dao.UsuarioDAO;
import br.com.controlefinanceiro.delegate.LoginDelegate;
import br.com.controlefinanceiro.model.Login;
import br.com.controlefinanceiro.model.Menu;
import br.com.controlefinanceiro.model.Perfil;
import br.com.controlefinanceiro.model.Usuario;
import br.com.controlefinanceiro.util.CarregaMenusSistema;
import br.com.controlefinanceiro.util.PagesUrl;

@Named
@RequestScoped
public class LoginMBean extends AbstractManagedBean<Login> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5474172709740727927L;
	@Inject
	private LoginDelegate loginDelegate;
	@Inject
	private Usuario usuario;
	@Inject
	private UsuarioSessionControler usuarioLogado;
	@Inject
	private Perfil perfil;
	@Inject
	private Menu menu;
	@Inject
	private PlanoDAO planoDAO;
	@Inject
	private UsuarioDAO usuarioDAO;
	@Inject
	private SubMenusDAO subMenuDAO;
	private MenuModel menuModel;
	private MenuModel menuConfiguracoes;
	@Inject
	private MenuDAO menuDAO;
	@Inject
	private CarregaMenusSistema carregarMenusSistema;
	private List<Menu> menusPlano;
	private List<MenuModel> menusCarregados;

	
	
	
	@Override
	public AbstractDelegate<Login> getDelegateInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	@PostConstruct
	public void init(){
		this.menusCarregados = new ArrayList<MenuModel>();
		this.menusPlano = new ArrayList<Menu>();
		this.menuModel = new DefaultMenuModel();
		this.menuConfiguracoes = new DefaultMenuModel();
	}
	
	public void logar(){
		try {
			this.usuario = this.usuarioDAO.buscarUsuarioPorLoginSenha(this.usuario);
			this.menusPlano = this.planoDAO.buscarMenusDoPlano(this.usuario.getPlano());
			this.carregarMenusDoUsuario();
		} catch (Exception e) {
			session.addMessageError("Usuário ou senha inválidos.");
			this.usuario = new Usuario();
		}
	}
	
	public void carregarMenusDoUsuario(){
		this.menusCarregados = carregarMenusSistema.carregaMenusSistema(usuario, subMenuDAO, menusPlano, menusCarregados);
		if(menusCarregados.size() == 1){
			this.menuModel = menusCarregados.get(0);
		}else{
			this.menuModel = menusCarregados.get(0);
			this.menuConfiguracoes = menusCarregados.get(1);
		}
		this.redirectToPage(PagesUrl.INDEX.getUrl(), true);
		session.addObjeto("usuarioLogado", this.usuario);
		usuarioLogado.logar(this.menuModel, this.menuConfiguracoes, this.usuario);
		this.limparObjetos();
	}
	
	public void limparObjetos(){
		this.usuario = new Usuario();
		this.menuModel = new DefaultMenuModel();
		this.menuConfiguracoes = new DefaultMenuModel();
		this.menusCarregados = new ArrayList<MenuModel>();
		this.menusPlano = new ArrayList<Menu>();
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}


	public void setUsuarioLogado(UsuarioSessionControler usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public MenuModel getMenuModel() {
		return menuModel;
	}

	public void setMenuModel(MenuModel menuModel) {
		this.menuModel = menuModel;
	}

	public MenuModel getMenuConfiguracoes() {
		return menuConfiguracoes;
	}

	public void setMenuConfiguracoes(MenuModel menuConfiguracoes) {
		this.menuConfiguracoes = menuConfiguracoes;
	}

	public List<Menu> getMenusPlano() {
		return menusPlano;
	}

	public void setMenusPlano(List<Menu> menusPlano) {
		this.menusPlano = menusPlano;
	}

	public List<MenuModel> getMenusCarregados() {
		return menusCarregados;
	}

	public void setMenusCarregados(List<MenuModel> menusCarregados) {
		this.menusCarregados = menusCarregados;
	}
	
	

}

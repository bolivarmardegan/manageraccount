package br.com.controlefinanceiro.Helper;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;
import br.com.controlefinanceiro.core.AbstractDelegate;
import br.com.controlefinanceiro.core.AbstractManagedBean;
import br.com.controlefinanceiro.model.Usuario;
import br.com.controlefinanceiro.util.PagesUrl;

@Named
@SessionScoped
public class UsuarioSessionControler extends AbstractManagedBean<UsuarioSessionControler> implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5396563523133860972L;
	private Usuario usuarioLogado;
	private MenuModel menuModel;
	private MenuModel menuConfiguracoes;
	
	
	public UsuarioSessionControler() {
	
	
	}
	@Override
	public AbstractDelegate<UsuarioSessionControler> getDelegateInstance() {
		return null;
	}
	
	@PostConstruct
	public void init() {
		
	}
	
	
	public void logar( MenuModel menu , MenuModel menuConfiguracoes,  Usuario usuario){
		this.usuarioLogado = usuario;
		if(usuarioLogado == null){
			redirectToPage(PagesUrl.LOGIN.getUrl(), true);
		}
		this.menuModel = menu;
		this.menuConfiguracoes = menuConfiguracoes;
	}
	
	public void deslogar(){
		session.addObjeto("usuarioLogado", new Usuario());
		this.usuarioLogado = new Usuario();
		this.menuModel =  new DefaultMenuModel();
		this.menuConfiguracoes = new DefaultMenuModel();
		session.encerrarSessao();
		redirectToPage(PagesUrl.LOGIN.getUrl(), true);
	}
	
	public boolean isLogado(){
		return usuarioLogado != null;
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
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
	
}

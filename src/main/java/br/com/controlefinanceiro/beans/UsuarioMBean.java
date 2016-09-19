package br.com.controlefinanceiro.beans;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FlowEvent;

import br.com.controlefinanceiro.Helper.CriptografadorDeSenhas;
import br.com.controlefinanceiro.core.AbstractDelegate;
import br.com.controlefinanceiro.core.AbstractManagedBean;
import br.com.controlefinanceiro.dao.PlanoDAO;
import br.com.controlefinanceiro.dao.UsuarioDAO;
import br.com.controlefinanceiro.delegate.PerfilDelegate;
import br.com.controlefinanceiro.delegate.PermissoesUsuarioDelegate;
import br.com.controlefinanceiro.delegate.PlanoDelegate;
import br.com.controlefinanceiro.delegate.UsuarioDelegate;
import br.com.controlefinanceiro.model.Perfil;
import br.com.controlefinanceiro.model.PermissoesUsuario;
import br.com.controlefinanceiro.model.Plano;
import br.com.controlefinanceiro.model.Usuario;
import br.com.controlefinanceiro.util.Constants;
import br.com.controlefinanceiro.util.PagesUrl;

@Named
@SessionScoped
public class UsuarioMBean extends AbstractManagedBean<Usuario> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1429204690195695324L;
	@Inject
	private UsuarioDelegate usuarioDelegate;
	@Inject
	private Usuario usuario;
	@Inject
	private Plano plano;
	@Inject
	private Perfil perfil;
	@Inject
	private CriptografadorDeSenhas criptografador;
	private List<Perfil> perfilList;
	private List<Plano> planoList;
	private List<Usuario> usuariosList;
	@Inject
	private PerfilDelegate perfilDelegate;
	@Inject
	private PlanoDelegate planoDelegate;
	@Inject
	private PermissoesUsuario permissoesUsuario;
	@Inject
	private PlanoDAO planoDAO;
	@Inject
	private PermissoesUsuarioDelegate permissoesUsuarioDelegate;
	private List<PermissoesUsuario> permissoesList;
	private Integer fluxoDePagina;
	private String novaSenha;
	private boolean solicitouNovaSenha;
	@Inject
	private UsuarioDAO usuarioDAO;
	
	
	@Override
	public AbstractDelegate<Usuario> getDelegateInstance() {
		return this.usuarioDelegate;
	}

	@PostConstruct
	public void init() {
		this.setFluxoDePagina(Constants.INCLUSAO);
		this.permissoesList = new ArrayList<PermissoesUsuario>();
		this.permissoesList = this.permissoesUsuarioDelegate.buscarTodos();
		this.perfilList = new ArrayList<Perfil>();
		this.perfilList = this.perfilDelegate.buscarTodos();
		this.usuariosList = new ArrayList<Usuario>();
		this.usuariosList = this.usuarioDelegate.buscarTodos();
		this.solicitouNovaSenha = false;
	}
	
	public void salvar(){
		try {
			String senhaCriptografada = this.criptografador.convertStringToMd5(usuario.getSenha());
			this.usuario.setSenha(senhaCriptografada);
			this.usuario.setPerfil(this.perfilList.get(1));
			this.planoList = this.planoDAO.buscarPlanoPorPerfil(this.usuario.getPerfil().getId());
			this.usuario.setPlano(this.planoList.get(0));
			this.usuario.setPermissoesUsuario(this.permissoesList.get(0));
			this.usuarioDelegate.inserir(this.usuario);
			this.usuario = new Usuario();
			this.redirectToPage(PagesUrl.LOGIN.getUrl(), true);
		} catch (Exception e) {
			session.addMessageError("Erro ao cadastrar");
		}
	}
	
	public void carregarPlanoPorPerfil(){
		this.planoList = new ArrayList<Plano>();
		this.planoList = this.planoDAO.buscarPlanoPorPerfil(this.perfil.getId());
	}
	
	public void cancelar(){
		this.usuario = new Usuario();
		this.novaSenha = "";
		this.setSolicitouNovaSenha(false);
		this.redirectToPage(PagesUrl.LOGIN.getUrl(), true);
	}
	
	
//	public String onFlowProcess(FlowEvent event) {
//	    return event.getNewStep();
//	}
	
	public void validarLoginCadastro(){
		
		Usuario buscarUsuarioPorLogin = this.usuarioDAO.buscarUsuarioPorLogin(this.usuario.getLogin());
		if(buscarUsuarioPorLogin != null){
			this.session.addMessageInfo("O login "+this.usuario.getLogin()+" está indisponível.", "");
			this.usuario.setLogin("");
		}
	}

	public void alterarSenha(){
		if(this.novaSenha.equals(this.usuario.getSenha())){
			String senhaCript = this.criptografador.convertStringToMd5(novaSenha);
			this.usuario.setSenha(senhaCript);
			this.usuarioDelegate.alterar(this.usuario);
			this.usuario = new Usuario();
			this.redirectToPage(PagesUrl.LOGIN.getUrl(), true);
			this.session.addMessageError("Senha alterada com Sucesso!");
		}else{
			this.session.addMessageError("Nova senha não confirmada!");
		}
		
	}
	
	
	public void verificarLoginemail(){
		try {
			this.usuario = this.usuarioDAO.buscarUserPorLoginEmail(this.usuario.getLogin(), this.usuario.getEmail());
			usuario.setSenha("");
			this.setSolicitouNovaSenha(true);
		} catch (Exception e) {
			this.session.addMessageError("Email ou Login inválidos!","");
		}
		
	}

	
	public void cadastrar(){
		this.redirectToPage(PagesUrl.CADASTRO_USUARIO.getUrl(), true);
	}
	
	public Integer getFluxoDePagina() {
		return fluxoDePagina;
	}
	
	public void setFluxoDePagina(Integer fluxoDePagina) {
		this.fluxoDePagina = fluxoDePagina;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Plano getPlano() {
		return plano;
	}

	public void setPlano(Plano plano) {
		this.plano = plano;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public List<Perfil> getPerfilList() {
		return perfilList;
	}

	public void setPerfilList(List<Perfil> perfilList) {
		this.perfilList = perfilList;
	}

	public List<Plano> getPlanoList() {
		return planoList;
	}

	public void setPlanoList(List<Plano> planoList) {
		this.planoList = planoList;
	}

	public List<Usuario> getUsuariosList() {
		return usuariosList;
	}

	public void setUsuariosList(List<Usuario> usuariosList) {
		this.usuariosList = usuariosList;
	}


	public PermissoesUsuario getPermissoesUsuario() {
		return permissoesUsuario;
	}

	public void setPermissoesUsuario(PermissoesUsuario permissoesUsuario) {
		this.permissoesUsuario = permissoesUsuario;
	}

	public List<PermissoesUsuario> getPermissoesList() {
		return permissoesList;
	}

	public void setPermissoesList(List<PermissoesUsuario> permissoesList) {
		this.permissoesList = permissoesList;
	}
	public boolean isSolicitouNovaSenha() {
		return solicitouNovaSenha;
	}
	
	public void setSolicitouNovaSenha(boolean solicitouNovaSenha) {
		this.solicitouNovaSenha = solicitouNovaSenha;
	}
	
	public String getNovaSenha() {
		return novaSenha;
	}
	
	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}
	
	
	
	
}

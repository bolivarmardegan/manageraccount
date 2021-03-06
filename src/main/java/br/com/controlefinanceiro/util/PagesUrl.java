package br.com.controlefinanceiro.util;

public enum PagesUrl {
	
	CADASTRO_USUARIO   ("/usuarios_cadastro.xhtml"),
	LOGIN    ("/login.xhtml"),
	INDEX ("/index.xhtml"), 
	RECUPERAR_SENHA_XHTML ("/recuperar_senha.xhtml");
	
	
	private final String url;
 
	private PagesUrl(final String url) {
        this.url = url;
    }
	
	public String getUrl() {
		return url;
	}
	
    @Override
    public String toString() {
        return url;
    }
}

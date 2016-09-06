package br.com.controlefinanceiro.util;

public enum PagesUrl {
	
	CADASTRO_USUARIO   ("/pages/clientes/usuarios/usuarios_cadastro.xhtml"),
	LOGIN    ("/login.xhtml"),
	INDEX ("/index.xhtml");
	
	
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

package br.com.controlefinanceiro.beans;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.TreeNode;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;

import br.com.controlefinanceiro.Helper.CriadroDeArvoresHelper;
import br.com.controlefinanceiro.Helper.UsuarioSessionControler;
import br.com.controlefinanceiro.core.AbstractDelegate;
import br.com.controlefinanceiro.core.AbstractManagedBean;
import br.com.controlefinanceiro.dao.CategoriaFinancaDAO;
import br.com.controlefinanceiro.dao.FinancaDAO;
import br.com.controlefinanceiro.delegate.CategoriaFinancaDelegate;
import br.com.controlefinanceiro.delegate.FinancaDelegate;
import br.com.controlefinanceiro.delegate.TipoFinancaDelegate;
import br.com.controlefinanceiro.model.CategoriaFinanca;
import br.com.controlefinanceiro.model.Financa;
import br.com.controlefinanceiro.model.TipoFinanca;
import br.com.controlefinanceiro.util.Constants;

@Named
@SessionScoped
public class FinancasMBean extends AbstractManagedBean<Financa> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7652326951664974652L;

	@Inject
	private FinancaDelegate financaDelegate;
	@Inject
	private Financa financa;
	@Inject
	private CategoriaFinanca categoria;
	@Inject
	private CategoriaFinancaDelegate categoriaDelegate;
	@Inject
	private CategoriaFinancaDAO categoriaDAO;
	private List<CategoriaFinanca> categoriaList;
	private Integer fluxoDePagina;
	@Inject
	private TipoFinancaDelegate tipoFinancaDelegate;
	private List<Financa> financas;
	private List<TipoFinanca> tipoFinancaList;
	private String opcao = "selecionar";

	@Inject
	private FinancaDAO financaDAO;
	@Inject
	private UsuarioSessionControler usuSession;

	private BigDecimal debitos;
	private BigDecimal creditos;
	private BigDecimal saldo;

	private Calendar dataAtual;
	private Calendar dataMaxima;
	private Calendar dataMinima;

	private TreeNode root;
	@Inject
	private CriadroDeArvoresHelper criadorTable;

	@Override
	public AbstractDelegate<Financa> getDelegateInstance() {
		return this.financaDelegate;
	}

	@PostConstruct
	public void init() {
		this.financas = new ArrayList<Financa>();
		this.financas = this.financaDAO.buscarFinancasDoUsuario(usuSession.getUsuarioLogado());
		this.tipoFinancaList = new ArrayList<TipoFinanca>();
		this.tipoFinancaList = this.tipoFinancaDelegate.buscarTodos();
		this.categoriaList = new ArrayList<CategoriaFinanca>();
		this.categoriaList = this.categoriaDAO.buscarCategoriasDoUsuario(this.usuSession.getUsuarioLogado());
		this.categoria.setNome("Gerais");
		this.root = this.criadorTable.geradorDeArvore(this.categoriaList, this.financaDAO, this.usuSession);
		this.gerarSaldo();
		this.gerarDatasDeControle();
		this.setFluxoDePagina(Constants.INCLUSAO);
	}

	public void salvar() {
		if (garantirAnoCoerente(this.financa)) {
			this.conferirData();
			if (this.categoria.getId() == null) {
				this.categoria.setIdUsuario(this.usuSession.getUsuarioLogado().getId());
				CategoriaFinanca categoriaPorNome = this.categoriaDAO.buscarCategoriaPorNome(this.categoria.getNome(), this.usuSession.getUsuarioLogado());
				if(categoriaPorNome == null){
					this.categoriaDelegate.simplesInsert(this.categoria);
					this.categoriaList = this.categoriaDAO.buscarCategoriasDoUsuario(this.usuSession.getUsuarioLogado());
					CategoriaFinanca categoriaNome = this.categoriaDAO.buscarCategoriaPorNome(this.categoria.getNome(), this.usuSession.getUsuarioLogado());
					this.categoria = categoriaNome;
					
				}else
					this.categoria = categoriaPorNome;
			}
//			CategoriaFinanca cate = this.categoriaDAO.buscarCategoriaPorNome(this.categoria.getNome(),
//					this.usuSession.getUsuarioLogado());
			this.financa.setIdUsuario(this.usuSession.getUsuarioLogado().getId());
			this.financa.setCategoriaFinanca(this.categoria);
			this.financaDelegate.inserir(this.financa);
			this.categoriaList = this.categoriaDAO.buscarCategoriasDoUsuario(this.usuSession.getUsuarioLogado());
			this.setFluxoDePagina(Constants.INCLUSAO);
			this.financas = new ArrayList<>();
			this.financas = this.financaDAO.buscarFinancasPorCategoria(this.categoria, this.usuSession.getUsuarioLogado());
			this.gerarSaldo();
			this.financa = new Financa();
		} else {
			this.financa.setDataVencimento(null);
		}
	}

	public void adicionarFinanca() {
		if (garantirAnoCoerente(this.financa)) {
			if (this.opcao.equals("digitar") && this.getFluxoDePagina().equals(Constants.INCLUSAO)) {
				this.financas = new ArrayList<Financa>();
			}
			this.financas.add(this.financa);
			this.financa = new Financa();
			this.setFluxoDePagina(Constants.ALTERACAO);
		} else {
			this.financa.setDataVencimento(null);
		}
	}

	public void tirarDaLista(Financa fin) {
			this.financaDelegate.deletar(fin);
			this.financas = this.financaDAO.buscarFinancasPorCategoria(this.categoria,
					this.usuSession.getUsuarioLogado());
			this.gerarSaldo();
	}

	public void limpar() {
		this.financa = new Financa();
		this.financas = new ArrayList<Financa>();
		this.categoria = new CategoriaFinanca();

	}

	public void carregarFinancasPorCategoriaInclude() {
		if (categoria.getId() == null) {
			this.financas = this.financaDAO.buscarFinancasDoUsuario(this.usuSession.getUsuarioLogado());
			this.gerarSaldo();
			this.categoria.setNome("Gerais");
		} else {
			this.categoria = this.categoriaDelegate.buscarPorId(this.categoria.getId());
			this.financas = this.financaDAO.buscarFinancasPorCategoria(this.categoria,
					this.usuSession.getUsuarioLogado());
		}
		this.setFluxoDePagina(Constants.ALTERACAO);
	}

	public void carregarFinancasPorCate() {
		this.financas = new ArrayList<Financa>();
		if (categoria.getId() == null) {
			this.financas = this.financaDAO.buscarFinancasDoUsuario(this.usuSession.getUsuarioLogado());
			this.gerarSaldo();
			this.categoria.setNome("Gerais");
		} else {
			this.categoria = this.categoriaDelegate.buscarPorId(this.categoria.getId());
			this.financas = this.financaDAO.buscarFinancasPorCategoria(this.categoria,
					this.usuSession.getUsuarioLogado());
			this.gerarSaldo();
		}
	}

	public void editarUnidade(Financa fin) {
		try {
			this.financaDelegate.alterar(fin);
			this.session.addMessageInfo("Finança " + fin.getNome() + " alterada com sucesso!", "");
			this.financas = new ArrayList<>();
			this.financas = this.financaDAO.buscarFinancasPorCategoria(this.categoria, this.usuSession.getUsuarioLogado());
			this.gerarSaldo();
		} catch (Exception e) {
			this.session.addMessageError("Falha ao atualizar a Finança "+this.financa.getNome());
		}

	}

	public void onRowCancel(Financa fin) {
		session.addMessageInfo("Edição da Finança " + fin.getNome() + " cancelada.", "");
	}

	public String onFlowProcess(FlowEvent event) {
		return event.getNewStep();
	}

	public void gerarSaldo() {
		this.creditos = new BigDecimal("0.0");
		this.debitos = new BigDecimal("0.0");
		for (Financa fin : this.financas) {
			if (fin.getTipoFinanca().equals("CRÉDITO")) {
				this.setCreditos(this.getCreditos().add(fin.getValor()));
			}
			if (fin.getTipoFinanca().equals("DÉBITO")) {
				this.setDebitos(this.getDebitos().add(fin.getValor()));
			}
		}
		this.setSaldo(this.getCreditos().subtract(this.getDebitos()));
	}

	public void conferirData() {
		Calendar dataVencimento = this.financa.getDataVencimento();
		Calendar dataAtual = Calendar.getInstance();
//		if (dataAtual.after(dataVencimento)) {
//			this.financa.setVencida(true);
//		}
	}

	public void gerarDatasDeControle() {
		this.dataAtual = Calendar.getInstance();
		this.dataMaxima = Calendar.getInstance();
		this.dataMinima = Calendar.getInstance();
		this.dataMaxima.add(Calendar.YEAR, 1);
		dataMinima.add(Calendar.DATE, -365);
	}

	public Boolean garantirAnoCoerente(Financa financa) {
		Calendar dataAtual = Calendar.getInstance();
		Calendar dataVencimento = financa.getDataVencimento();
		int anoVencimento = dataVencimento.get(Calendar.YEAR);
		int mesVencimento = dataVencimento.get(Calendar.MONTH);
		int diaMesVencimento = dataVencimento.get(Calendar.DAY_OF_MONTH);

		int anoAtual = dataAtual.get(Calendar.YEAR);
		Calendar mix = dataVencimento;
		mix.set(Calendar.DAY_OF_MONTH, 1);
		int ultimoDiaMes = mix.getActualMaximum(Calendar.DAY_OF_MONTH);

		if (anoAtual - anoVencimento < 0 || (anoAtual - anoVencimento) >= 1) {
			session.addMessageInfo("A data informada excede um período válido", "");
			return false;
		}
		if (diaMesVencimento > ultimoDiaMes || diaMesVencimento < 0) {
			session.addMessageInfo("A data informada excede um período válido", "");
			return false;
		}
		if (mesVencimento < 1 || mesVencimento > 12) {
			return false;
		}
		return true;

	}
	
	 public void postProcessXLS(Object document) {
	        HSSFWorkbook wb = (HSSFWorkbook) document;
	        HSSFSheet sheet = wb.getSheetAt(0);
	        HSSFRow header = sheet.getRow(0);
	         
	        sheet.setHorizontallyCenter(true);
	       
	        
	        HSSFCellStyle cellStyle = wb.createCellStyle();  
	        cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
	        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	        
	        
	        
	        for(int i=0; i < header.getPhysicalNumberOfCells();i++) {
	            HSSFCell cell = header.getCell(i);
	             
	            cell.setCellStyle(cellStyle);
	        }
	    }
	     
	    public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {
	        Document pdf = (Document) document;
	        pdf.open();
	        pdf.setPageSize(PageSize.A4);
	        
	        
	        
	        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	        externalContext.getApplicationMap();
	        //pdf.add(arg0)
	        String logo = externalContext.getRealPath("") + File.separator + "resources" + File.separator + "imagens" +  File.separator + "logo_manageraccount.png";
	         
	        pdf.add(Image.getInstance(logo));
	        
	        pdf.addHeader("Total de Débitos",String.valueOf(debitos));
	        pdf.addHeader("Total de Créditos",String.valueOf(creditos));
	        pdf.addHeader("Saldo: ",String.valueOf(saldo));
	        
	        
	    }
	
	

	public Financa getFinanca() {
		return financa;
	}

	public void setFinanca(Financa financa) {
		this.financa = financa;
	}

	public CategoriaFinanca getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaFinanca categoria) {
		this.categoria = categoria;
	}

	public List<CategoriaFinanca> getCategoriaList() {
		return categoriaList;
	}

	public void setCategoriaList(List<CategoriaFinanca> categoriaList) {
		this.categoriaList = categoriaList;
	}

	public Integer getFluxoDePagina() {
		return fluxoDePagina;
	}

	public void setFluxoDePagina(Integer fluxoDePagina) {
		this.fluxoDePagina = fluxoDePagina;
	}

	public List<TipoFinanca> getTipoFinancaList() {
		return tipoFinancaList;
	}

	public void setTipoFinancaList(List<TipoFinanca> tipoFinancaList) {
		this.tipoFinancaList = tipoFinancaList;
	}

	public String getOpcao() {
		return opcao;
	}

	public void setOpcao(String opcao) {
		this.opcao = opcao;
	}

	public List<Financa> getFinancas() {
		return financas;
	}

	public void setFinancas(List<Financa> financas) {
		this.financas = financas;
	}

	public BigDecimal getDebitos() {
		return debitos;
	}

	public void setDebitos(BigDecimal debitos) {
		this.debitos = debitos;
	}

	public BigDecimal getCreditos() {
		return creditos;
	}

	public void setCreditos(BigDecimal creditos) {
		this.creditos = creditos;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public Calendar getDataAtual() {
		return dataAtual;
	}

	public void setDataAtual(Calendar dataAtual) {
		this.dataAtual = dataAtual;
	}

	public Calendar getDataMaxima() {
		return dataMaxima;
	}

	public void setDataMaxima(Calendar dataMaxima) {
		this.dataMaxima = dataMaxima;
	}

	public Calendar getDataMinima() {
		return dataMinima;
	}

	public void setDataMinima(Calendar dataMinima) {
		this.dataMinima = dataMinima;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

}

package br.com.controlefinanceiro.beans;

import java.awt.Color;
import java.awt.Point;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jws.soap.SOAPBinding.Style;
import javax.swing.plaf.metal.MetalIconFactory.FolderIcon16;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FontFamily;
import org.apache.poi.ss.util.CellRangeAddress;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;

import com.lowagie.text.BadElementException;
import com.lowagie.text.DocListener;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.html.simpleparser.StyleSheet;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

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
import br.com.controlefinanceiro.model.Usuario;
import br.com.controlefinanceiro.util.Constants;
import br.com.controlefinanceiro.util.GerarPDFUtil;

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
	private StreamedContent arquivo;
	private StreamedContent arquivoXLS;
	@Inject
	private GerarPDFUtil geradorPDF;
	

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
			CategoriaFinanca cateEx = this.categoriaDAO.buscarCategoriaPorNome(this.categoria.getNome(), this.usuSession.getUsuarioLogado());
			if(cateEx == null){
					CategoriaFinanca cate = new CategoriaFinanca();
					cate.setNome(this.categoria.getNome());
					cate.setIdUsuario(this.usuSession.getUsuarioLogado().getId());
					this.categoriaDelegate.simplesInsert(cate);
					this.categoriaList = new ArrayList<CategoriaFinanca>();
					this.categoriaList = this.categoriaDAO.buscarCategoriasDoUsuario(this.usuSession.getUsuarioLogado());
					cateEx = this.categoriaDAO.buscarCategoriaPorNome(this.categoria.getNome(), this.usuSession.getUsuarioLogado());
			}
			this.financa.setIdUsuario(this.usuSession.getUsuarioLogado().getId());
			this.financa.setCategoriaFinanca(cateEx);
			this.financaDelegate.inserir(this.financa);
			this.setFluxoDePagina(Constants.INCLUSAO);
			this.financas = new ArrayList<>();
			this.financas = this.financaDAO.buscarFinancasPorCategoria(cateEx, this.usuSession.getUsuarioLogado());
			this.categoriaList = new ArrayList<CategoriaFinanca>();
			this.categoriaList = this.categoriaDAO.buscarCategoriasDoUsuario(usuSession.getUsuarioLogado());
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
		this.setSaldo(this.creditos.subtract(this.debitos));
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
	
	    public StreamedContent  getArquivo()  {
	    	try {
				return this.geradorPDF.geradorPDF(categoria, this.usuSession.getUsuarioLogado(), financas, debitos, creditos, saldo);
			} catch (IOException | DocumentException e) {
				System.out.println("Erro ao gerar PDF: "+e);
				e.printStackTrace();
			}
	    	return null;
	    }
	    
	    public void excluirCategoria(){
	    	List<Financa> buscarFinancasPorCategoria = this.financaDAO.buscarFinancasPorCategoria(this.categoria, this.usuSession.getUsuarioLogado());
	    	for (Financa financa : buscarFinancasPorCategoria) {
				financaDAO.deletar(financa);
			}
	    	this.categoriaDAO.deletar(this.categoria);
	    	this.categoria = new CategoriaFinanca();
	    	this.financas = new ArrayList<Financa>();
	    	this.financas = this.financaDAO.buscarFinancasDoUsuario(this.usuSession.getUsuarioLogado());
	    	this.categoriaList = new ArrayList<>();
	    	this.categoriaList = this.categoriaDAO.buscarCategoriasDoUsuario(this.usuSession.getUsuarioLogado());
	    }
	
	    public StreamedContent getArquivoXLS() {

	    	HSSFWorkbook workbook = new HSSFWorkbook();
	    	HSSFSheet firstSheet = workbook.createSheet("FINANÇAS DO TIPO "+this.categoria.getNome());
	    	HSSFRow row = firstSheet.createRow(7);
	    	
	    	HSSFCellStyle cellStyle = workbook.createCellStyle();  
	        cellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
	        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	        
	        HSSFCellStyle cellStyleHeader = workbook.createCellStyle();  
	        cellStyleHeader.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
	        cellStyleHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	    	
	        HSSFFont fonte = workbook.createFont();
            fonte.setFontHeightInPoints((short)12);
            fonte.setItalic(true);
            
            cellStyleHeader.setFont(fonte);
	        
	    	row.createCell(4).setCellValue("USUÁRIO");
	    	row.getCell(4).setCellStyle(cellStyleHeader);
	    	row.createCell(5).setCellValue("DATA/ RELATÓRIO");
	    	row.getCell(5).setCellStyle(cellStyleHeader);
	    	row.createCell(6).setCellValue("MAIOR DÉBITO");
	    	row.getCell(6).setCellStyle(cellStyleHeader);
	    	row.createCell(7).setCellValue("MAIOR CRÉDITO");
	    	row.getCell(7).setCellStyle(cellStyleHeader);
	    	
	    	HSSFRow row1 = firstSheet.createRow(8);
	    	LocalDate dataAtual = LocalDate.now();
	    	BigDecimal gerarMaiorCredito = this.gerarMaiorCredito(financas);
	    	BigDecimal gerarMaiorDebito = this.gerarMaiorDebito(financas);
	    	row1.createCell(4).setCellValue(this.usuSession.getUsuarioLogado().getNome()+" "+this.usuSession.getUsuarioLogado().getSobrenome());
	    	row1.getCell(4).setCellStyle(cellStyleHeader);
	    	row1.createCell(5).setCellValue(dataAtual.toString());
	    	row1.getCell(5).setCellStyle(cellStyleHeader);
	    	row1.createCell(6).setCellValue(gerarMaiorDebito.toString());
	    	row1.getCell(6).setCellStyle(cellStyleHeader);
	    	row1.createCell(7).setCellValue(gerarMaiorCredito.toString());
	    	row1.getCell(7).setCellStyle(cellStyleHeader);
	    	
	    	HSSFRow row2 = firstSheet.createRow(9);
	    	
	    	
	    	HSSFRow row4HeaderTabela = firstSheet.createRow(10);
	    	row4HeaderTabela.createCell(4).setCellValue("FINANÇA");
	    	row4HeaderTabela.getCell(4).setCellStyle(cellStyle);
	    	row4HeaderTabela.createCell(5).setCellValue("DATA/ VENCIMENTO");
	    	row4HeaderTabela.getCell(5).setCellStyle(cellStyle);
	    	row4HeaderTabela.createCell(6).setCellValue("VALOR");
	    	row4HeaderTabela.getCell(6).setCellStyle(cellStyle);
	    	row4HeaderTabela.createCell(7).setCellValue("TIPO");
	    	row4HeaderTabela.getCell(7).setCellStyle(cellStyle);
	    	
	    	firstSheet.setColumnWidth((short) (4), (short) (10 * 700));
			firstSheet.setColumnWidth((short) (5), (short) (10 * 700));
			firstSheet.setColumnWidth((short) (6), (short) (10 * 700));
			firstSheet.setColumnWidth((short) (7), (short) (10 * 700));
	    	
	    	FileOutputStream file = null;

	    	try {
	    		
	    		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	        	//this.cleanDirectory(externalContext, categoria, usuario);
	            //file =  new FileOutputStream(externalContext.getRealPath("") + File.separator + "resources" + File.separator + "processos" +  File.separator+"relatorio_"+categoria.getNome().toLowerCase()+"_"+this.usuSession.getUsuarioLogado().getNome().toLowerCase()+"_"+this.usuSession.getUsuarioLogado().getSobrenome().toLowerCase()+".xls");
	    		
	    		/** Ligando a figura ao Workbook**/
		    	byte data[] = new byte[8000]; // o suficiente para caber a figura
		    	new DataInputStream(new FileInputStream(externalContext.getRealPath("") + File.separator + "resources" + File.separator + "imagens" +  File.separator + "logo_relatorio.jpg")).read(data);
		    	int index = workbook.addPicture(data, HSSFWorkbook.PICTURE_TYPE_JPEG);
		    	/*Ligando a figura ao Sheet*/
		    	HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0,(short) 5, 1, (short) (5 + 2), 5);
		    	firstSheet.createDrawingPatriarch().createPicture(anchor, index);
		    	
	    		
	    		
	    		
	    		file = new FileOutputStream(new File(externalContext.getRealPath("") + File.separator + "resources" + File.separator + "processos" +  File.separator+"relatorio_"+categoria.getNome().toLowerCase()+"_"+this.usuSession.getUsuarioLogado().getNome().toLowerCase()+"_"+this.usuSession.getUsuarioLogado().getSobrenome().toLowerCase()+".xls"));
	    		int linha = 11;
	    		for (Financa financa: this.financas) {
	    			HSSFRow linhaValue = firstSheet.createRow(linha);
	    			linhaValue.createCell(4).setCellValue(financa.getNome());
	    			Calendar calendar = financa.getDataVencimento();
	          	  	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	          	  	String dataString = simpleDateFormat.format(calendar.getTime());
	          	  	linhaValue.createCell(5).setCellValue(dataString);
	    			linhaValue.createCell(6).setCellValue(financa.getValor().toString());
	    			linhaValue.createCell(7).setCellValue(financa.getTipoFinanca());
	    			linha++;
	    			
	    		}
	    		
	    		HSSFRow linhaB2= firstSheet.createRow(linha);
	    		
	    		linha++;
	    		HSSFRow linhaSaldo = firstSheet.createRow(linha);
	    		firstSheet.addMergedRegion(new CellRangeAddress(linha,linha,4,7));
	    		linhaSaldo.createCell(4).setCellValue("SALDO DA CATEGORIA "+this.categoria.getNome().toUpperCase());
	    		linhaSaldo.getCell(4).setCellStyle(cellStyle);
	    		
	    		linha++;
	    		HSSFRow linhaSaldo1 = firstSheet.createRow(linha);
	    		linhaSaldo1.createCell(4).setCellValue("TOTAL DE DÉBITOS");
	    		linhaSaldo1.getCell(4).setCellStyle(cellStyleHeader);
	    		linhaSaldo1.createCell(5).setCellValue(this.debitos.toString());
	    		linhaSaldo1.getCell(5).setCellStyle(cellStyleHeader);

	    		linha++;
	    		HSSFRow linhaSaldo2 = firstSheet.createRow(linha);
	    		linhaSaldo2.createCell(4).setCellValue("TOTAL DE CRÉDITOS");
	    		linhaSaldo2.getCell(4).setCellStyle(cellStyleHeader);
	    		linhaSaldo2.createCell(5).setCellValue(this.creditos.toString());
	    		linhaSaldo2.getCell(5).setCellStyle(cellStyleHeader);
	    		
	    		linha++;
	    		HSSFRow linhaSaldo3 = firstSheet.createRow(linha);
	    		linhaSaldo3.createCell(4).setCellValue("SALDO");
	    		linhaSaldo3.getCell(4).setCellStyle(cellStyleHeader);
	    		linhaSaldo3.createCell(5).setCellValue(this.saldo.toString());
	    		linhaSaldo3.getCell(5).setCellStyle(cellStyleHeader);
	    		
	    		workbook.write(file);
	    		file.close();
	    		file.flush();
	    		InputStream resourceAsStream = externalContext.getResourceAsStream("/resources/processos/"+"relatorio_"+categoria.getNome().toLowerCase()+"_"+this.usuSession.getUsuarioLogado().getNome().toLowerCase()+"_"+this.usuSession.getUsuarioLogado().getSobrenome().toLowerCase()+".xls");
	    		this.arquivoXLS = new DefaultStreamedContent(resourceAsStream,"application/xls","relatorio_"+categoria.getNome().toLowerCase()+"_"+this.usuSession.getUsuarioLogado().getNome().toLowerCase()+"_"+this.usuSession.getUsuarioLogado().getSobrenome().toLowerCase()+".xls");
	    		return arquivoXLS;  
				
	    	}catch (Exception e) {
			}
	    	return null;
			
	    }
	
	    
	    public BigDecimal gerarMaiorDebito(List<Financa> financas){
			 BigDecimal maiorDebito = new BigDecimal("0");
			 for (Financa financa : financas) {
				if(financa.getTipoFinanca().equals("DÉBITO")){
					if(financa.getValor().compareTo(maiorDebito) == 1){
						maiorDebito = financa.getValor();
					}
				}
			}
			 return maiorDebito;
		 }
		 
		 public BigDecimal gerarMaiorCredito(List<Financa> financas){
			 BigDecimal maiorCredito = new BigDecimal("0");
			 for (Financa financa : financas) {
				if(financa.getTipoFinanca().equals("CRÉDITO")){
					if(financa.getValor().compareTo(maiorCredito) == 1){
						maiorCredito = financa.getValor();
					}
				}
			}
			 return maiorCredito;
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

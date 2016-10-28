package br.com.controlefinanceiro.util;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import br.com.controlefinanceiro.model.CategoriaFinanca;
import br.com.controlefinanceiro.model.Financa;
import br.com.controlefinanceiro.model.Usuario;

@Stateless
public class GerarPDFUtil {
	
	private List<Financa> financas;
	
	public StreamedContent  geradorPDF(CategoriaFinanca categoria, Usuario usuario, List<Financa> financas, BigDecimal debitos, BigDecimal creditos, BigDecimal saldo) throws IOException, BadElementException, DocumentException {
	    this.financas = financas;
    	Document pdf = new Document();
    	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    	this.cleanDirectory(externalContext, categoria, usuario);
        PdfWriter.getInstance(pdf, new FileOutputStream(externalContext.getRealPath("") + File.separator + "resources" + File.separator + "processos" +  File.separator+"relatorio_"+categoria.getNome().toLowerCase()+"_"+usuario.getNome().toLowerCase()+"_"+usuario.getSobrenome().toLowerCase()+".pdf"));
          pdf.open();
          pdf.addKeywords("www.manageraccount.com.br");
          pdf.addAuthor("©ManagerAccount");
          String logo = externalContext.getRealPath("") + File.separator + "resources" + File.separator + "imagens" +  File.separator + "logo_manager_relatorio.png";
          Image image = Image.getInstance(logo);
          image.setAlignment(Image.ALIGN_CENTER | Image.TEXTWRAP);
          pdf.add(image);
          pdf.add(new Paragraph(" "));
          pdf.add(new Paragraph(" "));
          Font titulo = new Font(BaseFont.FONT_TYPE_T1, 13, Font.BOLD);
          Font tituloCredi = new Font(BaseFont.FONT_TYPE_T1, 13, Font.BOLD);
          tituloCredi.setColor(Color.blue);
          Font tituloDeb = new Font(BaseFont.FONT_TYPE_T1, 13, Font.BOLD);
          tituloDeb.setColor(Color.RED);
          Font conteudoTable = new Font(BaseFont.FONT_TYPE_T3,10,Font.ITALIC);
          conteudoTable.setColor(Color.black);
          Font tituloColuna = new Font(BaseFont.FONT_TYPE_CJK,10, Font.NORMAL);
          tituloColuna.setColor(Color.BLUE);
          titulo.setColor(Color.DARK_GRAY);
          
          this.gerarHeader(usuario, pdf, tituloColuna, conteudoTable);
          PdfPTable table = new PdfPTable(new float[]{0.40f, 0.19f, 0.19f, 0.19f});
          
          PdfPCell header = new PdfPCell(new Paragraph("RESUMO DA CATEGORIA "+categoria.getNome().toUpperCase(),titulo));
          header.setColspan(4);
          header.setBorderWidthTop(1.5f);
          header.setBorderColorTop(Color.GREEN);
          header.setBorder(Rectangle.TOP);
          table.addCell(header);
          
          PdfPTable tableSaldo = new PdfPTable(new float[]{0.3f, 0.3f});
          PdfPCell headerSaldo = new PdfPCell(new Paragraph("SALDO DA CATEGORIA "+categoria.getNome().toUpperCase(), titulo));
          PdfPCell pNome = new PdfPCell();
          pNome.setBackgroundColor(Color.LIGHT_GRAY);
          PdfPCell pVen = new PdfPCell();
          pVen.setBackgroundColor(Color.LIGHT_GRAY);
          PdfPCell pVal = new PdfPCell();
          pVal.setBackgroundColor(Color.LIGHT_GRAY);
          PdfPCell pTipo = new PdfPCell();
          pTipo.setBackgroundColor(Color.LIGHT_GRAY);
          headerSaldo.setColspan(2);
          
          tableSaldo.addCell(headerSaldo);
          Paragraph colNome = new Paragraph("NOME",tituloColuna);
          Paragraph colVen= new Paragraph("VENCIMENTO",tituloColuna);
          Paragraph colVal = new Paragraph("VALOR",tituloColuna);
          Paragraph colTipo = new Paragraph("TIPO",tituloColuna);
          
          pNome.addElement(colNome);
          pVen.addElement(colVen);
          pVal.addElement(colVal);
          pTipo.addElement(colTipo);
          
          table.addCell(pNome);
          table.addCell(pVen);
          table.addCell(pVal);
          table.addCell(pTipo);
          for (Financa categoriaFinanca : financas) {
        	  Paragraph paragraph1 = new Paragraph(categoriaFinanca.getNome(),conteudoTable);
        	  
        	  Calendar calendar = categoriaFinanca.getDataVencimento();
        	  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        	  String dataString = simpleDateFormat.format(calendar.getTime());
        	  
        	  Paragraph paragraph2 = new Paragraph(dataString,conteudoTable);
        	  Paragraph paragraph3 = new Paragraph("R$"+categoriaFinanca.getValor().toString(),conteudoTable);
        	  Paragraph paragraph4 = new Paragraph(categoriaFinanca.getTipoFinanca(),conteudoTable);
        	  table.addCell(paragraph1);
        	  table.addCell(paragraph2);
        	  table.addCell(paragraph3);
        	  table.addCell(paragraph4);
          }
         
          pdf.add(table);
          pdf.add(new Paragraph(" "));
          Paragraph pTiDeb = new Paragraph("TOTAL DE DÉBITOS",tituloColuna);
          Paragraph deb = new Paragraph("R$"+String.valueOf(debitos),tituloDeb);
          Paragraph pTiCred= new Paragraph("TOTAL DE CRÉDITOS",tituloColuna);
          Paragraph cred = new Paragraph("R$"+String.valueOf(creditos),tituloCredi);
          Paragraph pTiSaldo = new Paragraph("SALDO",tituloColuna);
          Paragraph sal = new Paragraph();
          
          if(creditos.compareTo(debitos) == 1){
        	  sal = new Paragraph("R$"+saldo,tituloCredi);
          }else{
        	  sal = new Paragraph("R$"+saldo,tituloDeb);
          }
          tableSaldo.addCell(pTiCred);
          tableSaldo.addCell(cred);
          tableSaldo.addCell(pTiDeb);
          tableSaldo.addCell(deb);
          tableSaldo.addCell(pTiSaldo);
          tableSaldo.addCell(sal);
          pdf.add(tableSaldo);
         
          pdf.close();
          
          InputStream resourceAsStream = externalContext.getResourceAsStream("/resources/processos/"+"relatorio_"+categoria.getNome().toLowerCase()+"_"+usuario.getNome().toLowerCase()+"_"+usuario.getSobrenome().toLowerCase()+".pdf");
          StreamedContent arquivo = new DefaultStreamedContent(resourceAsStream,"application/pdf","relatorio_"+categoria.getNome().toLowerCase()+"_"+usuario.getNome().toLowerCase()+"_"+usuario.getSobrenome().toLowerCase()+".pdf");
          return arquivo;
    }
	
	public void gerarHeader(Usuario usuario, Document pdf, Font tituloColuna, Font conteudoTable) throws DocumentException{
		 PdfPTable tableuSER = new PdfPTable(new float[]{0.25f, 0.25f, 0.25f, 0.25f});
		 Paragraph colNome = new Paragraph("USUÁRIO",tituloColuna);
         Paragraph colVen= new Paragraph("DATA/ RELATÓRIO",tituloColuna);
         Paragraph colVal = new Paragraph("MAIOR DÉBITO",tituloColuna);
         Paragraph colTipo = new Paragraph("MAIOR CRÉDITO",tituloColuna);
         
         PdfPCell pNome = new PdfPCell();
         pNome.setBackgroundColor(Color.LIGHT_GRAY);
         PdfPCell pVen = new PdfPCell();
         pVen.setBackgroundColor(Color.LIGHT_GRAY);
         PdfPCell pVal = new PdfPCell();
         pVal.setBackgroundColor(Color.LIGHT_GRAY);
         PdfPCell pTipo = new PdfPCell();
         pTipo.setBackgroundColor(Color.LIGHT_GRAY);
         
         pNome.addElement(colNome);
         pVen.addElement(colVen);
         pVal.addElement(colVal);
         pTipo.addElement(colTipo);
         
         tableuSER.addCell(pNome);
         tableuSER.addCell(pVen);
         tableuSER.addCell(pVal);
         tableuSER.addCell(pTipo);
         
         
      //   LocalDate dataAtual = LocalDate.now();
        Calendar calendar = new GregorianCalendar().getInstance();
 		SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
 		String dataString = s.format(calendar.getTime());
         
         
         BigDecimal maiorCredito = this.gerarMaiorCredito(this.financas);
         BigDecimal maiorDebito = this.gerarMaiorDebito(this.financas);
         
 		 Paragraph paragraph1 = new Paragraph(usuario.getNome()+" "+usuario.getSobrenome(),conteudoTable);
	   	 Paragraph paragraph2 = new Paragraph(dataString,conteudoTable);
	   	 Paragraph paragraph3 = new Paragraph("R$-"+maiorDebito,conteudoTable);
	   	 Paragraph paragraph4 = new Paragraph("R$"+maiorCredito,conteudoTable);
	   	 tableuSER.addCell(paragraph1);
	   	 tableuSER.addCell(paragraph2);
	   	 tableuSER.addCell(paragraph3);
	   	 tableuSER.addCell(paragraph4);
	   	 pdf.add(tableuSER);
	   	 pdf.add(new Paragraph(" "));
	}
	
	 public File cleanDirectory(ExternalContext externalContext, CategoriaFinanca categoria, Usuario usuario){
		 File diretorio = new File(externalContext.getRealPath("")+File.separator+"resources"+File.separator+"processos");
		 File[] listFiles = diretorio.listFiles();
		 for (File file : listFiles) {
			System.out.println("Arquivo: "+file.getName());
		}
		 	if(diretorio.isDirectory()){
				for (File arquivos : diretorio.listFiles()) {
					if(arquivos.getName().equals(categoria.getNome().toLowerCase()+"_"+usuario.getNome().
							toLowerCase()+".pdf")){
							arquivos.delete();
					}
				}
			}
			
			return diretorio;
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
	 
	 
	

}

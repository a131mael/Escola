package org.escola.rest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.servlet.ServletContext;

import org.aaf.financeiro.model.Pagador;
import org.aaf.financeiro.sicoob.util.CNAB240_SICOOB;
import org.escola.controller.OfficeDOCUtil;
import org.escola.model.Aluno;
import org.escola.model.ContratoAluno;
import org.escola.service.AlunoService;

/** Endpoints usados pelo portal do responsável (app Python separado) pra gerar os
 * documentos (boleto em PDF e comprovante de pagamento) que dependem das mesmas
 * bibliotecas Java já usadas pelo sistema admin — tudo o mais (listar crianças,
 * boletos, atualizar dados/senha) o portal faz direto no banco Escola. */
@Stateless
@Path("/portal")
public class PortalRest {

	@Inject
	private AlunoService alunoService;

	@Context
	private ServletContext servletContext;

	@GET
	@Path("/boleto/{idboleto}")
	@Produces("application/pdf")
	public Response getBoletoPDF(@PathParam("idboleto") Long idboleto) {
		try {
			org.escola.model.Boleto boleto = alunoService.findBoletoById(idboleto);
			if (boleto == null || boleto.getContrato() == null) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			ContratoAluno contrato = boleto.getContrato();
			Aluno aluno = contrato.getAluno();

			CNAB240_SICOOB cnab = new CNAB240_SICOOB(2);

			Pagador pagador = new Pagador();
			pagador.setBairro(contrato.getBairro());
			pagador.setCep(contrato.getCep());
			pagador.setCidade(contrato.getCidade() != null ? contrato.getCidade() : "PALHOCA");
			pagador.setCpfCNPJ(contrato.getCpfResponsavel());
			pagador.setEndereco(contrato.getEndereco());
			pagador.setNome(contrato.getNomeResponsavel() + "   (" + aluno.getNomeAluno() + ")");
			pagador.setNossoNumero(boleto.getNossoNumero() + "");
			pagador.setUF("SC");

			List<org.aaf.financeiro.model.Boleto> boletos = new ArrayList<>();
			org.aaf.financeiro.model.Boleto b = new org.aaf.financeiro.model.Boleto();
			b.setEmissao(boleto.getEmissao());
			b.setId(boleto.getId());
			b.setNossoNumero(String.valueOf(boleto.getNossoNumero()));
			b.setValorNominal(boleto.getValorNominal());
			b.setVencimento(boleto.getVencimento());
			boletos.add(b);
			pagador.setBoletos(boletos);

			byte[] pdf = cnab.getBoletoPDF(pagador);

			return Response.ok(pdf)
					.header("Content-Disposition", "inline; filename=boleto_" + idboleto + ".pdf")
					.build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Path("/comprovante/{idcontrato}")
	@Produces("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
	public Response getComprovantePagamento(@PathParam("idcontrato") Long idcontrato) {
		try {
			ContratoAluno contrato = alunoService.findContratoById(idcontrato);
			if (contrato == null || contrato.getAluno() == null) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			Aluno aluno = contrato.getAluno();

			DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL, new Locale("pt", "BR"));
			String dataExtenso = formatador.format(new java.util.Date());

			java.text.NumberFormat formatadorMoeda = java.text.NumberFormat.getInstance(new Locale("pt", "BR"));
			formatadorMoeda.setMinimumFractionDigits(2);
			formatadorMoeda.setMaximumFractionDigits(2);

			double valorPago = 0d;
			int parcelasPagas = 0;
			if (contrato.getBoletos() != null) {
				for (org.escola.model.Boleto boleto : contrato.getBoletos()) {
					if (Boolean.TRUE.equals(boleto.getCancelado())) {
						continue;
					}
					if (boleto.getDataPagamento() != null && boleto.getValorPago() != null) {
						valorPago += boleto.getValorPago();
						parcelasPagas++;
					}
				}
			}

			HashMap<String, String> trocas = new HashMap<>();
			trocas.put("adonainomealuno", aluno.getNomeAluno());
			trocas.put("adonaiturma", aluno.getSerie() != null ? aluno.getSerie().getName() : "");
			trocas.put("adonaiperiodo", aluno.getPeriodo() != null ? aluno.getPeriodo().getName() : "");
			trocas.put("adonaidata", dataExtenso);
			trocas.put("adonaicpfresponsavel", contrato.getCpfResponsavel());
			trocas.put("adonainomeresponsavel", contrato.getNomeResponsavel());
			trocas.put("adonaiano", contrato.getAno() + "");
			trocas.put("adonaianuidade", formatadorMoeda.format(valorPago));
			trocas.put("adonaiparcelas", parcelasPagas + "");
			trocas.put("adonaivalorparcelas", formatadorMoeda.format(contrato.getValorMensal()));

			String caminhoTemplate = servletContext.getRealPath("/modeloNegativoDebito2017.docx");
			String nomeArquivoSaida = "comprovante_" + idcontrato + ".doc";
			String caminhoSaida = System.getProperty("java.io.tmpdir") + File.separator + nomeArquivoSaida;

			new OfficeDOCUtil().editDoc2CaminhoAbsoluto(caminhoTemplate, trocas, caminhoSaida);

			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			try (FileInputStream in = new FileInputStream(caminhoSaida)) {
				byte[] chunk = new byte[8192];
				int lidos;
				while ((lidos = in.read(chunk)) != -1) {
					buffer.write(chunk, 0, lidos);
				}
			}

			return Response.ok(buffer.toByteArray())
					.header("Content-Disposition", "inline; filename=comprovante_" + idcontrato + ".doc")
					.build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
}

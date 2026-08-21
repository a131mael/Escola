package org.escola.model;

import java.io.Serializable;

public class PedidoCancelamento implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Long alunoId;
	private String nomeAluno;
	private String numeroContrato;
	private String ano;
	private String nomeResponsavel;
	private String motivo;
	private String dataPedido;
	private String dataUltimoUso;
	private String valorMulta;
	private String detalheMensalidades;

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public Long getAlunoId() { return alunoId; }
	public void setAlunoId(Long alunoId) { this.alunoId = alunoId; }

	public String getNomeAluno() { return nomeAluno; }
	public void setNomeAluno(String nomeAluno) { this.nomeAluno = nomeAluno; }

	public String getNumeroContrato() { return numeroContrato; }
	public void setNumeroContrato(String numeroContrato) { this.numeroContrato = numeroContrato; }

	public String getAno() { return ano; }
	public void setAno(String ano) { this.ano = ano; }

	public String getNomeResponsavel() { return nomeResponsavel; }
	public void setNomeResponsavel(String nomeResponsavel) { this.nomeResponsavel = nomeResponsavel; }

	public String getMotivo() { return motivo; }
	public void setMotivo(String motivo) { this.motivo = motivo; }

	public String getDataPedido() { return dataPedido; }
	public void setDataPedido(String dataPedido) { this.dataPedido = dataPedido; }

	public String getDataUltimoUso() { return dataUltimoUso; }
	public void setDataUltimoUso(String dataUltimoUso) { this.dataUltimoUso = dataUltimoUso; }

	public String getValorMulta() { return valorMulta; }
	public void setValorMulta(String valorMulta) { this.valorMulta = valorMulta; }

	public String getDetalheMensalidades() { return detalheMensalidades; }
	public void setDetalheMensalidades(String detalheMensalidades) { this.detalheMensalidades = detalheMensalidades; }
}

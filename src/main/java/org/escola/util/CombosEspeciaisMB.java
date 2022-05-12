/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.escola.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import org.escola.enums.BimestreEnum;
import org.escola.enums.DisciplinaEnum;
import org.escola.enums.EspecialidadeEnum;
import org.escola.enums.PerioddoEnum;
import org.escola.enums.Serie;
import org.escola.enums.Sexo;
import org.escola.enums.TipoDestinatario;
import org.escola.enums.TipoMembro;
import org.escola.model.Professor;
import org.escola.service.ConfiguracaoService;
import org.escola.service.ProfessorService;

import br.com.aaf.base.importacao.extrato.ExtratoGruposPagamentoRecebimentoAdonaiEnum;
import br.com.aaf.base.importacao.extrato.ExtratoTiposEntradaEnum;
import br.com.aaf.base.importacao.extrato.ExtratoTiposEntradaSaidaEnum;

/**
 *
 * @author abimael Fidencio Combos na aplicação.
 */
@ManagedBean(name = "CombosEspeciaisMB")
@LocalBean
@ApplicationScoped
public class CombosEspeciaisMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ProfessorService professorService;
	
	@Inject
	private ConfiguracaoService configuracaoService;

	public ArrayList<SelectItem> getProfessores() {
		ArrayList<SelectItem> items = new ArrayList<SelectItem>();
		try {
			// items.add(new SelectItem(null, "Selecione um País"));

			List<Professor> professores = professorService.findAll();
			for (Professor m : professores) {
				items.add(new SelectItem(m, m.getNome()));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return items;
	}
	
	public static Integer[] getAnos() {
		Integer[] anos = {2020,2021,2022,2023,2024,2025};
		return anos;
	}
	
	public BimestreEnum getBimestreAtual(){
		return configuracaoService.findAll().get(0).getBimestre();
	}
	
	public ArrayList<SelectItem> getSimNao() {
		ArrayList<SelectItem> items = new ArrayList<SelectItem>();
		try {
			 items.add(new SelectItem(false, "Não"));
			 items.add(new SelectItem(true, "Sim"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return items;
	}

	public ArrayList<SelectItem> getSimNaoNull() {
		ArrayList<SelectItem> items = new ArrayList<SelectItem>();
		try {
			items.add(new SelectItem(null, ""));
			 items.add(new SelectItem(Boolean.FALSE, "Não"));
			 items.add(new SelectItem(Boolean.TRUE, "Sim"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return items;
	}

	
	public ArrayList<SelectItem> getPeriodosSelectIItem() {
		ArrayList<SelectItem> items = new ArrayList<SelectItem>();
		try {
			 items.add(new SelectItem(null, " "));
			for (PerioddoEnum m : PerioddoEnum.values()) {
				items.add(new SelectItem(m, m.getName()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return items;
	}
	
	public ArrayList<SelectItem> getDestinatariosSelectIItem() {
		ArrayList<SelectItem> items = new ArrayList<SelectItem>();
		try {
			 items.add(new SelectItem(null, " "));
			for (TipoDestinatario td : TipoDestinatario.values()) {
				items.add(new SelectItem(td, td.getName()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return items;
	}

	public ArrayList<SelectItem> getBimestreSelectIItem() {
		ArrayList<SelectItem> items = new ArrayList<SelectItem>();
		try {
			 items.add(new SelectItem(null, " "));
			for (BimestreEnum m : BimestreEnum.values()) {
				items.add(new SelectItem(m, m.getName()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return items;
	}
	
	public ArrayList<SelectItem> getDisciplinasSelectIItem() {
		ArrayList<SelectItem> items = new ArrayList<SelectItem>();
		try {
			 items.add(new SelectItem(null, " "));
			for (DisciplinaEnum m : DisciplinaEnum.values()) {
				items.add(new SelectItem(m, m.getName()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return items;
	}

	
	public ArrayList<SelectItem> getSeriesSelectIItem() {
		ArrayList<SelectItem> items = new ArrayList<SelectItem>();
		try {
			 items.add(new SelectItem(null, " "));
			for (Serie m : Serie.values()) {
				items.add(new SelectItem(m, m.getName()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return items;
	}

	public ArrayList<SelectItem> getSexoSelectIItem() {
		ArrayList<SelectItem> items = new ArrayList<SelectItem>();
		try {
			 items.add(new SelectItem(null, " "));
			for (Sexo m : Sexo.values()) {
				items.add(new SelectItem(m, m.getName()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return items;
	}

	public static Serie[] getSeries() {

		return Serie.values();
	}

	public static Sexo[] getSexo() {

		return Sexo.values();
	}

	
	public static PerioddoEnum[] getPeriodos() {

		return PerioddoEnum.values();
	}

	public static EspecialidadeEnum[] getEspecialidades() {

		return EspecialidadeEnum.values();
	}

	public static DisciplinaEnum[] getDisciplinas() {

		return DisciplinaEnum.values();
	}

	public static BimestreEnum[] getBimestres() {

		return BimestreEnum.values();
	}
	
	public static TipoMembro[] getTipoMembro() {

		return TipoMembro.values();
	}
	
	public ArrayList<SelectItem> getMeses() {
		ArrayList<SelectItem> items = new ArrayList<SelectItem>();
		try {
			 items.add(new SelectItem(-1, "Selecione"));
			 items.add(new SelectItem(0, "Janeiro"));
			 items.add(new SelectItem(1, "Fevereiro"));
			 items.add(new SelectItem(2, "Março"));
			 items.add(new SelectItem(3, "Abril"));
			 items.add(new SelectItem(4, "Maio"));
			 items.add(new SelectItem(5, "junho"));
			 items.add(new SelectItem(6, "Julho"));
			 items.add(new SelectItem(7, "Agosto"));
			 items.add(new SelectItem(8, "Setembro"));
			 items.add(new SelectItem(9, "Outubro"));
			 items.add(new SelectItem(10, "Novembro"));
			 items.add(new SelectItem(11, "Dezembro"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}
	
	public ArrayList<SelectItem> getMesesValorInteiroCorreto() {
		ArrayList<SelectItem> items = new ArrayList<SelectItem>();
		try {
			 items.add(new SelectItem(-1, "Selecione"));
			 items.add(new SelectItem(1, "Janeiro"));
			 items.add(new SelectItem(2, "Fevereiro"));
			 items.add(new SelectItem(3, "Março"));
			 items.add(new SelectItem(4, "Abril"));
			 items.add(new SelectItem(5, "Maio"));
			 items.add(new SelectItem(6, "junho"));
			 items.add(new SelectItem(7, "Julho"));
			 items.add(new SelectItem(8, "Agosto"));
			 items.add(new SelectItem(9, "Setembro"));
			 items.add(new SelectItem(10, "Outubro"));
			 items.add(new SelectItem(11, "Novembro"));
			 items.add(new SelectItem(12, "Dezembro"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	public ArrayList<SelectItem> getExtratoTiposEntradaSaidaSelectItem() {
		ArrayList<SelectItem> items = new ArrayList<SelectItem>();
		try {
			 items.add(new SelectItem(null, " "));
			for (ExtratoTiposEntradaSaidaEnum m : ExtratoTiposEntradaSaidaEnum.values()) {
				items.add(new SelectItem(m, m.getNome()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return items;
	}
	
	public static ExtratoTiposEntradaSaidaEnum[] getExtratoTiposEntradaSaidaEnum() {
		ExtratoTiposEntradaSaidaEnum[] extratos = ExtratoTiposEntradaSaidaEnum.values();
		
		return extratos;
	}
	
	public static ExtratoTiposEntradaEnum[] getExtratoTiposEntradaEnum() {

		return ExtratoTiposEntradaEnum.values();
	}
	
	public static ExtratoGruposPagamentoRecebimentoAdonaiEnum[] getExtratoGruposPagamentoRecebimentoEnum() {
		ExtratoGruposPagamentoRecebimentoAdonaiEnum[] grupos =  ExtratoGruposPagamentoRecebimentoAdonaiEnum.values();
		
		return grupos;
	}

	public ArrayList<SelectItem> getExtratoGruposPagamentoRecebimentoEnumSelectItem() {
		ArrayList<SelectItem> items = new ArrayList<SelectItem>();
		try {
			 items.add(new SelectItem(null, " "));
			for (ExtratoGruposPagamentoRecebimentoAdonaiEnum m : ExtratoGruposPagamentoRecebimentoAdonaiEnum.values()) {
				items.add(new SelectItem(m, m.getNome()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return items;
	}
	
	public ArrayList<SelectItem> getExtratoTiposEntradaSelectItem() {
		ArrayList<SelectItem> items = new ArrayList<SelectItem>();
		try {
			 items.add(new SelectItem(null, " "));
			for (ExtratoTiposEntradaEnum m : ExtratoTiposEntradaEnum.values()) {
				items.add(new SelectItem(m, m.getNameReal()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return items;
	}
	

	
}

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
import org.escola.enums.TipoMembro;
import org.escola.model.Professor;
import org.escola.service.ConfiguracaoService;
import org.escola.service.ProfessorService;

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
	
	public BimestreEnum getBimestreAtual(){
		return configuracaoService.findAll().get(0).getBimestre();
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

}

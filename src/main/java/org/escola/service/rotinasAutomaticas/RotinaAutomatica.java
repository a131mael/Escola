package org.escola.service.rotinasAutomaticas;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.escola.model.AlunoAula;
import org.escola.service.AulaService;

@Singleton
@Startup
public class RotinaAutomatica {

	@Inject
	private org.escola.service.AlunoService alunoService;

	@Inject
	private AulaService aulaService;
	
	@Schedule(hour="*/13",  persistent = false)
	public void removerAlunosSemContratoAtivo() {
		try {
			System.out.println("Cancelando crianças sem contrato ativo ");
			alunoService.cancelarAlunosSemContratoAtivo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Schedule(hour="*/15",  persistent = false)
	public void colocarAlunosNaListaCobranca() {
		try {
			System.out.println("Colocar alunos na Lista de cobrança ");
			alunoService.colocarAlunosNaListaDeCobranca();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Schedule(hour="21", minute="30",  persistent = false)
	public void disponibilizarAulas() {
		try {
			System.out.println("Habilitando aulas ");
			aulaService.habilitarAulas();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

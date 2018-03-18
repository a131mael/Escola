/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.escola.controller.turma;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.escola.auth.AuthController;
import org.escola.controller.OfficeDOCUtil;
import org.escola.enums.BimestreEnum;
import org.escola.enums.DisciplinaEnum;
import org.escola.enums.PerioddoEnum;
import org.escola.enums.Serie;
import org.escola.enums.TipoMembro;
import org.escola.model.Aluno;
import org.escola.model.AlunoAvaliacao;
import org.escola.model.AlunoTurma;
import org.escola.model.Avaliacao;
import org.escola.model.Professor;
import org.escola.model.Turma;
import org.escola.service.AlunoService;
import org.escola.service.AvaliacaoService;
import org.escola.service.ProfessorService;
import org.escola.service.TurmaService;
import org.escola.util.CompactadorZip;
import org.escola.util.FileDownload;
import org.escola.util.ImpressoesUtils;
import org.escola.util.Util;
import org.primefaces.model.DualListModel;
import org.primefaces.model.StreamedContent;

import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

@Named
@ViewScoped
public class TurmaController extends AuthController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Produces
	@Named
	private Turma turma;

	@Inject
	private AvaliacaoService avaliacaoService;

	private float valorSelecionado;

	private int totalAlunos = 0;
	
	@Named
	private DisciplinaEnum disciplinaSelecionada;

	@Named
	private BimestreEnum bimestreSelecionado;

	@Inject
	private TurmaService turmaService;

	@Inject
	private ProfessorService professorService;

	@Inject
	private AlunoService alunoService;
	
	//private List<AlunoAvaliacao> alunoAvaliacaoPortugues;
	private Map<Aluno,List<AlunoAvaliacao>> alunoAvaliacaoPortugues;
	private Map<Aluno,List<AlunoAvaliacao>> alunoAvaliacaoIngles;
	private Map<Aluno,List<AlunoAvaliacao>> alunoAvaliacaoMatematica;
	private Map<Aluno,List<AlunoAvaliacao>> alunoAvaliacaoHistoria;
	private Map<Aluno,List<AlunoAvaliacao>> alunoAvaliacaoEDFisica;
	private Map<Aluno,List<AlunoAvaliacao>> alunoAvaliacaoGeografia;
	private Map<Aluno,List<AlunoAvaliacao>> alunoAvaliacaoCiencias;
	private Map<Aluno,List<AlunoAvaliacao>> alunoAvaliacaoFormacaoCrista;
	private Map<Aluno,List<AlunoAvaliacao>> alunoAvaliacaoArtes;

	private DualListModel<Professor> professores;

	private DualListModel<Aluno> alunos;
	/* private DualListModel<Professor> professores; */
	
	private List<AlunoAvaliacao> alunosAvaliacao;

	@PostConstruct
	private void init() {
		// TODO SOMENTE CARREGAR OS PICKLISTA pra quem tem permissao de ver
		if (turma == null) {
			Object objectSessao = Util.getAtributoSessao("turma");
			if (objectSessao != null) {
				turma = (Turma) objectSessao;
				Util.removeAtributoSessao("turma");
			} else {
				turma = new Turma();
			}
		}

		montarPickListProfessor();
		montarPickListAluno(turma.getSerie(), turma.getPeriodo());
		popularAlunoAvaliacao();
	}

	private void montarPickListAluno(Serie serie, PerioddoEnum periodo) {

		/** MONTANDO O PICKLIST */
		List<Aluno> todosAlunosDisponiveis = alunoService.findAll(serie, periodo);
		List<Aluno> alunosDisponiveis = new ArrayList<>();
		List<Aluno> alunosSelecionados = getAlunosSelecionados();

		alunosDisponiveis.addAll(todosAlunosDisponiveis);
		alunosDisponiveis.removeAll(alunosSelecionados);
		
		
		Set<Aluno> alunosDisponiveisSet = new HashSet<>();
		alunosDisponiveisSet.addAll(alunosDisponiveis);
		alunosDisponiveis.clear();
		alunosDisponiveis.addAll(alunosDisponiveisSet);
		
		alunos = new DualListModel<Aluno>(alunosDisponiveis, alunosSelecionados);
		
		totalAlunos = alunosSelecionados.size();
	}

	public void montarPickListAluno() {
		montarPickListAluno(turma.getSerie(), turma.getPeriodo());
	}

	private void montarPickListProfessor() {
		/** MONTANDO O PICKLIST */
		List<Professor> todosProfessores = professorService.findAll();
		List<Professor> professoresDisponiveis = new ArrayList<>();
		List<Professor> professoresSelecionados = getProfessoresSelecionados();

		professoresDisponiveis.addAll(todosProfessores);
		professoresDisponiveis.removeAll(professoresSelecionados);

		professores = new DualListModel<Professor>(professoresDisponiveis, professoresSelecionados);
	}

	public int getTotalAlunos(){
		return this.totalAlunos;
	}
	
	public void verificarTodosAlunosTemAvaliacao(Long idTurma){
		List<Aluno> alunosTurma = alunoService.findAlunoTurmaBytTurma(idTurma);
		if(alunosTurma != null && !alunosTurma.isEmpty()){
			List<Avaliacao> avaliacoesTurma = avaliacaoService.find(alunosTurma.get(0).getSerie(), null);
			for(Aluno aluno :alunosTurma){
				for(Avaliacao avaliacao :avaliacoesTurma){
					List<AlunoAvaliacao> alav = avaliacaoService.findAlunoAvaliacaoby(aluno.getId(), avaliacao.getId(), null, null, null); 
					if(alav== null || alav.isEmpty()){
						avaliacaoService.createAlunoAvaliacao(aluno,avaliacao);
					}
				}
			}	
		}
		
	}
	
	private List<Professor> getProfessoresSelecionados() {

		return turma.getId() != null ? professorService.findProfessorTurmaBytTurma(turma.getId())
				: new ArrayList<Professor>();
	}

	private List<Aluno> getAlunosSelecionados() {

		return turma.getId() != null ? alunoService.findAlunoTurmaBytTurma(turma.getId()) : new ArrayList<Aluno>();
	}
	
	public float getNota(Aluno aluno, DisciplinaEnum disciplina, BimestreEnum  bimestre){
		return alunoService.getNota(aluno.getId(), disciplina, bimestre, false);
	}

	public List<Turma> getTurmas() {
		if (getLoggedUser().getProfessor() != null) {
			return turmaService.findAll(getLoggedUser().getProfessor().getId());

		} else {
			return turmaService.findAll();
		}
	}

	public String salvar() {
		turma = turmaService.save(turma);
		saveProfessorTurma();
		saveAlunoTurma();
		verificarTodosAlunosTemAvaliacao(turma.getId());

		return "index";
	}

	public String editar(Long idTurma) {
		turma = turmaService.findById(idTurma);
		Util.addAtributoSessao("turma", turma);
		return "cadastrar";
	}
	
	public  StreamedContent imprimirBoletinsTurma(Long idTurma) throws IOException {
		turma = turmaService.findById(idTurma);
		Util.addAtributoSessao("turma", turma);
		
		String pasta = ""+System.currentTimeMillis();
		String caminhoFinalPasta = System.getProperty("java.io.tmpdir") + pasta; 
		//String caminhoFinalPasta = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "\\"+pasta; 
		CompactadorZip.createDir(caminhoFinalPasta);
		List<Aluno> alunos= alunoService.findAlunoTurmaBytTurma(idTurma);
		for(Aluno al :alunos){
			try {
				gerarBoletim(al,pasta);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		OfficeDOCUtil.unionDocs2(caminhoFinalPasta, turma.getNome()+".doc");
		
		String arquivoSaida = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "\\"+turma.getNome()+".zip";
		CompactadorZip.compactarParaZip(arquivoSaida, caminhoFinalPasta);
		
		InputStream stream =  new FileInputStream(arquivoSaida);
		return FileDownload.getContentDoc(stream, turma.getNome()+".zip");

	}
	
	public void gerarBoletim(Aluno aluno,String pasta) throws IOException {
		String nomeArquivo = "";
		if(aluno != null && aluno.getId() != null){
			

			nomeArquivo =  pasta+ System.getProperty("file.separator") + aluno.getNomeAluno() + "(" + aluno.getId() +")";
			ImpressoesUtils.imprimirInformacoesAlunoTemp(aluno, "mb1.docx", montarBoletim(aluno), nomeArquivo);
			nomeArquivo += ".doc";
		}else{
			nomeArquivo ="mb1.docx";
		}
		
	}
	
	private HashMap<String, String> montarBoletim(Aluno aluno) {
		Professor prof = alunoService.getProfessor(aluno.getId());
		HashMap<String, String> trocas = new HashMap<>();
		trocas.put("#nomeAluno", aluno.getNomeAluno());
		trocas.put("#nomeProfessor", prof.getNome());
		trocas.put("#turma", aluno.getSerie().getName());
		
		//FALTAS
		trocas.put("#f1", aluno.getFaltas1Bimestre() != null ?aluno.getFaltas1Bimestre().toString():"");
		trocas.put("#f2", aluno.getFaltas2Bimestre() != null ?aluno.getFaltas2Bimestre().toString():"");
		trocas.put("#f3", aluno.getFaltas3Bimestre() != null ?aluno.getFaltas3Bimestre().toString():"");
		trocas.put("#f4", aluno.getFaltas4Bimestre() != null ?aluno.getFaltas4Bimestre().toString():"");

		float notaPortuguesPrimeiroBimestre = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES,
				BimestreEnum.PRIMEIRO_BIMESTRE, false);
		float notaPortuguesSegundoBimestre = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES,
				BimestreEnum.SEGUNDO_BIMESTRE, false);
		float notaPortuguesTerceiroBimestre = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES,
				BimestreEnum.TERCEIRO_BIMESTRE, false);
		float notaPortuguesQuartoBimestre = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES,
				BimestreEnum.QUARTO_BIMESTRE, false);

		float notaPortuguesPrimeiroRec = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES,
				BimestreEnum.PRIMEIRO_BIMESTRE, true);
		float notaPortuguesSegundoRec = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES,
				BimestreEnum.SEGUNDO_BIMESTRE, true);
		float notaPortuguesTerceiroRec = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES,
				BimestreEnum.TERCEIRO_BIMESTRE, true);
		float notaPortuguesQuartoRec = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES,
				BimestreEnum.QUARTO_BIMESTRE, true);
		float notaPortuguesRecFinal = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES, true, true);

		// PORTUGUES
		trocas.put("#np1", mostraNotas(notaPortuguesPrimeiroBimestre));
		trocas.put("#np2", mostraNotas(notaPortuguesSegundoBimestre));
		trocas.put("#np3", mostraNotas(notaPortuguesTerceiroBimestre));
		trocas.put("#np4", mostraNotas(notaPortuguesQuartoBimestre));

		// rec
		trocas.put("#npr1", mostraNotas(notaPortuguesPrimeiroRec));
		trocas.put("#npr2", mostraNotas(notaPortuguesSegundoRec));
		trocas.put("#npr3", mostraNotas(notaPortuguesTerceiroRec));
		trocas.put("#npr4", mostraNotas(notaPortuguesQuartoRec));
		// mediaFinal
		trocas.put("#mp1", mostraNotas(maior(notaPortuguesPrimeiroBimestre, notaPortuguesPrimeiroRec)));
		trocas.put("#mp2", mostraNotas(maior(notaPortuguesSegundoRec, notaPortuguesSegundoBimestre)));
		trocas.put("#mp3", mostraNotas(maior(notaPortuguesTerceiroRec, notaPortuguesTerceiroBimestre)));
		trocas.put("#mp4", mostraNotas(maior(notaPortuguesQuartoRec, notaPortuguesQuartoBimestre)));
		// Final do ano
		trocas.put("#npF",
				mostraNotas(media(maior(notaPortuguesPrimeiroBimestre, notaPortuguesPrimeiroRec),
						maior(notaPortuguesSegundoRec, notaPortuguesSegundoBimestre),
						maior(notaPortuguesTerceiroRec, notaPortuguesTerceiroBimestre),
						maior(notaPortuguesQuartoRec, notaPortuguesQuartoBimestre))));
		trocas.put("#nprf", mostraNotas(notaPortuguesRecFinal));

		// Matematica
		float notaMTM1Bimestre = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA,
				BimestreEnum.PRIMEIRO_BIMESTRE, false);
		float notaMTM2Bimestre = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA,
				BimestreEnum.SEGUNDO_BIMESTRE, false);
		float notaMTM3Bimestre = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA,
				BimestreEnum.TERCEIRO_BIMESTRE, false);
		float notaMTM4Bimestre = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA,
				BimestreEnum.QUARTO_BIMESTRE, false);

		float notaMTM1Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA,
				BimestreEnum.PRIMEIRO_BIMESTRE, true);
		float notaMTM2Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA,
				BimestreEnum.SEGUNDO_BIMESTRE, true);
		float notaMTM3Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA,
				BimestreEnum.TERCEIRO_BIMESTRE, true);
		float notaMTM4Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA, BimestreEnum.QUARTO_BIMESTRE,
				true);
		float notaMtmRecFinal = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA, true, true);

		trocas.put("#nm1", mostraNotas(notaMTM1Bimestre));
		trocas.put("#nm2", mostraNotas(notaMTM2Bimestre));
		trocas.put("#nm3", mostraNotas(notaMTM3Bimestre));
		trocas.put("#nm4", mostraNotas(notaMTM4Bimestre));
		// rec
		trocas.put("#nmr1", mostraNotas(notaMTM1Rec));
		trocas.put("#nmr2", mostraNotas(notaMTM2Rec));
		trocas.put("#nmr3", mostraNotas(notaMTM3Rec));
		trocas.put("#nmr4", mostraNotas(notaMTM4Rec));
		// mediaFinal
		trocas.put("#mm1", mostraNotas(maior(notaMTM1Bimestre, notaMTM1Rec)));
		trocas.put("#mm2", mostraNotas(maior(notaMTM2Bimestre, notaMTM2Rec)));
		trocas.put("#mm3", mostraNotas(maior(notaMTM3Bimestre, notaMTM3Rec)));
		trocas.put("#mm4", mostraNotas(maior(notaMTM4Bimestre, notaMTM4Rec)));
		// Final do ano
		trocas.put("#nmF", mostraNotas(media(maior(notaMTM1Bimestre, notaMTM1Rec), maior(notaMTM2Bimestre, notaMTM2Rec),
				maior(notaMTM3Bimestre, notaMTM3Rec), maior(notaMTM4Bimestre, notaMTM4Rec))));
		trocas.put("#nmrf", mostraNotas(notaMtmRecFinal));

		// Ingles
		float nota1BimestreIngles = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES,
				BimestreEnum.PRIMEIRO_BIMESTRE, false);
		float nota2BimestreIngles = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES,
				BimestreEnum.SEGUNDO_BIMESTRE, false);
		float nota3BimestreIngles = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES,
				BimestreEnum.TERCEIRO_BIMESTRE, false);
		float nota4BimestreIngles = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES,
				BimestreEnum.QUARTO_BIMESTRE, false);

		float nota1RecIngles = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES,
				BimestreEnum.PRIMEIRO_BIMESTRE, true);
		float nota2RecIngles = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES, BimestreEnum.SEGUNDO_BIMESTRE,
				true);
		float nota3RecIngles = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES,
				BimestreEnum.TERCEIRO_BIMESTRE, true);
		float nota4RecIngles = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES, BimestreEnum.QUARTO_BIMESTRE,
				true);
		float notaRecFinalIngles = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES, true, true);

		trocas.put("#ni1", mostraNotas(nota1BimestreIngles));
		trocas.put("#ni2", mostraNotas(nota2BimestreIngles));
		trocas.put("#ni3", mostraNotas(nota3BimestreIngles));
		trocas.put("#ni4", mostraNotas(nota4BimestreIngles));
		// rec
		trocas.put("#nir1", mostraNotas(nota1RecIngles));
		trocas.put("#nir2", mostraNotas(nota2RecIngles));
		trocas.put("#nir3", mostraNotas(nota3RecIngles));
		trocas.put("#nir4", mostraNotas(nota4RecIngles));
		// mediaFinal
		trocas.put("#mi1", mostraNotas(maior(nota1RecIngles, nota1BimestreIngles)));
		trocas.put("#mi2", mostraNotas(maior(nota2RecIngles, nota2BimestreIngles)));
		trocas.put("#mi3", mostraNotas(maior(nota3RecIngles, nota3BimestreIngles)));
		trocas.put("#mi4", mostraNotas(maior(nota4RecIngles, nota4BimestreIngles)));
		// Final do ano
		trocas.put("#niF",
				mostraNotas(media(maior(nota1RecIngles, nota1BimestreIngles),
						maior(nota2RecIngles, nota2BimestreIngles), maior(nota3RecIngles, nota3BimestreIngles),
						maior(nota4RecIngles, nota4BimestreIngles))));
		trocas.put("#nirf", mostraNotas(notaRecFinalIngles));

		// EdFisica
		float nota1BimestreEdFisica = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA,
				BimestreEnum.PRIMEIRO_BIMESTRE, false);
		float nota2BimestreEdFisica = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA,
				BimestreEnum.SEGUNDO_BIMESTRE, false);
		float nota3BimestreEdFisica = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA,
				BimestreEnum.TERCEIRO_BIMESTRE, false);
		float nota4BimestreEdFisica = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA,
				BimestreEnum.QUARTO_BIMESTRE, false);

		float nota1RecEdFisica = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA,
				BimestreEnum.PRIMEIRO_BIMESTRE, true);
		float nota2RecEdFisica = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA,
				BimestreEnum.SEGUNDO_BIMESTRE, true);
		float nota3RecEdFisica = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA,
				BimestreEnum.TERCEIRO_BIMESTRE, true);
		float nota4RecEdFisica = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA,
				BimestreEnum.QUARTO_BIMESTRE, true);
		float notaRecFinalEdFisica = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA, true, true);

		trocas.put("#ne1", mostraNotas(nota1BimestreEdFisica));
		trocas.put("#ne2", mostraNotas(nota2BimestreEdFisica));
		trocas.put("#ne3", mostraNotas(nota3BimestreEdFisica));
		trocas.put("#ne4", mostraNotas(nota4BimestreEdFisica));
		// rec
		trocas.put("#ner1", mostraNotas(nota1RecEdFisica));
		trocas.put("#ner2", mostraNotas(nota2RecEdFisica));
		trocas.put("#ner3", mostraNotas(nota3RecEdFisica));
		trocas.put("#ner4", mostraNotas(nota4RecEdFisica));
		// mediaFinal
		trocas.put("#me1", mostraNotas(maior(nota1BimestreEdFisica, nota1RecEdFisica)));
		trocas.put("#me2", mostraNotas(maior(nota2BimestreEdFisica, nota2RecEdFisica)));
		trocas.put("#me3", mostraNotas(maior(nota3BimestreEdFisica, nota3RecEdFisica)));
		trocas.put("#me4", mostraNotas(maior(nota4BimestreEdFisica, nota4RecEdFisica)));
		// Final do ano
		trocas.put("#neF",
				mostraNotas(media(maior(nota1BimestreEdFisica, nota1RecEdFisica),
						maior(nota2BimestreEdFisica, nota2RecEdFisica), maior(nota3BimestreEdFisica, nota3RecEdFisica),
						maior(nota4BimestreEdFisica, nota4RecEdFisica))));
		trocas.put("#nerf", mostraNotas(notaRecFinalEdFisica));

		// Geofrafia
		float nota1BimestreGeografia = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA,
				BimestreEnum.PRIMEIRO_BIMESTRE, false);
		float nota2BimestreGeografia = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA,
				BimestreEnum.SEGUNDO_BIMESTRE, false);
		float nota3BimestreGeografia = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA,
				BimestreEnum.TERCEIRO_BIMESTRE, false);
		float nota4BimestreGeografia = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA,
				BimestreEnum.QUARTO_BIMESTRE, false);

		float nota1RecGeografia = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA,
				BimestreEnum.PRIMEIRO_BIMESTRE, true);
		float nota2RecGeografia = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA,
				BimestreEnum.SEGUNDO_BIMESTRE, true);
		float nota3RecGeografia = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA,
				BimestreEnum.TERCEIRO_BIMESTRE, true);
		float nota4RecGeografia = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA,
				BimestreEnum.QUARTO_BIMESTRE, true);
		float notaRecFinalGeografia = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA, true, true);

		trocas.put("#ng1", mostraNotas(nota1BimestreGeografia));
		trocas.put("#ng2", mostraNotas(nota2BimestreGeografia));
		trocas.put("#ng3", mostraNotas(nota3BimestreGeografia));
		trocas.put("#ng4", mostraNotas(nota4BimestreGeografia));
		// rec
		trocas.put("#ngr1", mostraNotas(nota1RecGeografia));
		trocas.put("#ngr2", mostraNotas(nota2RecGeografia));
		trocas.put("#ngr3", mostraNotas(nota3RecGeografia));
		trocas.put("#ngr4", mostraNotas(nota4RecGeografia));
		// mediaFinal
		trocas.put("#mg1", mostraNotas(maior(nota1RecGeografia, nota1BimestreGeografia)));
		trocas.put("#mg2", mostraNotas(maior(nota2RecGeografia, nota2BimestreGeografia)));
		trocas.put("#mg3", mostraNotas(maior(nota3RecGeografia, nota3BimestreGeografia)));
		trocas.put("#mg4", mostraNotas(maior(nota4RecGeografia, nota4BimestreGeografia)));
		// Final do ano
		trocas.put("#ngF",
				mostraNotas(media(maior(nota1RecGeografia, nota1BimestreGeografia),
						maior(nota2RecGeografia, nota2BimestreGeografia),
						maior(nota3RecGeografia, nota3BimestreGeografia),
						maior(nota4RecGeografia, nota4BimestreGeografia))));
		trocas.put("#ngrf", mostraNotas(notaRecFinalGeografia));

		// Historia
		float nota1BimestreHistoria = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA,
				BimestreEnum.PRIMEIRO_BIMESTRE, false);
		float nota2BimestreHistoria = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA,
				BimestreEnum.SEGUNDO_BIMESTRE, false);
		float nota3BimestreHistoria = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA,
				BimestreEnum.TERCEIRO_BIMESTRE, false);
		float nota4BimestreHistoria = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA,
				BimestreEnum.QUARTO_BIMESTRE, false);

		float nota1RecHistoria = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA,
				BimestreEnum.PRIMEIRO_BIMESTRE, true);
		float nota2RecHistoria = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA,
				BimestreEnum.SEGUNDO_BIMESTRE, true);
		float nota3RecHistoria = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA,
				BimestreEnum.TERCEIRO_BIMESTRE, true);
		float nota4RecHistoria = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA,
				BimestreEnum.QUARTO_BIMESTRE, true);
		float notaRecFinalHistoria = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA, true, true);

		trocas.put("#nh1", mostraNotas(nota1BimestreHistoria));
		trocas.put("#nh2", mostraNotas(nota2BimestreHistoria));
		trocas.put("#nh3", mostraNotas(nota3BimestreHistoria));
		trocas.put("#nh4", mostraNotas(nota4BimestreHistoria));
		// rec
		trocas.put("#nhr1", mostraNotas(nota1RecHistoria));
		trocas.put("#nhr2", mostraNotas(nota2RecHistoria));
		trocas.put("#nhr3", mostraNotas(nota3RecHistoria));
		trocas.put("#nhr4", mostraNotas(nota4RecHistoria));
		// mediaFinal
		trocas.put("#mh1", mostraNotas(maior(nota1RecHistoria, nota1BimestreHistoria)));
		trocas.put("#mh2", mostraNotas(maior(nota2RecHistoria, nota2BimestreHistoria)));
		trocas.put("#mh3", mostraNotas(maior(nota3RecHistoria, nota3BimestreHistoria)));
		trocas.put("#mh4", mostraNotas(maior(nota4RecHistoria, nota4BimestreHistoria)));
		// Final do ano
		trocas.put("#nhF",
				mostraNotas(media(maior(nota1RecHistoria, nota1BimestreHistoria),
						maior(nota2RecHistoria, nota2BimestreHistoria), maior(nota3RecHistoria, nota3BimestreHistoria),
						maior(nota4RecHistoria, nota4BimestreHistoria))));
		trocas.put("#nhrf", mostraNotas(notaRecFinalHistoria));

		// Ciencias
		float nota1BimestreCiencias = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS,
				BimestreEnum.PRIMEIRO_BIMESTRE, false);
		float nota2BimestreCiencias = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS,
				BimestreEnum.SEGUNDO_BIMESTRE, false);
		float nota3BimestreCiencias = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS,
				BimestreEnum.TERCEIRO_BIMESTRE, false);
		float nota4BimestreCiencias = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS,
				BimestreEnum.QUARTO_BIMESTRE, false);

		float nota1RecCiencias = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS,
				BimestreEnum.PRIMEIRO_BIMESTRE, true);
		float nota2RecCiencias = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS,
				BimestreEnum.SEGUNDO_BIMESTRE, true);
		float nota3RecCiencias = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS,
				BimestreEnum.TERCEIRO_BIMESTRE, true);
		float nota4RecCiencias = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS,
				BimestreEnum.QUARTO_BIMESTRE, true);
		float notaRecFinalCiencia = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS, true, true);

		trocas.put("#nc1", mostraNotas(nota1BimestreCiencias));
		trocas.put("#nc2", mostraNotas(nota2BimestreCiencias));
		trocas.put("#nc3", mostraNotas(nota3BimestreCiencias));
		trocas.put("#nc4", mostraNotas(nota4BimestreCiencias));
		// rec
		trocas.put("#ncr1", mostraNotas(nota1RecCiencias));
		trocas.put("#ncr2", mostraNotas(nota2RecCiencias));
		trocas.put("#ncr3", mostraNotas(nota3RecCiencias));
		trocas.put("#ncr4", mostraNotas(nota4RecCiencias));
		// mediaFinal
		trocas.put("#mc1", mostraNotas(maior(nota1RecCiencias, nota1BimestreCiencias)));
		trocas.put("#mc2", mostraNotas(maior(nota2RecCiencias, nota2BimestreCiencias)));
		trocas.put("#mc3", mostraNotas(maior(nota3RecCiencias, nota3BimestreCiencias)));
		trocas.put("#mc4", mostraNotas(maior(nota4RecCiencias, nota4BimestreCiencias)));
		// Final do ano
		trocas.put("#ncF",
				mostraNotas(media(maior(nota1RecCiencias, nota1BimestreCiencias),
						maior(nota2RecCiencias, nota2BimestreCiencias), maior(nota3RecCiencias, nota3BimestreCiencias),
						maior(nota4RecCiencias, nota4BimestreCiencias))));
		trocas.put("#ncrf", mostraNotas(notaRecFinalHistoria));

		// Formacao Crista
		float nota1BimestreFormaCrista = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA,
				BimestreEnum.PRIMEIRO_BIMESTRE, false);
		float nota2BimestreFormaCrista = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA,
				BimestreEnum.SEGUNDO_BIMESTRE, false);
		float nota3BimestreFormaCrista = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA,
				BimestreEnum.TERCEIRO_BIMESTRE, false);
		float nota4BimestreFormaCrista = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA,
				BimestreEnum.QUARTO_BIMESTRE, false);

		float nota1RecFormaCrista = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA,
				BimestreEnum.PRIMEIRO_BIMESTRE, true);
		float nota2RecFormaCrista = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA,
				BimestreEnum.SEGUNDO_BIMESTRE, true);
		float nota3RecFormaCrista = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA,
				BimestreEnum.TERCEIRO_BIMESTRE, true);
		float nota4RecFormaCrista = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA,
				BimestreEnum.QUARTO_BIMESTRE, true);
		float notaRecFinalFormaCrista = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA, true, true);

		trocas.put("#nf1", mostraNotas(nota1BimestreFormaCrista));
		trocas.put("#nf2", mostraNotas(nota2BimestreFormaCrista));
		trocas.put("#nf3", mostraNotas(nota3BimestreFormaCrista));
		trocas.put("#nf4", mostraNotas(nota4BimestreFormaCrista));
		// rec
		trocas.put("#nfr1", mostraNotas(nota1RecFormaCrista));
		trocas.put("#nfr2", mostraNotas(nota2RecFormaCrista));
		trocas.put("#nfr3", mostraNotas(nota3RecFormaCrista));
		trocas.put("#nfr4", mostraNotas(nota4RecFormaCrista));
		// mediaFinal
		trocas.put("#mf1", mostraNotas(maior(nota1RecFormaCrista, nota1BimestreFormaCrista)));
		trocas.put("#mf2", mostraNotas(maior(nota2RecFormaCrista, nota2BimestreFormaCrista)));
		trocas.put("#mf3", mostraNotas(maior(nota3RecFormaCrista, nota3BimestreFormaCrista)));
		trocas.put("#mf4", mostraNotas(maior(nota4RecFormaCrista, nota4BimestreFormaCrista)));
		// Final do ano
		trocas.put("#nfF",
				mostraNotas(media(maior(nota1RecFormaCrista, nota1BimestreFormaCrista),
						maior(nota2RecFormaCrista, nota2BimestreFormaCrista),
						maior(nota3RecFormaCrista, nota3BimestreFormaCrista),
						maior(nota4RecFormaCrista, nota4BimestreFormaCrista))));
		trocas.put("#nfrf", mostraNotas(notaRecFinalFormaCrista));

		// Artes
		float nota1BimestreArtes = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES,
				BimestreEnum.PRIMEIRO_BIMESTRE, false);
		float nota2BimestreArtes = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES,
				BimestreEnum.SEGUNDO_BIMESTRE, false);
		float nota3BimestreArtes = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES,
				BimestreEnum.TERCEIRO_BIMESTRE, false);
		float nota4BimestreArtes = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES,
				BimestreEnum.QUARTO_BIMESTRE, false);

		float nota1RecArtes = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, BimestreEnum.PRIMEIRO_BIMESTRE,
				true);
		float nota2RecArtes = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, BimestreEnum.SEGUNDO_BIMESTRE,
				true);
		float nota3RecArtes = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, BimestreEnum.TERCEIRO_BIMESTRE,
				true);
		float nota4RecArtes = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, BimestreEnum.QUARTO_BIMESTRE,
				true);
		float notaRecFinalArtes = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, true, true);

		trocas.put("#na1", mostraNotas(nota1BimestreArtes));
		trocas.put("#na2", mostraNotas(nota2BimestreArtes));
		trocas.put("#na3", mostraNotas(nota3BimestreArtes));
		trocas.put("#na4", mostraNotas(nota4BimestreArtes));
		// rec
		trocas.put("#nar1", mostraNotas(nota1RecArtes));
		trocas.put("#nar2", mostraNotas(nota2RecArtes));
		trocas.put("#nar3", mostraNotas(nota3RecArtes));
		trocas.put("#nar4", mostraNotas(nota4RecArtes));
		// mediaFinal
		trocas.put("#ma1", mostraNotas(maior(nota1RecArtes, nota1BimestreArtes)));
		trocas.put("#ma2", mostraNotas(maior(nota2RecArtes, nota2BimestreArtes)));
		trocas.put("#ma3", mostraNotas(maior(nota3RecArtes, nota3BimestreArtes)));
		trocas.put("#ma4", mostraNotas(maior(nota4RecArtes, nota4BimestreArtes)));
		// Final do ano
		trocas.put("#naF",
				mostraNotas(media(maior(nota1RecArtes, nota1BimestreArtes), maior(nota2RecArtes, nota2BimestreArtes),
						maior(nota3RecArtes, nota3BimestreArtes), maior(nota4RecArtes, nota4BimestreArtes))));
		trocas.put("#narf", mostraNotas(notaRecFinalArtes));

		return trocas;
	}

	private Float maior(Float float1, Float float2) {
		if(Float.isNaN(float1)){
			return float2;
		}
		if(Float.isNaN(float2)){
			return float1;
		}
		
		return float1 > float2 ? float1 : float2;
	}

	private String mostraNotas(Float nota) {
		if (nota == null || nota == 0 || Float.isNaN(nota)) {
			return "";
		} else {
			DecimalFormat df = new DecimalFormat("0.##");
			String dx = df.format(nota);
			
			dx = dx.replace(",", ".");
			
			return String.valueOf((Math.round(Float.parseFloat(dx) / 0.5) * 0.5)) ;
		}
	}

	private Float media(Float... notas) {
		int qtade = 0;
		float sum = 0;
		for (Float f : notas) {
			sum += f;
			qtade++;
		}
		return qtade > 0 ? sum / qtade : 0;
	}


	public String voltar() {
		return "index";
	}

	public String remover(Long idTurma) {
		turmaService.remove(idTurma);
		return "index";
	}

	private void saveProfessorTurma() {
		professorService.saveProfessorTurma(professores.getTarget(), turma);
	}

	private void saveAlunoTurma() {
		alunoService.saveAlunoTurma(alunos.getTarget(), turma);
	}

	public String adicionarNovo() {
		return "cadastrar";
	}

	public String cadastrarNovo() {
		Util.removeAtributoSessao("turma");
		return "exibir";
	}

	public DualListModel<Professor> getProfessores() {
		return professores;
	}

	public void setProfessores(DualListModel<Professor> professores) {
		this.professores = professores;
	}

	public DualListModel<Aluno> getAlunos() {
		return alunos;
	}

	public void setAlunos(DualListModel<Aluno> alunos) {
		this.alunos = alunos;
	}

	public DisciplinaEnum getDisciplinaSelecionada() {
		return disciplinaSelecionada;
	}

	public void setDisciplinaSelecionada(DisciplinaEnum disciplinaSelecionada) {
		this.disciplinaSelecionada = disciplinaSelecionada;
	}

	public BimestreEnum getBimestreSelecionado() {
		return bimestreSelecionado;
	}

	public void setBimestreSelecionado(BimestreEnum bimestreSelecionado) {
		this.bimestreSelecionado = bimestreSelecionado;
	}

	public float getValorSelecionado() {
		return valorSelecionado;
	}

	public void setValorSelecionado(float valorSelecionado) {
		this.valorSelecionado = valorSelecionado;
	}

	public void test(Long id, float nota) {
		System.out.println(id);
		System.out.println(nota);
	}

	public void saveAvaliacaoAluno(AlunoAvaliacao alav) {
		avaliacaoService.saveAlunoAvaliacao(alav);
	}
	
	public void saveAluno(Aluno aluno) {
		alunoService.save(aluno);
	}

	public void saveAvaliacaoAluno(Long idAluAv, Float nota) {
		avaliacaoService.saveAlunoAvaliacao(idAluAv, nota);
	}

	public List<AlunoAvaliacao> getAlunoAvaliacoes(Long idAluno, Long idAvaliacao, DisciplinaEnum disciplina,
			BimestreEnum bimestre, Serie serie) {

		setAlunosAvaliacao(avaliacaoService.findAlunoAvaliacaoby(idAluno, idAvaliacao, disciplina, bimestre, serie));
		return getAlunosAvaliacao();
	}

	public List<AlunoAvaliacao> getAlunosAvaliacao() {
		return alunosAvaliacao;
	}

	public void setAlunosAvaliacao(List<AlunoAvaliacao> alunosAvaliacao) {
		this.alunosAvaliacao = alunosAvaliacao;
	}

	public boolean renderDisciplina(int ordinal){
		if(getLoggedUser() != null && !getLoggedUser().getTipoMembro().equals(TipoMembro.PROFESSOR) ){
			return false;
		}
		
		if(disciplinaSelecionada == null){
			return false;
		}		
		return ordinal== disciplinaSelecionada.ordinal();
	}
	public void popularAlunoAvaliacao(){
			alunoAvaliacaoPortugues = avaliacaoService.findAlunoAvaliacaoMap(null, null, DisciplinaEnum.PORTUGUES, this.bimestreSelecionado,this.turma.getId());
			alunoAvaliacaoIngles = avaliacaoService.findAlunoAvaliacaoMap(null, null, DisciplinaEnum.INGLES, this.bimestreSelecionado,this.turma.getId());
			alunoAvaliacaoEDFisica = avaliacaoService.findAlunoAvaliacaoMap(null, null, DisciplinaEnum.EDUCACAO_FISICA, this.bimestreSelecionado,this.turma.getId());
			alunoAvaliacaoGeografia = avaliacaoService.findAlunoAvaliacaoMap(null, null, DisciplinaEnum.GEOGRAFIA, this.bimestreSelecionado,this.turma.getId());
			alunoAvaliacaoHistoria = avaliacaoService.findAlunoAvaliacaoMap(null, null, DisciplinaEnum.HISTORIA, this.bimestreSelecionado,this.turma.getId());
			alunoAvaliacaoMatematica = avaliacaoService.findAlunoAvaliacaoMap(null, null, DisciplinaEnum.MATEMATICA, this.bimestreSelecionado,this.turma.getId());
			
			alunoAvaliacaoCiencias = avaliacaoService.findAlunoAvaliacaoMap(null, null, DisciplinaEnum.CIENCIAS, this.bimestreSelecionado,this.turma.getId());
			alunoAvaliacaoFormacaoCrista = avaliacaoService.findAlunoAvaliacaoMap(null, null, DisciplinaEnum.FORMACAO_CRISTA, this.bimestreSelecionado,this.turma.getId());
			alunoAvaliacaoArtes = avaliacaoService.findAlunoAvaliacaoMap(null, null, DisciplinaEnum.ARTES, this.bimestreSelecionado,this.turma.getId());
	}
	
	public Map<Aluno,List<AlunoAvaliacao>> getAlunoAvaliacaoPortugues() {
		return alunoAvaliacaoPortugues;
	}
	
	public Map<Aluno,List<AlunoAvaliacao>> getAlunoAvaliacaoIngles() {
		return alunoAvaliacaoIngles;
	}
	
	public Map<Aluno,List<AlunoAvaliacao>> getAlunoAvaliacaoEdFisica() {
		return alunoAvaliacaoEDFisica;
	}
	public Map<Aluno,List<AlunoAvaliacao>> getAlunoAvaliacaoGeografia() {
		return alunoAvaliacaoGeografia;
	}
	public Map<Aluno,List<AlunoAvaliacao>> getAlunoAvaliacaoHistoria() {
		return alunoAvaliacaoHistoria;
	}
	
	public Map<Aluno,List<AlunoAvaliacao>> getAlunoAvaliacaoMatematica() {
		return alunoAvaliacaoMatematica;
	}
	
	public List<AlunoAvaliacao> getAlunosAvaliacao(Aluno al){
		return alunoAvaliacaoPortugues.get(al);
	}

	public Map<Aluno,List<AlunoAvaliacao>> getAlunoAvaliacaoCiencias() {
		return alunoAvaliacaoCiencias;
	}

	public void setAlunoAvaliacaoCiencias(Map<Aluno,List<AlunoAvaliacao>> alunoAvaliacaoCiencias) {
		this.alunoAvaliacaoCiencias = alunoAvaliacaoCiencias;
	}

	public Map<Aluno,List<AlunoAvaliacao>> getAlunoAvaliacaoFormacaoCrista() {
		return alunoAvaliacaoFormacaoCrista;
	}

	public void setAlunoAvaliacaoFormacaoCrista(Map<Aluno,List<AlunoAvaliacao>> alunoAvaliacaoFormacaoCrista) {
		this.alunoAvaliacaoFormacaoCrista = alunoAvaliacaoFormacaoCrista;
	}

	public Map<Aluno,List<AlunoAvaliacao>> getAlunoAvaliacaoArtes() {
		return alunoAvaliacaoArtes;
	}

	public void setAlunoAvaliacaoArtes(Map<Aluno,List<AlunoAvaliacao>> alunoAvaliacaoArtes) {
		this.alunoAvaliacaoArtes = alunoAvaliacaoArtes;
	}
	public BimestreEnum getPrimeiroBimestre(){
		return BimestreEnum.PRIMEIRO_BIMESTRE;
	}
	
	public BimestreEnum getSegundoBimestre(){
		return BimestreEnum.SEGUNDO_BIMESTRE;
	}
	
	public BimestreEnum getTerceiroBimestre(){
		return BimestreEnum.TERCEIRO_BIMESTRE;
	}
	
	public BimestreEnum getQuartoBimestre(){
		return BimestreEnum.QUARTO_BIMESTRE;
	}
	
	public DisciplinaEnum getPortugues(){
		return DisciplinaEnum.PORTUGUES;
	}
	
	public DisciplinaEnum getMatematica(){
		return DisciplinaEnum.MATEMATICA;
	}
	
	public DisciplinaEnum getHistoria(){
		return DisciplinaEnum.HISTORIA;
	}
	
	public DisciplinaEnum getIngles(){
		return DisciplinaEnum.INGLES;
	}
	
	public DisciplinaEnum getEDFisica(){
		return DisciplinaEnum.EDUCACAO_FISICA;
	}
	
	public DisciplinaEnum getGeografia(){
		return DisciplinaEnum.GEOGRAFIA;
	}
	
	public DisciplinaEnum getCiencias(){
		return DisciplinaEnum.CIENCIAS;
	}
	
	public DisciplinaEnum getFormacaoCrista(){
		return DisciplinaEnum.FORMACAO_CRISTA;
	}
	
	public DisciplinaEnum getArtes(){
		return DisciplinaEnum.ARTES;
	}

	public void setTotalAlunos(int totalAlunos) {
		this.totalAlunos = totalAlunos;
	}

}

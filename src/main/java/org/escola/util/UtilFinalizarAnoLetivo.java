package org.escola.util;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.escola.enums.BimestreEnum;
import org.escola.enums.DisciplinaEnum;
import org.escola.enums.Serie;
import org.escola.model.Aluno;
import org.escola.model.Configuracao;
import org.escola.model.ContratoAluno;
import org.escola.model.Evento;
import org.escola.model.HistoricoAluno;
import org.escola.service.AlunoService;
import org.escola.service.ConfiguracaoService;
import org.escola.service.EventoService;
import org.escola.service.HistoricoService;


@Stateless
public class UtilFinalizarAnoLetivo {

	
	@Inject
	private Logger log;

	@Inject
	private EventoService eventoService;

	@Inject
	private ConfiguracaoService configuracaoService;

	@Inject
	private AlunoService alunoService;

	@Inject
	private HistoricoService historicoService;
	
	@PersistenceContext(unitName = "EscolaDS")
	private EntityManager em;
	
	public void finalizar(int inicio,int quantidade, int anoLetivoAtual){
		editarrAlunos(inicio,quantidade,anoLetivoAtual);
		//alterarConfiguracao();
		//mudarDataDosEventos();
	}
	
	public void finalizarAnoLetivo(List<Aluno> alunos,int inicio,int quantidade,int anoLetivoAtual){
		editarrAlunos(alunos,inicio,quantidade,anoLetivoAtual);
		//alterarConfiguracao();
		//mudarDataDosEventos();
	}
	
	public void alterarConfiguracao() {
		Configuracao conf = configuracaoService.getConfiguracao();
		conf.setBimestre(BimestreEnum.PRIMEIRO_BIMESTRE);
		conf.setAnoLetivo(conf.getAnoLetivo()+1);
		configuracaoService.save(conf);
	}

	public void mudarDataDosEventos(){
		List<Evento> todosEventos = eventoService.findAll();
		for(Evento evento : todosEventos){
			evento.setDataFim(mudarAno(evento.getDataFim(), configuracaoService.getConfiguracao().getAnoLetivo()));
			evento.setDataInicio(mudarAno(evento.getDataInicio(), configuracaoService.getConfiguracao().getAnoLetivo()));
			em.persist(evento);
		}
	}

	public Date mudarAno(Date data, int ano){
		if(data != null){
			Calendar c = Calendar.getInstance();
			c.setTime(data);
			c.set(Calendar.YEAR, ano); 
			return c.getTime();	
		}
		return data;
	}

	private int faltas(Aluno aluno){
		int faltas = 0;
		if(aluno.getFaltas1Bimestre() != null){
			faltas+= aluno.getFaltas1Bimestre();
		}
		if(aluno.getFaltas2Bimestre()  != null){
			faltas+= aluno.getFaltas1Bimestre();;
		}
		if(aluno.getFaltas3Bimestre() != null){
			faltas+= aluno.getFaltas3Bimestre();;
		}
		if(aluno.getFaltas4Bimestre() != null){
			faltas+= aluno.getFaltas4Bimestre();
		}
		
		return faltas;
	}
	
	public void gerarHistorico(Aluno aluno, int ano){
		gerarHistorico(aluno, ano, true);
	}
	
	public void gerarHistorico(Aluno aluno, int ano, boolean mesmoano){
		if(aluno != null && aluno.getNomeAluno() != null){
			HistoricoAluno historico = new HistoricoAluno();
			historico.setNomeAluno(aluno.getNomeAluno());
			historico.setAluno(aluno);
			historico.setAno(ano);
			historico.setEscola("Centro Educacional ADONAI");
			historico.setEstado("Santa Catarina");
			
			double frequencia = ((200-faltas(aluno))/200)*100;
			if(frequencia == 0){
				frequencia = 100;
			}
			historico.setFrequencia(frequencia+"%");
			
			historico.setMunicipio("Palhoça");
			historico.setPeriodo(aluno.getPeriodo());
			if(mesmoano){
				historico.setSerie(aluno.getSerie());
			}else{
				if(aluno.getRematricular() != null && aluno.getRematricular()){
					historico.setSerie(Serie.values()[aluno.getSerie().ordinal()]);
				}else{
					historico.setSerie(Serie.values()[aluno.getSerie().ordinal()-1]);
				}
			}

			//Artes
			float notaArtes1 = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, BimestreEnum.PRIMEIRO_BIMESTRE, false, ano);
			float notaArtes1Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, BimestreEnum.PRIMEIRO_BIMESTRE, true,ano);
			float notaArtes2 = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, BimestreEnum.SEGUNDO_BIMESTRE, false,ano);
			float notaArtes2Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, BimestreEnum.SEGUNDO_BIMESTRE, true,ano);
			float notaArtes3 = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, BimestreEnum.TERCEIRO_BIMESTRE, false,ano);
			float notaArtes3Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, BimestreEnum.TERCEIRO_BIMESTRE, true,ano);
			float notaArtes4 = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, BimestreEnum.QUARTO_BIMESTRE, false,ano);
			float notaArtes4Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, BimestreEnum.QUARTO_BIMESTRE, true,ano);
			Float notaArtes = media(maior(notaArtes1, notaArtes1Rec),	maior(notaArtes2, notaArtes2Rec), maior(notaArtes3, notaArtes3Rec),	maior(notaArtes4, notaArtes4Rec));
			historico.setNotaArtes(mostraNotas(notaArtes));
			
			//Ciencias
			float notaCiencias1 = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS, BimestreEnum.PRIMEIRO_BIMESTRE, false,ano);
			float notaCiencias1Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS, BimestreEnum.PRIMEIRO_BIMESTRE, true,ano);
			float notaCiencias2 = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS, BimestreEnum.SEGUNDO_BIMESTRE, false,ano);
			float notaCiencias2Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS, BimestreEnum.SEGUNDO_BIMESTRE, true,ano);
			float notaCiencias3 = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS, BimestreEnum.TERCEIRO_BIMESTRE, false,ano);
			float notaCiencias3Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS, BimestreEnum.TERCEIRO_BIMESTRE, true,ano);
			float notaCiencias4 = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS, BimestreEnum.QUARTO_BIMESTRE, false,ano);
			float notaCiencias4Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS, BimestreEnum.QUARTO_BIMESTRE, true,ano);
			Float notaCiencias = media(maior(notaCiencias1, notaCiencias1Rec),	maior(notaCiencias2, notaCiencias2Rec), maior(notaCiencias3, notaCiencias3Rec),	maior(notaCiencias4, notaCiencias4Rec));
			historico.setNotaCiencias(notaCiencias);
			
			//EdFisica
			float notaEdFisica1 = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA, BimestreEnum.PRIMEIRO_BIMESTRE, false,ano);
			float notaEdFisica1Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA, BimestreEnum.PRIMEIRO_BIMESTRE, true,ano);
			float notaEdFisica2 = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA, BimestreEnum.SEGUNDO_BIMESTRE, false,ano);
			float notaEdFisica2Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA, BimestreEnum.SEGUNDO_BIMESTRE, true,ano);
			float notaEdFisica3 = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA, BimestreEnum.TERCEIRO_BIMESTRE, false,ano);
			float notaEdFisica3Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA, BimestreEnum.TERCEIRO_BIMESTRE, true,ano);
			float notaEdFisica4 = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA, BimestreEnum.QUARTO_BIMESTRE, false,ano);
			float notaEdFisica4Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA, BimestreEnum.QUARTO_BIMESTRE, true,ano);
			Float notaEdFisica = media(maior(notaEdFisica1, notaEdFisica1Rec),	maior(notaEdFisica2, notaEdFisica2Rec), maior(notaEdFisica3, notaEdFisica3Rec),	maior(notaEdFisica4, notaEdFisica4Rec));
			historico.setNotaEdFisica(notaEdFisica);
			
			//FORM CRISTA
			float notaFORMACAO_CRISTA1 = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA, BimestreEnum.PRIMEIRO_BIMESTRE, false,ano);
			float notaFORMACAO_CRISTA1Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA, BimestreEnum.PRIMEIRO_BIMESTRE, true,ano);
			float notaFORMACAO_CRISTA2 = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA, BimestreEnum.SEGUNDO_BIMESTRE, false,ano);
			float notaFORMACAO_CRISTA2Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA, BimestreEnum.SEGUNDO_BIMESTRE, true,ano);
			float notaFORMACAO_CRISTA3 = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA, BimestreEnum.TERCEIRO_BIMESTRE, false,ano);
			float notaFORMACAO_CRISTA3Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA, BimestreEnum.TERCEIRO_BIMESTRE, true,ano);
			float notaFORMACAO_CRISTA4 = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA, BimestreEnum.QUARTO_BIMESTRE, false,ano);
			float notaFORMACAO_CRISTA4Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA, BimestreEnum.QUARTO_BIMESTRE, true,ano);
			Float notaFORMACAO_CRISTA = media(maior(notaFORMACAO_CRISTA1, notaFORMACAO_CRISTA1Rec),	maior(notaFORMACAO_CRISTA2, notaFORMACAO_CRISTA2Rec), maior(notaFORMACAO_CRISTA3, notaFORMACAO_CRISTA3Rec),	maior(notaFORMACAO_CRISTA4, notaFORMACAO_CRISTA4Rec));
			historico.setNotaformacaoCrista(notaFORMACAO_CRISTA);
			
			//GEOGRAFIA
			float notaGEOGRAFIA1 = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA, BimestreEnum.PRIMEIRO_BIMESTRE, false,ano);
			float notaGEOGRAFIA1Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA, BimestreEnum.PRIMEIRO_BIMESTRE, true,ano);
			float notaGEOGRAFIA2 = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA, BimestreEnum.SEGUNDO_BIMESTRE, false,ano);
			float notaGEOGRAFIA2Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA, BimestreEnum.SEGUNDO_BIMESTRE, true,ano);
			float notaGEOGRAFIA3 = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA, BimestreEnum.TERCEIRO_BIMESTRE, false,ano);
			float notaGEOGRAFIA3Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA, BimestreEnum.TERCEIRO_BIMESTRE, true,ano);
			float notaGEOGRAFIA4 = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA, BimestreEnum.QUARTO_BIMESTRE, false,ano);
			float notaGEOGRAFIA4Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA, BimestreEnum.QUARTO_BIMESTRE, true,ano);
			Float notaGEOGRAFIA = media(maior(notaGEOGRAFIA1, notaGEOGRAFIA1Rec),	maior(notaGEOGRAFIA2, notaGEOGRAFIA2Rec), maior(notaGEOGRAFIA3, notaGEOGRAFIA3Rec),	maior(notaGEOGRAFIA4, notaGEOGRAFIA4Rec));
			historico.setNotaGeografia(notaGEOGRAFIA);
			
			//HITORIA
			float notaHISTORIA1 = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA, BimestreEnum.PRIMEIRO_BIMESTRE, false,ano);
			float notaHISTORIA1Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA, BimestreEnum.PRIMEIRO_BIMESTRE, true,ano);
			float notaHISTORIA2 = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA, BimestreEnum.SEGUNDO_BIMESTRE, false,ano);
			float notaHISTORIA2Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA, BimestreEnum.SEGUNDO_BIMESTRE, true,ano);
			float notaHISTORIA3 = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA, BimestreEnum.TERCEIRO_BIMESTRE, false,ano);
			float notaHISTORIA3Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA, BimestreEnum.TERCEIRO_BIMESTRE, true,ano);
			float notaHISTORIA4 = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA, BimestreEnum.QUARTO_BIMESTRE, false,ano);
			float notaHISTORIA4Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA, BimestreEnum.QUARTO_BIMESTRE, true,ano);
			Float notaHISTORIA = media(maior(notaHISTORIA1, notaHISTORIA1Rec),	maior(notaHISTORIA2, notaHISTORIA2Rec), maior(notaHISTORIA3, notaHISTORIA3Rec),	maior(notaHISTORIA4, notaHISTORIA4Rec));
			historico.setNotaHistoria(notaHISTORIA);
			
			//Ingles
			float notaINGLES1 = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES, BimestreEnum.PRIMEIRO_BIMESTRE, false,ano);
			float notaINGLES1Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES, BimestreEnum.PRIMEIRO_BIMESTRE, true,ano);
			float notaINGLES2 = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES, BimestreEnum.SEGUNDO_BIMESTRE, false,ano);
			float notaINGLES2Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES, BimestreEnum.SEGUNDO_BIMESTRE, true,ano);
			float notaINGLES3 = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES, BimestreEnum.TERCEIRO_BIMESTRE, false,ano);
			float notaINGLES3Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES, BimestreEnum.TERCEIRO_BIMESTRE, true,ano);
			float notaINGLES4 = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES, BimestreEnum.QUARTO_BIMESTRE, false,ano);
			float notaINGLES4Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES, BimestreEnum.QUARTO_BIMESTRE, true,ano);
			Float notaINGLES = media(maior(notaINGLES1, notaINGLES1Rec),	maior(notaINGLES2, notaINGLES2Rec), maior(notaINGLES3, notaINGLES3Rec),	maior(notaINGLES4, notaINGLES4Rec));
			historico.setNotaIngles(notaINGLES);
			
			//Espanhol
			float notaEspanhols1 = alunoService.getNota(aluno.getId(), DisciplinaEnum.ESPANHOL, BimestreEnum.PRIMEIRO_BIMESTRE, false,ano);
			float notaEspanhols1Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.ESPANHOL, BimestreEnum.PRIMEIRO_BIMESTRE, true,ano);
			float notaEspanhols2 = alunoService.getNota(aluno.getId(), DisciplinaEnum.ESPANHOL, BimestreEnum.SEGUNDO_BIMESTRE, false,ano);
			float notaEspanhols2Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.ESPANHOL, BimestreEnum.SEGUNDO_BIMESTRE, true,ano);
			float notaEspanhols3 = alunoService.getNota(aluno.getId(), DisciplinaEnum.ESPANHOL, BimestreEnum.TERCEIRO_BIMESTRE, false,ano);
			float notaEspanhols3Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.ESPANHOL, BimestreEnum.TERCEIRO_BIMESTRE, true,ano);
			float notaEspanhols4 = alunoService.getNota(aluno.getId(), DisciplinaEnum.ESPANHOL, BimestreEnum.QUARTO_BIMESTRE, false,ano);
			float notaEspanhols4Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.ESPANHOL, BimestreEnum.QUARTO_BIMESTRE, true,ano);
			Float notaEspanhols = media(maior(notaEspanhols1, notaEspanhols1Rec),	maior(notaEspanhols2, notaEspanhols2Rec), maior(notaEspanhols3, notaEspanhols3Rec),	maior(notaEspanhols4, notaEspanhols4Rec));
			historico.setNotaEspanhol(notaEspanhols);
			
			//HITORIA
			float notaMATEMATICA1 = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA, BimestreEnum.PRIMEIRO_BIMESTRE, false,ano);
			float notaMATEMATICA1Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA, BimestreEnum.PRIMEIRO_BIMESTRE, true,ano);
			float notaMATEMATICA2 = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA, BimestreEnum.SEGUNDO_BIMESTRE, false,ano);
			float notaMATEMATICA2Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA, BimestreEnum.SEGUNDO_BIMESTRE, true,ano);
			float notaMATEMATICA3 = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA, BimestreEnum.TERCEIRO_BIMESTRE, false,ano);
			float notaMATEMATICA3Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA, BimestreEnum.TERCEIRO_BIMESTRE, true,ano);
			float notaMATEMATICA4 = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA, BimestreEnum.QUARTO_BIMESTRE, false,ano);
			float notaMATEMATICA4Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA, BimestreEnum.QUARTO_BIMESTRE, true,ano);
			Float notaMATEMATICA = media(maior(notaMATEMATICA1, notaMATEMATICA1Rec),	maior(notaMATEMATICA2, notaMATEMATICA2Rec), maior(notaMATEMATICA3, notaMATEMATICA3Rec),	maior(notaMATEMATICA4, notaMATEMATICA4Rec));
			historico.setNotaMatematica(notaMATEMATICA);
				
			//HITORIA
			float notaPORTUGUES1 = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES, BimestreEnum.PRIMEIRO_BIMESTRE, false,ano);
			float notaPORTUGUES1Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES, BimestreEnum.PRIMEIRO_BIMESTRE, true,ano);
			float notaPORTUGUES2 = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES, BimestreEnum.SEGUNDO_BIMESTRE, false,ano);
			float notaPORTUGUES2Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES, BimestreEnum.SEGUNDO_BIMESTRE, true,ano);
			float notaPORTUGUES3 = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES, BimestreEnum.TERCEIRO_BIMESTRE, false,ano);
			float notaPORTUGUES3Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES, BimestreEnum.TERCEIRO_BIMESTRE, true,ano);
			float notaPORTUGUES4 = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES, BimestreEnum.QUARTO_BIMESTRE, false,ano);
			float notaPORTUGUES4Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES, BimestreEnum.QUARTO_BIMESTRE, true,ano);
			Float notaPORTUGUES = media(maior(notaPORTUGUES1, notaPORTUGUES1Rec),	maior(notaPORTUGUES2, notaPORTUGUES2Rec), maior(notaPORTUGUES3, notaPORTUGUES3Rec),	maior(notaPORTUGUES4, notaPORTUGUES4Rec));
			historico.setNotaPortugues(notaPORTUGUES);
			
			historicoService.save(historico);	
	
		}
	}
	
	public void gerarHistorico(Aluno aluno){
		gerarHistorico(aluno, configuracaoService.getConfiguracao().getAnoLetivo() -1);
	}
	
	public void mudarAnoLetivoAluno(Aluno aluno,int anoRematricula){
	
		if(temContratoProximoAno(aluno,anoRematricula)){
			aluno.setAnoLetivo(anoRematricula);
		}
		
		aluno.setRematricular(false);
	}
	
	private boolean temContratoProximoAno(Aluno aluno,int anoRematricula) {
		return configuracaoService.temContratoNoAno(aluno,anoRematricula);
	}

	public void editarrAlunos(int inicio,int quantidade,int anoLetivoAtual){
		List<Aluno> alunos = getAlunosAluno(anoLetivoAtual);
		
		for(int i=inicio;i<inicio+quantidade;i++){
			try{
				System.out.println("id : " + alunos.get(i).getId());
				System.out.println("Nome : " + alunos.get(i).getNomeAluno());
				mudarAnoLetivoAluno(alunos.get(i),anoLetivoAtual);
				gerarHistorico(alunos.get(i));
				alunos.get(i).setSerie(Serie.values()[alunos.get(i).getSerie().ordinal()+1]);
								
			}catch(Exception e){
				
			}
		}
	}
	
	public void editarrAlunos(List<Aluno> alunos ,int inicio,int quantidade,int anoLetivoAtual){
		Configuracao conf = configuracaoService.getConfiguracao2();
		for(int i=inicio;i<inicio+quantidade;i++){
			try{
				System.out.println("id : " + alunos.get(i).getId());
				System.out.println("Nome : " + alunos.get(i).getNomeAluno());
				//mudarAnoLetivoAluno(alunos.get(i),conf.getAnoRematricula());
				gerarHistorico(alunos.get(i));
				alunos.get(i).setSerie(Serie.values()[alunos.get(i).getSerie().ordinal()+1]);
								
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void gerarHistorico(int inicio,int quantidade,int ano){
		List<Aluno> alunos = alunoService.findAll();
		
		for(int i=inicio;i<inicio+quantidade;i++){
			try{
				System.out.println("id : " + alunos.get(i).getId());
				System.out.println("Nome : " + alunos.get(i).getNomeAluno());
				gerarHistorico(alunos.get(i),ano,false);
			}catch(Exception e){
				
			}
		}
	}
	
	public int getAnoLetivoAtual(){
		return configuracaoService.getConfiguracao2().getAnoLetivo();
	}
	
	public List<Aluno> getAlunosAluno(int ano){
		
		return getAlunosAlunoLetivo2(ano);
	}
	
	public List<Aluno> getAlunosAlunoLetivo(int ano){

		Map<String, Object> filtros = new HashMap<String, Object>();
		
		filtros.put("anoLetivo" ,ano);
		filtros.put("removido" , false);
		return  alunoService.find(1, 30000, "nomeAluno", "asc", filtros);
	}
	
	public List<Aluno> getAlunosAlunoLetivo2(int ano){
		
		return  configuracaoService.findAlunosComContratoEm(ano);
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
	
	private Float maior(Float float1, Float float2) {
		if(Float.isNaN(float1)){
			return float2;
		}
		if(Float.isNaN(float2)){
			return float1;
		}
		
		return float1 > float2 ? float1 : float2;
	}
	
	private Float mostraNotas(Float nota) {
		if (nota == null || nota == 0 || Float.isNaN(nota)) {
			return 0F;
		} else {
			DecimalFormat df = new DecimalFormat("0.##");
			String dx = df.format(nota);
			
			dx = dx.replace(",", ".");
			
			return (float) (Math.round(Float.parseFloat(dx) / 0.5) * 0.5D) ;
		}
	}
}

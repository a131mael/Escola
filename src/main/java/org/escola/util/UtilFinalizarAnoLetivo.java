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
	
	public void finalizar(int inicio,int quantidade){
		editarrAlunos(inicio,quantidade);
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
	public void gerarHistorico(Aluno aluno){
		if(aluno != null && aluno.getNomeAluno() != null){
			HistoricoAluno historico = new HistoricoAluno();
			historico.setNomeAluno(aluno.getNomeAluno());
			historico.setAluno(aluno);
			historico.setAno(configuracaoService.getConfiguracao().getAnoLetivo());
			historico.setEscola("Centro Educacional ADONAI");
			historico.setEstado("Santa Catarina");
			
			double frequencia = ((200-faltas(aluno))/200)*100;
			if(frequencia == 0){
				frequencia = 100;
			}
			historico.setFrequencia(frequencia+"%");
			
			historico.setMunicipio("Palhoça");
			historico.setPeriodo(aluno.getPeriodo());
			historico.setSerie(aluno.getSerie());

			//Artes
			float notaArtes1 = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, BimestreEnum.PRIMEIRO_BIMESTRE, false);
			float notaArtes1Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, BimestreEnum.PRIMEIRO_BIMESTRE, true);
			float notaArtes2 = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, BimestreEnum.SEGUNDO_BIMESTRE, false);
			float notaArtes2Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, BimestreEnum.SEGUNDO_BIMESTRE, true);
			float notaArtes3 = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, BimestreEnum.TERCEIRO_BIMESTRE, false);
			float notaArtes3Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, BimestreEnum.TERCEIRO_BIMESTRE, true);
			float notaArtes4 = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, BimestreEnum.QUARTO_BIMESTRE, false);
			float notaArtes4Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.ARTES, BimestreEnum.QUARTO_BIMESTRE, true);
			Float notaArtes = media(maior(notaArtes1, notaArtes1Rec),	maior(notaArtes2, notaArtes2Rec), maior(notaArtes3, notaArtes3Rec),	maior(notaArtes4, notaArtes4Rec));
			historico.setNotaArtes(mostraNotas(notaArtes));
			
			//Ciencias
			float notaCiencias1 = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS, BimestreEnum.PRIMEIRO_BIMESTRE, false);
			float notaCiencias1Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS, BimestreEnum.PRIMEIRO_BIMESTRE, true);
			float notaCiencias2 = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS, BimestreEnum.SEGUNDO_BIMESTRE, false);
			float notaCiencias2Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS, BimestreEnum.SEGUNDO_BIMESTRE, true);
			float notaCiencias3 = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS, BimestreEnum.TERCEIRO_BIMESTRE, false);
			float notaCiencias3Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS, BimestreEnum.TERCEIRO_BIMESTRE, true);
			float notaCiencias4 = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS, BimestreEnum.QUARTO_BIMESTRE, false);
			float notaCiencias4Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.CIENCIAS, BimestreEnum.QUARTO_BIMESTRE, true);
			Float notaCiencias = media(maior(notaCiencias1, notaCiencias1Rec),	maior(notaCiencias2, notaCiencias2Rec), maior(notaCiencias3, notaCiencias3Rec),	maior(notaCiencias4, notaCiencias4Rec));
			historico.setNotaCiencias(notaCiencias);
			
			//EdFisica
			float notaEdFisica1 = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA, BimestreEnum.PRIMEIRO_BIMESTRE, false);
			float notaEdFisica1Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA, BimestreEnum.PRIMEIRO_BIMESTRE, true);
			float notaEdFisica2 = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA, BimestreEnum.SEGUNDO_BIMESTRE, false);
			float notaEdFisica2Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA, BimestreEnum.SEGUNDO_BIMESTRE, true);
			float notaEdFisica3 = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA, BimestreEnum.TERCEIRO_BIMESTRE, false);
			float notaEdFisica3Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA, BimestreEnum.TERCEIRO_BIMESTRE, true);
			float notaEdFisica4 = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA, BimestreEnum.QUARTO_BIMESTRE, false);
			float notaEdFisica4Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.EDUCACAO_FISICA, BimestreEnum.QUARTO_BIMESTRE, true);
			Float notaEdFisica = media(maior(notaEdFisica1, notaEdFisica1Rec),	maior(notaEdFisica2, notaEdFisica2Rec), maior(notaEdFisica3, notaEdFisica3Rec),	maior(notaEdFisica4, notaEdFisica4Rec));
			historico.setNotaEdFisica(notaEdFisica);
			
			//FORM CRISTA
			float notaFORMACAO_CRISTA1 = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA, BimestreEnum.PRIMEIRO_BIMESTRE, false);
			float notaFORMACAO_CRISTA1Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA, BimestreEnum.PRIMEIRO_BIMESTRE, true);
			float notaFORMACAO_CRISTA2 = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA, BimestreEnum.SEGUNDO_BIMESTRE, false);
			float notaFORMACAO_CRISTA2Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA, BimestreEnum.SEGUNDO_BIMESTRE, true);
			float notaFORMACAO_CRISTA3 = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA, BimestreEnum.TERCEIRO_BIMESTRE, false);
			float notaFORMACAO_CRISTA3Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA, BimestreEnum.TERCEIRO_BIMESTRE, true);
			float notaFORMACAO_CRISTA4 = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA, BimestreEnum.QUARTO_BIMESTRE, false);
			float notaFORMACAO_CRISTA4Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.FORMACAO_CRISTA, BimestreEnum.QUARTO_BIMESTRE, true);
			Float notaFORMACAO_CRISTA = media(maior(notaFORMACAO_CRISTA1, notaFORMACAO_CRISTA1Rec),	maior(notaFORMACAO_CRISTA2, notaFORMACAO_CRISTA2Rec), maior(notaFORMACAO_CRISTA3, notaFORMACAO_CRISTA3Rec),	maior(notaFORMACAO_CRISTA4, notaFORMACAO_CRISTA4Rec));
			historico.setNotaformacaoCrista(notaFORMACAO_CRISTA);
			
			//GEOGRAFIA
			float notaGEOGRAFIA1 = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA, BimestreEnum.PRIMEIRO_BIMESTRE, false);
			float notaGEOGRAFIA1Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA, BimestreEnum.PRIMEIRO_BIMESTRE, true);
			float notaGEOGRAFIA2 = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA, BimestreEnum.SEGUNDO_BIMESTRE, false);
			float notaGEOGRAFIA2Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA, BimestreEnum.SEGUNDO_BIMESTRE, true);
			float notaGEOGRAFIA3 = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA, BimestreEnum.TERCEIRO_BIMESTRE, false);
			float notaGEOGRAFIA3Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA, BimestreEnum.TERCEIRO_BIMESTRE, true);
			float notaGEOGRAFIA4 = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA, BimestreEnum.QUARTO_BIMESTRE, false);
			float notaGEOGRAFIA4Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.GEOGRAFIA, BimestreEnum.QUARTO_BIMESTRE, true);
			Float notaGEOGRAFIA = media(maior(notaGEOGRAFIA1, notaGEOGRAFIA1Rec),	maior(notaGEOGRAFIA2, notaGEOGRAFIA2Rec), maior(notaGEOGRAFIA3, notaGEOGRAFIA3Rec),	maior(notaGEOGRAFIA4, notaGEOGRAFIA4Rec));
			historico.setNotaGeografia(notaGEOGRAFIA);
			
			//HITORIA
			float notaHISTORIA1 = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA, BimestreEnum.PRIMEIRO_BIMESTRE, false);
			float notaHISTORIA1Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA, BimestreEnum.PRIMEIRO_BIMESTRE, true);
			float notaHISTORIA2 = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA, BimestreEnum.SEGUNDO_BIMESTRE, false);
			float notaHISTORIA2Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA, BimestreEnum.SEGUNDO_BIMESTRE, true);
			float notaHISTORIA3 = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA, BimestreEnum.TERCEIRO_BIMESTRE, false);
			float notaHISTORIA3Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA, BimestreEnum.TERCEIRO_BIMESTRE, true);
			float notaHISTORIA4 = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA, BimestreEnum.QUARTO_BIMESTRE, false);
			float notaHISTORIA4Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.HISTORIA, BimestreEnum.QUARTO_BIMESTRE, true);
			Float notaHISTORIA = media(maior(notaHISTORIA1, notaHISTORIA1Rec),	maior(notaHISTORIA2, notaHISTORIA2Rec), maior(notaHISTORIA3, notaHISTORIA3Rec),	maior(notaHISTORIA4, notaHISTORIA4Rec));
			historico.setNotaHistoria(notaHISTORIA);
				
			
			//HITORIA
			float notaINGLES1 = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES, BimestreEnum.PRIMEIRO_BIMESTRE, false);
			float notaINGLES1Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES, BimestreEnum.PRIMEIRO_BIMESTRE, true);
			float notaINGLES2 = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES, BimestreEnum.SEGUNDO_BIMESTRE, false);
			float notaINGLES2Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES, BimestreEnum.SEGUNDO_BIMESTRE, true);
			float notaINGLES3 = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES, BimestreEnum.TERCEIRO_BIMESTRE, false);
			float notaINGLES3Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES, BimestreEnum.TERCEIRO_BIMESTRE, true);
			float notaINGLES4 = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES, BimestreEnum.QUARTO_BIMESTRE, false);
			float notaINGLES4Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.INGLES, BimestreEnum.QUARTO_BIMESTRE, true);
			Float notaINGLES = media(maior(notaINGLES1, notaINGLES1Rec),	maior(notaINGLES2, notaINGLES2Rec), maior(notaINGLES3, notaINGLES3Rec),	maior(notaINGLES4, notaINGLES4Rec));
			historico.setNotaIngles(notaINGLES);
				
			
			//HITORIA
			float notaMATEMATICA1 = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA, BimestreEnum.PRIMEIRO_BIMESTRE, false);
			float notaMATEMATICA1Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA, BimestreEnum.PRIMEIRO_BIMESTRE, true);
			float notaMATEMATICA2 = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA, BimestreEnum.SEGUNDO_BIMESTRE, false);
			float notaMATEMATICA2Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA, BimestreEnum.SEGUNDO_BIMESTRE, true);
			float notaMATEMATICA3 = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA, BimestreEnum.TERCEIRO_BIMESTRE, false);
			float notaMATEMATICA3Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA, BimestreEnum.TERCEIRO_BIMESTRE, true);
			float notaMATEMATICA4 = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA, BimestreEnum.QUARTO_BIMESTRE, false);
			float notaMATEMATICA4Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.MATEMATICA, BimestreEnum.QUARTO_BIMESTRE, true);
			Float notaMATEMATICA = media(maior(notaMATEMATICA1, notaMATEMATICA1Rec),	maior(notaMATEMATICA2, notaMATEMATICA2Rec), maior(notaMATEMATICA3, notaMATEMATICA3Rec),	maior(notaMATEMATICA4, notaMATEMATICA4Rec));
			historico.setNotaMatematica(notaMATEMATICA);
				
			//HITORIA
			float notaPORTUGUES1 = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES, BimestreEnum.PRIMEIRO_BIMESTRE, false);
			float notaPORTUGUES1Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES, BimestreEnum.PRIMEIRO_BIMESTRE, true);
			float notaPORTUGUES2 = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES, BimestreEnum.SEGUNDO_BIMESTRE, false);
			float notaPORTUGUES2Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES, BimestreEnum.SEGUNDO_BIMESTRE, true);
			float notaPORTUGUES3 = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES, BimestreEnum.TERCEIRO_BIMESTRE, false);
			float notaPORTUGUES3Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES, BimestreEnum.TERCEIRO_BIMESTRE, true);
			float notaPORTUGUES4 = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES, BimestreEnum.QUARTO_BIMESTRE, false);
			float notaPORTUGUES4Rec = alunoService.getNota(aluno.getId(), DisciplinaEnum.PORTUGUES, BimestreEnum.QUARTO_BIMESTRE, true);
			Float notaPORTUGUES = media(maior(notaPORTUGUES1, notaPORTUGUES1Rec),	maior(notaPORTUGUES2, notaPORTUGUES2Rec), maior(notaPORTUGUES3, notaPORTUGUES3Rec),	maior(notaPORTUGUES4, notaPORTUGUES4Rec));
			historico.setNotaPortugues(notaPORTUGUES);
			
			historicoService.save(historico);	
	
		}
						
	}
	
	public void mudarAnoLetivoAluno(Aluno aluno){
	
		if(aluno.getRematricular() != null && aluno.getRematricular()){
			aluno.setAnoLetivo(configuracaoService.getConfiguracao().getAnoLetivo()+1);
		}else if(aluno.getAnoLetivo()< configuracaoService.getConfiguracao().getAnoLetivo()){
			aluno.setRemovido(true);
		}
		aluno.setRematricular(false);
	}
	
	public void editarrAlunos(int inicio,int quantidade){
		List<Aluno> alunos = getAlunosAlunoLetivoAtual();
		
		for(int i=inicio;i<inicio+quantidade;i++){
			try{
				System.out.println("id : " + alunos.get(i).getId());
				System.out.println("Nome : " + alunos.get(i).getNomeAluno());
				mudarAnoLetivoAluno(alunos.get(i));
				gerarHistorico(alunos.get(i));
				alunos.get(i).setSerie(Serie.values()[alunos.get(i).getSerie().ordinal()+1]);
								
			}catch(Exception e){
				
			}
		}
	}
	
	public List<Aluno> getAlunosAlunoLetivoAtual(){
		Map<String, Object> filtros = new HashMap<String, Object>();
		
		filtros.put("anoLetivo" , Constant.anoLetivoAtual -1);
		filtros.put("removido" , false);
		return  alunoService.find(1, 30000, "nomeAluno", "asc", filtros);
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

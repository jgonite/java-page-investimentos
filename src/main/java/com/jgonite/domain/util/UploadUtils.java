package com.jgonite.domain.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jgonite.adapter.postgres.model.DividendoAcoesModel;
import com.jgonite.adapter.postgres.model.InplitSplitModel;
import com.jgonite.adapter.postgres.model.MovimentoAcoesModel;
import com.jgonite.adapter.postgres.model.PrecoAcaoModel;
import com.jgonite.adapter.postgres.model.compositekey.PrecoAcaoPrimaryKey;
import com.jgonite.domain.exception.PlanilhaException;

public class UploadUtils {
	
	private static final int INPLITSPLIT_COLUNA_ODATE = 0;
	private static final int INPLITSPLIT_COLUNA_NOME_ACAO = 1;
	private static final int INPLITSPLIT_COLUNA_TIPO = 2;
	private static final int INPLITSPLIT_COLUNA_FATOR = 3;
	
	public static InplitSplitModel lerLinhaInplitSplit(int linha, String[] conteudoLinha) {
		InplitSplitModel model = new InplitSplitModel();
		int coluna = 0;
		try {
			coluna = INPLITSPLIT_COLUNA_ODATE;
			model.setOdate(conteudoLinha[coluna]);
			coluna = INPLITSPLIT_COLUNA_NOME_ACAO;
			model.setNomeAcao(conteudoLinha[coluna]);
			coluna = INPLITSPLIT_COLUNA_TIPO;
			model.setTipoInplitSplit(conteudoLinha[coluna]); 
			coluna = INPLITSPLIT_COLUNA_FATOR;
			model.setFator(new BigDecimal(conteudoLinha[coluna])); 
		} catch (Exception e) {
			throw new PlanilhaException( e.getMessage(), linha, coluna+1);
		}
		return model;
	}
	
	private static final int PRECOACAO_COLUNA_NOME_ACAO = 0;
	private static final int PRECOACAO_COLUNA_DATA_PRECO = 1;
	private static final int PRECOACAO_COLUNA_PRECO_FECH = 2;
	
	public static PrecoAcaoModel lerLinhasPrecoAcao(int linha, String[] conteudoLinha) {
		PrecoAcaoModel model = new PrecoAcaoModel();
		int coluna = 0;
		try {
			PrecoAcaoPrimaryKey pk = new PrecoAcaoPrimaryKey();
			coluna = PRECOACAO_COLUNA_NOME_ACAO;
			pk.setNmAcao(conteudoLinha[coluna]);
			coluna = PRECOACAO_COLUNA_DATA_PRECO;
			pk.setOdate(StringUtils.DDdotMMdotYYYYParaOdate(conteudoLinha[coluna]));
			model.setChavePrimaria(pk);
			coluna = PRECOACAO_COLUNA_PRECO_FECH;
			model.setValorAcao(new BigDecimal(conteudoLinha[coluna])); 
		} catch (Exception e) {
			throw new PlanilhaException( e.getMessage(), linha, coluna+1);
		}
		return model;
	}
	
	private static final int MOVIMENTO_COLUNA_DATA_MOVIMENTO = 0;
	private static final int MOVIMENTO_COLUNA_ACAO = 1;
	private static final int MOVIMENTO_COLUNA_NR_ACOE= 2;
	private static final int MOVIMENTO_COLUNA_VL_BRUT = 3;
	
	public static MovimentoAcoesModel lerLinhaMovimento(int linha, String[] conteudoLinha) {
		MovimentoAcoesModel movimentoModel = new MovimentoAcoesModel();
		int coluna = 0;
		try {
			coluna = MOVIMENTO_COLUNA_DATA_MOVIMENTO;
			movimentoModel.setDataMovimento( StringUtils.YYYYMMDDparaLocalDateTime(conteudoLinha[coluna]));
			coluna = MOVIMENTO_COLUNA_ACAO;
			movimentoModel.setNomeAcao(conteudoLinha[coluna]);
			coluna = MOVIMENTO_COLUNA_NR_ACOE;
			movimentoModel.setNumeroAcoes(Integer.parseInt(conteudoLinha[coluna])); 
			coluna = MOVIMENTO_COLUNA_VL_BRUT;
			movimentoModel.setValorBruto( StringUtils.stringNumericaParaNumero(conteudoLinha[coluna]));
			movimentoModel.setDataDeCarga(LocalDateTime.now());
		} catch (Exception e) {
			throw new PlanilhaException( e.getMessage(), linha, coluna+1);
		}
		return movimentoModel;
	}
	
	private static final int DIVIDENDO_COLUNA_DATA_MOVIMENTO = 0;
	private static final int DIVIDENDO_COLUNA_ACAO = 1;
	private static final int DIVIDENDO_COLUNA_VL_RECE= 2;
	private static final int DIVIDENDO_COLUNA_VL_ACOE_RECE = 3;
	private static final int DIVIDENDO_COLUNA_VL_ACAO_IR = 4;
	
	public static DividendoAcoesModel lerLinhaDividendoBonificacao(int linha, String[] conteudoLinha) {
		DividendoAcoesModel dividendoModel = new DividendoAcoesModel();
		int coluna = 0;
		try {
			coluna = DIVIDENDO_COLUNA_DATA_MOVIMENTO;
			dividendoModel.setDataDividendo( StringUtils.YYYYMMDDparaLocalDateTime(conteudoLinha[coluna]));
			coluna = DIVIDENDO_COLUNA_ACAO;
			dividendoModel.setNomeAcao(conteudoLinha[coluna]);
			coluna = DIVIDENDO_COLUNA_VL_RECE;
			dividendoModel.setValorRecebido(StringUtils.stringNumericaParaNumero(conteudoLinha[coluna])); 
			coluna = DIVIDENDO_COLUNA_VL_ACOE_RECE;
			dividendoModel.setValorAcoesRecebido( Integer.parseInt(conteudoLinha[coluna]));	
			coluna = DIVIDENDO_COLUNA_VL_ACAO_IR;
			if (conteudoLinha.length > DIVIDENDO_COLUNA_VL_ACAO_IR &&
					"" != conteudoLinha[coluna] && conteudoLinha[coluna] != null) {
				dividendoModel.setValorAcaoIr(StringUtils.stringNumericaParaNumero(conteudoLinha[coluna]));
			}
			
		} catch (Exception e) {
			throw new PlanilhaException( e.getMessage(), linha, coluna+1);
		}
		return dividendoModel;
	}
	
}

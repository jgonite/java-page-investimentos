package com.jgonite.port.http.out.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InvesteSiteDadosAcoesRequestDTO {
	
	private String tipodata;
	private String versao;
	private String data_tabela;
	private String cons_tabela;
	private String codcvm;
	private String cod_negociacao;
	private String tabela_id;
	private String tipo_ord_pref;
	private String tipoemp;
	
	public static InvesteSiteDadosAcoesRequestDTO montarRequestRoicMargensEndividamento() {
		InvesteSiteDadosAcoesRequestDTO n = new InvesteSiteDadosAcoesRequestDTO();
		n.setTipodata("hist");
		n.setVersao("01");
		n.setCons_tabela("2");
		n.setTabela_id("tabela_resumo_empresa_margens_retornos");
		n.setTipo_ord_pref("Ordinária");
		n.setTipoemp("1");
		return n;
	}

}

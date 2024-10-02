package com.jgonite.port.http.out;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.jgonite.port.http.out.dto.InvesteSiteDadosAcoesRequestDTO;
import com.jgonite.port.http.out.dto.SessaoPrincipaisIndicadoresDTO;

@Component
public class InvesteSitePort {
	
	@Value("${url.investe-site.principais-indicadores}")
	private String urlPrincipaisIndicadores;
	
	@Value("${url.investe-site.dados-acoes}")
	private String urlExtrairRoicMargensEndividamento;
	
	@Cacheable(value = "investeSiteRanking")
	public String extrairRanking(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		return doc.select("table#tabela_selecao_acoes").first().outerHtml();
	}
	
	public SessaoPrincipaisIndicadoresDTO criarSessaoPrincipaisIndicadores(String ticker) throws IOException {
		StringBuilder sb = new StringBuilder();
		String tickerUrl = sb.append(urlPrincipaisIndicadores).append(ticker).toString();
		SessaoPrincipaisIndicadoresDTO sessao = new SessaoPrincipaisIndicadoresDTO();
		Connection.Response connectionGet = Jsoup.connect(tickerUrl)
                .method(Connection.Method.GET)
                .execute();
		Document document = connectionGet.parse();
		sessao.setCookies(connectionGet.cookies());
		sessao.setCsrfToken(document.select("meta[name=csrf-token]").attr("content"));
		
		Pattern codcvmPattern = Pattern.compile("let codcvm = \"(.*?)\";");
        Pattern tipoempPattern = Pattern.compile("let tipoemp = \"(.*?)\";");
        String codcvm = null;
        String tipoemp = null;
        Elements scripts = document.getElementsByTag("script");
        for (Element script : scripts) {
            String scriptContent = script.html();
            Matcher codcvmMatcher = codcvmPattern.matcher(scriptContent);
            if (codcvmMatcher.find()) {
                codcvm = codcvmMatcher.group(1);
            }            Matcher tipoempMatcher = tipoempPattern.matcher(scriptContent);
            if (tipoempMatcher.find()) {
                tipoemp = tipoempMatcher.group(1);
            }
        }
        
		sessao.setCodcvm(codcvm);
		sessao.setTipoemp(tipoemp);
		sessao.setOuterHml(document.outerHtml());
		return sessao;
	}
	
	public String extrairRoicMargensEndividamento(String ticker, String odate, SessaoPrincipaisIndicadoresDTO sessao) throws IOException, InterruptedException {
		InvesteSiteDadosAcoesRequestDTO requestDTO = InvesteSiteDadosAcoesRequestDTO.montarRequestRoicMargensEndividamento();
		requestDTO.setData_tabela(odate);
		requestDTO.setCod_negociacao(ticker);
		requestDTO.setCodcvm(sessao.getCodcvm());
		requestDTO.setTipoemp(sessao.getTipoemp());

		return Jsoup.connect(urlExtrairRoicMargensEndividamento)
                .cookies(sessao.getCookies())
                .data("tipodata", requestDTO.getTipodata())  // Replace with actual form data if needed
                .data("versao", requestDTO.getVersao())  // Replace with actual form data if needed
                .data("data_tabela", requestDTO.getData_tabela())  // Replace with actual form data if needed
                .data("cons_tabela", requestDTO.getCons_tabela())  // Replace with actual form data if needed
                .data("codcvm", requestDTO.getCodcvm())  // Replace with actual form data if needed
                .data("cod_negociacao", requestDTO.getCod_negociacao())  // Replace with actual form data if needed
                .data("tabela_id", requestDTO.getTabela_id())  // Replace with actual form data if needed
                .data("tipo_ord_pref", requestDTO.getTipo_ord_pref())  // Replace with actual form data if needed
                .data("tipoemp", requestDTO.getTipoemp())  // Replace with actual form data if needed
                .header("csrf-token", sessao.getCsrfToken())
                .method(Connection.Method.POST)
                .execute()
                .parse()
                .outerHtml();
		
	}
	
}

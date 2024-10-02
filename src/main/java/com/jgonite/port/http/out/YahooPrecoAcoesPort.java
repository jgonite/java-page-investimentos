package com.jgonite.port.http.out;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jgonite.adapter.postgres.model.PrecoAcaoModel;
import com.jgonite.adapter.postgres.model.compositekey.PrecoAcaoPrimaryKey;
import com.jgonite.port.http.PrecoAcoesPort;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class YahooPrecoAcoesPort implements PrecoAcoesPort {

	@Value("${scrapping.url.yahoo-dados-historicos}")
	private String url;

	public List<PrecoAcaoModel> obterListPrecoAcaoModel(String ticker) {
		List<PrecoAcaoModel> retorno = new ArrayList<PrecoAcaoModel>();

		LocalDate today = LocalDate.now();
		LocalDate fiveYearsAgo = today.minusYears(5);
		ZonedDateTime zonedDateTime = today.atStartOfDay(ZoneId.systemDefault());
		ZonedDateTime zonedDateTime5y = fiveYearsAgo.atStartOfDay(ZoneId.systemDefault());
		String epoch = String.valueOf(zonedDateTime.toInstant().toEpochMilli() / 1000);
		String epoch5y = String.valueOf(zonedDateTime5y.toInstant().toEpochMilli() / 1000);
		String urlTicker = url.replace("{TICKER}", ticker).replace("{TIMESTAMP1}", epoch5y).replace("{TIMESTAMP2}",
				epoch);
		Document doc = null;
		try {
			doc = Jsoup.connect(urlTicker).get();
		} catch (IOException e) {
			log.error("Erro ao obter html yahoo.finance: " + e.getMessage());
		}
		Element table = doc.select("table.table.yf-ewueuo.noDl").first();
		if (table != null) {
			boolean header = true;
			Elements rows = table.select("tr");
			for (Element row : rows) {
				if (header) {
					header = false;
					continue;
				}
				String dateString = "";
				try {
					Elements cols = row.select("td");
					dateString = cols.get(0).text();
					if (cols.size() > 4) {
						BigDecimal close = new BigDecimal(cols.get(4).text());
						DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
						DateTimeFormatter odateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
						LocalDate date = LocalDate.parse(dateString, inputFormatter);
						String odate = date.format(odateFormatter);
						PrecoAcaoModel pam = new PrecoAcaoModel();
						pam.setChavePrimaria(PrecoAcaoPrimaryKey.builder().nmAcao(ticker).odate(odate).build());
						pam.setValorAcao(close);
						retorno.add(pam);
					}

				} catch (Exception e) {
					log.error("Erro ao obter valor de " + ticker + " para " + dateString + ": " + e.getMessage());
				}

			}
		} else {
			log.error("Tabela yahoo.finance não encontrada para " + ticker);
		}
		return retorno;
	}

}

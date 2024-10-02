package com.jgonite.port.http.out;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.jgonite.adapter.postgres.model.PrecoAcaoModel;
import com.jgonite.adapter.postgres.model.compositekey.PrecoAcaoPrimaryKey;
import com.jgonite.domain.util.StringUtils;
import com.jgonite.port.http.PrecoAcoesPort;
import com.jgonite.port.http.out.dto.BrapiHistoricalDataPrice;
import com.jgonite.port.http.out.dto.BrapiRootResponseDTO;

@Component
public class BrapiPrecoAcoesPort implements PrecoAcoesPort {
	
	@Value("${open-apis.brapi.uri-acao-3mes}")
	private String url;
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	public List<PrecoAcaoModel> obterListPrecoAcaoModel(String ticker)  {
		List<PrecoAcaoModel> retorno = new ArrayList<PrecoAcaoModel>();
		String urlTicker = url.replace("{TICKER}", ticker);
		HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
		ResponseEntity<BrapiRootResponseDTO> response = restTemplate.exchange(
					urlTicker,
					HttpMethod.GET,
					entity,
					BrapiRootResponseDTO.class);
		List<BrapiHistoricalDataPrice> prices = Optional.ofNullable(response).map(x->x.getBody()).map(x->x.getResults()).map(x->x.get(0)).map(x->x.getHistoricalDataPrice()).orElse(new ArrayList<>());
		for (BrapiHistoricalDataPrice price : prices ) {
            BigDecimal close = new BigDecimal( price.getClose() ).setScale(2, RoundingMode.HALF_UP);
            String date = StringUtils.timestampParaLocalDDMMYYYY(price.getDate(), false);
            retorno.add(PrecoAcaoModel.builder()
            		.chavePrimaria(PrecoAcaoPrimaryKey.builder()
            				.odate(date)
            				.nmAcao(ticker).build())
            		.valorAcao(close).build());
        }
		return retorno;
	}
	
}

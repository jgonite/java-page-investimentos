package com.jgonite.adapter.http.controller;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jgonite.domain.service.CarteiraAcoesService;
import com.jgonite.domain.service.DividendoAcoesService;
import com.jgonite.domain.service.FundamentusService;
import com.jgonite.domain.service.InvesteSiteService;
import com.jgonite.domain.service.MovimentoAcoesService;

@Controller
@RequestMapping
public class RecursosFrontController {
	
	@Autowired private CarteiraAcoesService carteiraAcoesService;	
	@Autowired private FundamentusService fundamentusService;
	@Autowired private InvesteSiteService investeSiteService;
	@Autowired private DividendoAcoesService dividendoAcoesService;
	@Autowired private MovimentoAcoesService movimentoAcoesService;
	
	
	@GetMapping
	public String elementosIndex(Model model) throws IOException {
		model.addAttribute("acoesDisponiveis", fundamentusService.listarAcoesDeUmSetor(""));
		model.addAttribute("acoesDaCarteira", carteiraAcoesService.calcularCarteira());
		return "index";
	}
	
	@GetMapping("/magic-formula")
	public String elementosMagicFormula(Model model) throws IOException {
		model.addAttribute("ranking", fundamentusService.getRanking());
		return "magic-formula";
	}
	
	@GetMapping("/acoes-mais-baratas/capital-intensivo")
	public String elementosAcoesMaisBaratasCapitalIntensivo(Model model) throws IOException {
		model.addAttribute("ranking", investeSiteService.extrairRankingCapitalIntesivo());
		return "acm-capital-intenstivo";
	}
	
	@GetMapping("/acoes-mais-baratas/utilidades")
	public String elementosAcoesMaisBaratasUtilidades(Model model) throws IOException {
		model.addAttribute("ranking", investeSiteService.extrairRankingUtilidades());
		return "acm-utilidades";
	}
	
	@GetMapping("/acoes-mais-baratas/financeiras")
	public String elementosAcoesMaisBaratasFinanceiras(Model model) throws IOException {
		model.addAttribute("ranking", investeSiteService.extrairRankingFinanceiras());
		return "acm-financeiras";
	}
	
	@GetMapping("/detalhes-acoes/{ticker}")
	public String elementosAcoesMaisBaratasFinanceiras(@PathVariable("ticker") String ticker, Model model) throws IOException, InterruptedException, ExecutionException {
		model.addAttribute("detalhesAcao", investeSiteService.extrairDetalhesAcao(ticker));
		return "detalhes-acoes";
	}
	
	@GetMapping("/historico-transacoes")
	public String elementosHistoricoTransacoes(Model model) throws IOException {
		model.addAttribute("dividendos", dividendoAcoesService.list());
		model.addAttribute("movimentacoes", movimentoAcoesService.list());
		return "historico-transacoes";
	}

}

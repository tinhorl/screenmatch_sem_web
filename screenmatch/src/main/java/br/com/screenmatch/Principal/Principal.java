package br.com.screenmatch.Principal;

import br.com.screenmatch.model.DadosEpisodio;
import br.com.screenmatch.model.DadosSerie;
import br.com.screenmatch.model.DadosTemporada;
import br.com.screenmatch.model.Episodios;
import br.com.screenmatch.service.ConsumoApi;
import br.com.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    final private String ENDERECO = "https://www.omdbapi.com/?t=";
    final private String ENDERECO_2 = "&season=";
    final private String API_KEY = "&apikey=8977765b";

    public void exibeMenu(){
        System.out.println("Digite o nome da série para busca: ");
        var nomeSerie =leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

        DadosSerie dados = conversor.obeterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();


        for(int i=1; i<=dados.totalTemporada(); i++ ){
            json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + ENDERECO_2 + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obeterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);


        }

        temporadas.forEach(System.out::println);

        temporadas.forEach( t -> t.episodios().forEach( e -> System.out.println(e.titulo())));

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());
        System.out.println("\nTop 5 episodios: ");

        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episodios> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodios(t.numero(), d)))
                .collect(Collectors.toList());

        episodios.forEach(System.out::println);

    }
}

package br.com.emanoel.test;

import br.com.emanoel.payment.model.Customer;
import br.com.emanoel.payment.model.Payment;
import br.com.emanoel.payment.model.Product;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class ProdutosMaisVendidos {

    public static void main(String[] args) {

        Customer emanoel = new Customer("Emanoel");
        Customer ana = new Customer("Ana");
        Customer david = new Customer("David");
        Customer lucas = new Customer("Lucas");

        Product vivaldi = new Product("Quatro Estações de Vivaldi",
                Paths.get("/music/vivaldi.mp3"), new BigDecimal(190));
        Product mrMorale = new Product("Mr. Morale & the Big Steppers de Kendrick Lamar",
                Paths.get("/music/mrmorale.mp3"), new BigDecimal(250));
        Product truman = new Product("O Show de Truman",
                Paths.get("/movies/truman.mov"), new BigDecimal(150));
        Product clubeLuta = new Product("Clube da Luta",
                Paths.get("/movies/clubeluta.mov"), new BigDecimal(165));
        Product boJack = new Product("BoJack Horseman",
                Paths.get("/images/truman.jpg"), new BigDecimal(90));

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime yesterday = today.minusDays(1);
        LocalDateTime lastMonth = today.minusMonths(1);

        Payment payment1 = new Payment(asList(vivaldi, mrMorale), today, emanoel);
        Payment payment2 = new Payment(asList(vivaldi, boJack, clubeLuta), yesterday, ana);
        Payment payment3 = new Payment(asList(boJack, truman, clubeLuta), today, david);
        Payment payment4 = new Payment(asList(boJack, truman, mrMorale, clubeLuta), lastMonth, lucas);
        Payment payment5 = new Payment(asList(truman, clubeLuta), yesterday, emanoel);

        List<Payment> payments = asList(payment1, payment2, payment3, payment4, payment5);

        //queremos saber os produtos mais vendidos
        Stream<Product> products = payments.stream()
                .map(Payment::getProducts)
                .flatMap(List::stream); //.flatMap(p -> p.stream());

        //podemos juntar dois maps (independente de um deles ser flat) em um único map
        Stream<Product> products2 = payments.stream()
                .flatMap(p -> p.getProducts().stream());

        //gerar um Map de Product para Long, esse Long indica quantas vezes esse produto foi vendido
        //usaremos o groupingBy
        Map< Product, Long> topProducts = payments.stream()
                .flatMap(p -> p.getProducts().stream())
                        .collect(Collectors.groupingBy(Function.identity(),
                                Collectors.counting()));

        //imprimindo na tela, linha a linha
        topProducts.entrySet().stream()
                .forEach(System.out::println);

        //pedir a maior entrada do mapa considerando um Comparator que compare o value de cada entrada
        //representada pela interface Map.Entry
        topProducts.entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue)) //max devolve um Optional = (ifPresente)
                .ifPresent(System.out::println);






    }
}

package br.com.emanoel.test;

import br.com.emanoel.payment.model.Customer;
import br.com.emanoel.payment.model.Payment;
import br.com.emanoel.payment.model.Product;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class RelatoriosComData {

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

        //É muito simples separarmos os pagamentos por data, usando um groupingBy(Payment::getDate)
        //LocalDateTime vai agrupar os pagamentos até pelos milissegundos. Não é o que queremos
        //agrupar por LocalDate usando um groupingBy(p -> p.getDate().toLocalDate())
        //podemos usar um intervalo maior, como por ano e mês = YearMonth
        Map<YearMonth, List<Payment>> paymentsPerMonth = payments.stream()
                .collect(Collectors.groupingBy(p -> YearMonth.from(p.getDate())));
        //linha a linha
        paymentsPerMonth.entrySet().stream()
                .forEach(System.out::println);

        //e se quisermos saber por mês, quanto foi faturado na loja?
        //basta agrupar com o mesmo critério e usar redução = somando todos os preços de todos produtos de todos pagamentos
        Map<YearMonth, BigDecimal> paymentsValuePerMonth = payments.stream()
                .collect(Collectors.groupingBy(p -> YearMonth.from(p.getDate()),
                Collectors.reducing(BigDecimal.ZERO, p -> p.getProducts().stream()
                        .map(Product::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add),
                        BigDecimal::add)));
        System.out.println(paymentsValuePerMonth);


    }
}

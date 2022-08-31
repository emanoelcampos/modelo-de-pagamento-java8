package br.com.emanoel.test;

import br.com.emanoel.payment.model.Customer;
import br.com.emanoel.payment.model.Payment;
import br.com.emanoel.payment.model.Product;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class TotalPagamentos {

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

        //reduzindo BigDecimal em somas
        //calculo do valor total de pagamento "payment1" usando API de Stream

        //se preço fosse um int, poderíamos usar o mapToDouble e invocar o sum do DoubleStream
        //resultante. Não é o caso. Teremos um Stream<BigDecimal> e ele não possui um sum

        //redução na mão realizando a soma de BigDecimal
        payment1.getProducts().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal::add)
                .ifPresent(System.out::println);

        //e se precisarmos somar todos os valores de todos os pagamentos da lista payments?
        Stream<BigDecimal> pricesStream =
                payments.stream()
                        .map(p -> p.getProducts().stream()
                                .map(Product::getPrice)
                                .reduce(BigDecimal.ZERO, BigDecimal::add));

        //repetir a operação de reduce para somar esses valores intermediários
        // realizamos a soma de preços dos produtos de cada pagamento, agora vamos somar cada um desses subtotais:
        BigDecimal total =
                payments.stream()
                        .map(p -> p.getProducts().stream()
                                .map(Product::getPrice)
                                .reduce(BigDecimal.ZERO, BigDecimal::add))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println(total);

        //código está um pouco repetitivo. Em vez de realizarmos operações de soma em
        //momentos distintos, podemos criar um único Stream<BigDecimal> com os valores
        // de todos os produtos de todos pagamentos. Conseguimos esse Stream usando o flatMap
        Stream<BigDecimal> priceOfRachProduct =
                payments.stream()
                        .flatMap(p -> p.getProducts().stream().map(Product::getPrice));

        //para somar todo esse Stream<BigDecimal>, basta realizarmos a operação de reduce
        BigDecimal totalFlat =
                payments.stream()
                        .flatMap(p -> p.getProducts().stream()
                                .map(Product::getPrice))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println(totalFlat);
    }
}

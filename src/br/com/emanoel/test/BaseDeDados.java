package br.com.emanoel.test;

import br.com.emanoel.payment.model.Product;
import br.com.emanoel.payment.model.Customer;
import br.com.emanoel.payment.model.Payment;
import br.com.emanoel.payment.model.Subscription;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static java.util.Arrays.asList;

public class BaseDeDados {

    public static void main(String[] args) {

        //testes:

        //criando clientes
        Customer emanoel = new Customer("Emanoel");
        Customer ana = new Customer("Ana");
        Customer david = new Customer("David");
        Customer lucas = new Customer("Lucas");

        //criando produtos
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

        //criando pagamentos
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime yesterday = today.minusDays(1);
        LocalDateTime lastMonth = today.minusMonths(1);

        //por uma questão de legibilidade, o import estático de Arrays.asList é feito
        Payment payment1 = new Payment(asList(vivaldi, mrMorale), today, emanoel);
        Payment payment2 = new Payment(asList(vivaldi, boJack, clubeLuta), yesterday, ana);
        Payment payment3 = new Payment(asList(boJack, truman, clubeLuta), today, david);
        Payment payment4 = new Payment(asList(boJack, truman, mrMorale, clubeLuta), lastMonth, lucas);
        Payment payment5 = new Payment(asList(truman, clubeLuta), yesterday, emanoel);

        List<Payment> payments = asList(payment1, payment2, payment3, payment4, payment5);

        //ordernar os pagamentos por datas e imprimi-los:
        payments.stream()
                .sorted(Comparator.comparing(Payment::getDate))
                .forEach(System.out::println);

        //temos três usuarios com assinatiras de 99.90, dois deles encerraram suas assinaturas:
        BigDecimal monthlyFee = new BigDecimal("99.90");

        Subscription s1 = new Subscription(monthlyFee, yesterday.minusMonths(5), emanoel);
        Subscription s2 = new Subscription(monthlyFee, yesterday.minusMonths(8), today.minusMonths(1), ana);
        Subscription s3 = new Subscription(monthlyFee, yesterday.minusMonths(5), today.minusMonths(2), david);

        List<Subscription> subscription = Arrays.asList(s1, s2, s3);

    }
}

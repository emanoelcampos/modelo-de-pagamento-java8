package br.com.emanoel.test;

import br.com.emanoel.payment.model.Customer;
import br.com.emanoel.payment.model.Payment;
import br.com.emanoel.payment.model.Product;
import br.com.emanoel.payment.model.Subscription;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

public class SistemaDeAssinaturas{

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

        BigDecimal monthlyFee = new BigDecimal("99.90");

        Subscription s1 = new Subscription(monthlyFee, yesterday.minusMonths(5), emanoel);
        Subscription s2 = new Subscription(monthlyFee, yesterday.minusMonths(8), today.minusMonths(1), ana);
        Subscription s3 = new Subscription(monthlyFee, yesterday.minusMonths(5), today.minusMonths(2), david);

        List<Subscription> subscriptions = Arrays.asList(s1, s2, s3);

        //imagine um sistema de assinatura
        //pagando um valor mensal um cliente tem acesso a todo conteúdo do e-commerce
        //além do valor e do cliente, uma Subscription precisa ter uma data e uma hora e
        //talvez uma data de término de assinatura

        //quantos meses foram pagos atrvés daquela assinatura?
        //se a assinatura ainda estiver ativa, calculamos o intervalo
        //de tempo entre begin e a data de hoje:
        long meses = ChronoUnit.MONTHS
                .between(s1.getBegin(), LocalDateTime.now());

        //e se a assinatura terminou?
        //em vez de ifs, usamos o Optional
        long meses2 = ChronoUnit.MONTHS
                .between(s1.getBegin(), s1.getEnd().orElse(LocalDateTime.now()));

        //calcular o valor gerado por aquela assinatura
        //multiplicar esse número de meses pelo custo mensal
        BigDecimal total = s1.getMonthlyFee()
                .multiply((new BigDecimal(ChronoUnit.MONTHS
                        .between(s1.getBegin(),
                                s1.getEnd().orElse(LocalDateTime.now())))));

        //facilitar com método na própria Subscription
        //dada uma lista de subscriptions, fica fácil somar todo o total pago:
        BigDecimal totalPaid = subscriptions.stream()
                .map(Subscription::getTotalPaid)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }
}

package br.com.emanoel.test;

import br.com.emanoel.payment.model.Customer;
import br.com.emanoel.payment.model.Payment;
import br.com.emanoel.payment.model.Product;

import javax.crypto.spec.PSource;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class ProdutosDeCadaCliente {

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

        //em um primeiro momento, podemos ter, para cada Customer sua List<Payment>
        //basta agrupar payments com groupingBy(Payment::getCustomer)
        Map<Customer,List<Payment>> customerToPaymets = payments.stream()
                .collect(Collectors.groupingBy(Payment::getCustomer));

        //não estamos interessados nos payments de um Customer e sim
        //na lista de Product dentro de cada um desses Payments
        Map<Customer, List<List<Product>>> customerToProductsList =
                payments.stream()
                        .collect(Collectors.groupingBy(Payment::getCustomer,
                                Collectors.mapping(Payment::getProducts, Collectors.toList())));
        customerToProductsList.entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getKey().getName()));

        //queremos esse mesmo resultado, porém com listas achatadas em uma só
        //há duas formas e uma delas envolve flatMap do mapa resultante
        //dado o customerToProductsList, queremos que o value de cada entry seja achatado
        Map<Customer, List<Product>> customerToProducts2steps =
                customerToProductsList.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey,
                                e -> e.getValue().stream()
                                        .flatMap(List::stream)
                                        .collect(Collectors.toList())));
        customerToProducts2steps.entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getKey().getName()))
                .forEach(System.out::println);
        //Usamos o Collectors.toMap para criar um novo mapa no qual a chave
        //continua a mesma ( Map.EntrygetKey) mas o valor é o resultado do
        //flatMap dos Liststream de todas as listas

        //há outrasformas de resolver o mesmo problema. Podemos usar o reducing
        //mais uma vez, pois queremos acumular aslistas de cada cliente agrupado
        Map<Customer, List<Product>> customerToProducts = payments.stream()
                .collect(Collectors.groupingBy(Payment::getCustomer,
                        Collectors.reducing(Collections.emptyList(),
                                Payment::getProducts,
                                (l1, l2) -> { List<Product> l = new ArrayList<>();
                                    l.addAll(l1);
                                    l.addAll(l2);
                                    return l;} )));



    }
}

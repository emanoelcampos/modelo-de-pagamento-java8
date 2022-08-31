package br.com.emanoel.payment.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

//Repare no uso do Optional<LocalDateTime>. Fica interessante para expormos o getEnd,
//tornando obrigatório o cuidado com o valor nulo por parte da classe que utilizará essa informação
public class Subscription {
    private BigDecimal monthlyFee;
    private LocalDateTime begin;
    private Optional<LocalDateTime> end;
    private Customer customer;

    //Teremos dois construtores. Um para assinaturas que ainda estão ativas, outro
    //para as que se encerraram e precisam ter uma data específica para tal:


    public Subscription(BigDecimal monthlyFee, LocalDateTime being, Customer customer) {
        this.monthlyFee = monthlyFee;
        this.begin = being;
        this.end = Optional.empty();
        this.customer = customer;
    }

    public Subscription(BigDecimal monthlyFee, LocalDateTime being, LocalDateTime end, Customer customer) {
        this.monthlyFee = monthlyFee;
        this.begin = being;
        this.end = Optional.of((end));
        this.customer = customer;
    }

    public BigDecimal getMonthlyFee() {
        return monthlyFee;
    }

    public LocalDateTime getBegin() {
        return begin;
    }

    public Optional<LocalDateTime> getEnd() {
        return end;
    }

    public Customer getCustomer() {
        return customer;
    }

    public BigDecimal getTotalPaid() {
        return getMonthlyFee()
                .multiply(new BigDecimal(ChronoUnit.MONTHS
                        .between(getBegin(),
                                getEnd().orElse(LocalDateTime.now()))));
    }
}

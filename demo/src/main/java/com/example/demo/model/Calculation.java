package com.example.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "calculations")
public class Calculation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 30, scale = 10)
    private BigDecimal bilangan1;

    // "opr" dipakai karena "operator" bentrok dengan kata kunci SQL
    @Column(name = "opr", nullable = false, length = 1)
    private String operator;

    @Column(nullable = false, precision = 30, scale = 10)
    private BigDecimal bilangan2;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public BigDecimal getBilangan1() { return bilangan1; }
    public void setBilangan1(BigDecimal v) { this.bilangan1 = v; }
    public String getOperator() { return operator; }
    public void setOperator(String v) { this.operator = v; }
    public BigDecimal getBilangan2() { return bilangan2; }
    public void setBilangan2(BigDecimal v) { this.bilangan2 = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

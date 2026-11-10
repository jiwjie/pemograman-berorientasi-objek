package com.example.demo.controller;

import com.example.demo.model.Calculation;
import com.example.demo.repository.CalculationRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/calculations")
public class CalculationController {

    public record CalcRequest(BigDecimal bilangan1, String operator, BigDecimal bilangan2) {}

    // hasil hanya dikirim ke UI, tidak disimpan di database
    public record CalcResponse(Long id, BigDecimal bilangan1, String operator,
                               BigDecimal bilangan2, BigDecimal result) {}

    private final CalculationRepository repo;

    public CalculationController(CalculationRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<CalcResponse> all() {
        return repo.findAll().stream()
                .sorted((a, b) -> b.getId().compareTo(a.getId()))
                .map(c -> toResponse(c, hitung(
                        new CalcRequest(c.getBilangan1(), c.getOperator(), c.getBilangan2()))))
                .toList();
            }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CalcResponse create(@RequestBody CalcRequest req) {
        BigDecimal hasil = hitung(req);
        Calculation c = repo.save(isi(new Calculation(), req));
        return toResponse(c, hasil);
    }

    @PutMapping("/{id}")
    public CalcResponse update(@PathVariable Long id, @RequestBody CalcRequest req) {
        Calculation c = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data tidak ditemukan"));
        BigDecimal hasil = hitung(req);
        return toResponse(repo.save(isi(c, req)), hasil);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Data tidak ditemukan");
        }
        repo.deleteById(id);
    }

    // validasi + hitung (tanpa menyimpan hasil)
    private BigDecimal hitung(CalcRequest r) {
        if (r.bilangan1() == null || r.bilangan2() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bilangan 1 dan 2 harus berupa angka");
        }
        BigDecimal a = r.bilangan1(), b = r.bilangan2();
        String op = r.operator() == null ? "" : r.operator().trim();
        return switch (op) {
            case "+" -> a.add(b);
            case "-" -> a.subtract(b);
            case "x" -> a.multiply(b);
            case "/" -> {
                if (b.signum() == 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tidak bisa membagi dengan nol");
                }
                yield a.divide(b, 10, RoundingMode.HALF_UP);
            }
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Operator harus + - x atau /");
        };
    }

    private Calculation isi(Calculation c, CalcRequest r) {
        c.setBilangan1(r.bilangan1());
        c.setOperator(r.operator().trim());
        c.setBilangan2(r.bilangan2());
        return c;
    }

    private CalcResponse toResponse(Calculation c, BigDecimal hasil) {
        return new CalcResponse(c.getId(), c.getBilangan1(), c.getOperator(), c.getBilangan2(), hasil);
    }
}
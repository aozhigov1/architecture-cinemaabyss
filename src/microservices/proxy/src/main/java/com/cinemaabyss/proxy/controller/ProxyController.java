package com.cinemaabyss.proxy.controller;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.cinemaabyss.proxy.service.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api")
public class ProxyController {
    private static final Logger LOG = LoggerFactory.getLogger(ProxyController.class);

    private final ProxyService proxyService;
    private final AtomicReference<String> lastRoutedTo;

    @Autowired
    public ProxyController(ProxyService proxyService) {
        this.proxyService = proxyService;
        this.lastRoutedTo = new AtomicReference<>("none");
    }
    /**
     * Прокси для endpoint'ов фильмов
     * Поддерживает постепенную миграцию с монолита на микросервис
     */
    @GetMapping("/movies")
    public Mono<ResponseEntity<String>> getMovies() {
        return proxyService.proxyMoviesRequest("/movies")
            .map(response -> ResponseEntity.ok(response))
            .onErrorResume(error -> Mono.just(
                ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("{\"error\":\"" + error.getMessage() + "\"}")));
    }

    @GetMapping("/movies/{id}")
    public Mono<ResponseEntity<String>> getMovieById(@PathVariable String id) {
        return proxyService.proxyMoviesRequest("/movies/" + id)
            .map(response -> ResponseEntity.ok(response))
            .onErrorResume(error -> Mono.just(
                ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("{\"error\":\"" + error.getMessage() + "\"}")));
    }

    @GetMapping("/movies/health")
    public Mono<ResponseEntity<String>> getMoviesHealth() {
        return proxyService.proxyMoviesRequest("/movies/health")
            .map(response -> ResponseEntity.ok(response))
            .onErrorResume(error -> Mono.just(
                ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("{\"error\":\"" + error.getMessage() + "\"}")));
    }

    /**
     * Прокси для endpoint'ов пользователей через монолит
     */
    @GetMapping("/users")
    public Mono<ResponseEntity<String>> getUsers() {
        return proxyService.proxyMonolithRequest("/users")
            .map(response -> ResponseEntity.ok(response))
            .onErrorResume(error -> Mono.just(
                ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("{\"error\":\"" + error.getMessage() + "\"}")));
    }

    /**
     * Прокси для endpoint'ов монолита (которые ещё не мигрировали)
     */
    @GetMapping("/events/**")
    public Mono<ResponseEntity<String>> proxyMonolithEvents() {
        return proxyService.proxyMonolithRequest("/events")
            .map(response -> ResponseEntity.ok(response))
            .onErrorResume(error -> Mono.just(
                ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("{\"error\":\"" + error.getMessage() + "\"}")));
    }

    /**
     * Отдельный прокси для микросервиса событий
     */
    @GetMapping("/events-service/**")
    public Mono<ResponseEntity<String>> proxyEventsService() {
        return proxyService.proxyEventsRequest("/events")
            .map(response -> ResponseEntity.ok(response))
            .onErrorResume(error -> Mono.just(
                ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("{\"error\":\"" + error.getMessage() + "\"}")));
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("{\"status\":\"UP\",\"service\":\"proxy-service\"}");
    }

    /**
     * Endpoint для получения статуса миграции
     */
    @GetMapping("/admin/migration-status")
    public ResponseEntity<ProxyService.MigrationStatus> getMigrationStatus() {
        return ResponseEntity.ok(proxyService.getMigrationStatus());
    }

    /**
     * Endpoint для изменения процента миграции (runtime)
     */
    @PostMapping("/admin/migration-percent")
    public ResponseEntity<String> setMigrationPercent(@RequestParam int percent) {
        if (percent < 0 || percent > 100) {
            return ResponseEntity.badRequest()
                .body("{\"error\":\"Percent must be between 0 and 100\"}");
        }
        proxyService.setMoviesMigrationPercent(percent);
        return ResponseEntity.ok("{\"message\":\"Migration percent set to " + percent + "\"}");
    }
}

package com.cinemaabyss.proxy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Random;

@Service
public class ProxyService {

    private static final Logger LOG = LoggerFactory.getLogger(ProxyService.class);

    @Value("${services.monolith.url}")
    private String monolithUrl;

    @Value("${services.movies.url}")
    private String moviesUrl;

    @Value("${services.events.url}")
    private String eventsUrl;

    @Value("${migration.enabled:true}")
    private boolean migrationEnabled;

    @Value("${migration.movies-migration-percent:50}")
    private int moviesMigrationPercent;

    private final WebClient.Builder webClientBuilder;
    private final Random random;

    @Autowired
    public ProxyService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
        this.random = new Random();
    }

    /**
     * Прокси запрос к сервису фильмов с использованием паттерна Strangler Fig
     * При включенной миграции трафик распределяется между монолитом и микросервисом
     * @param path эндпоинт запроса
     * @return ответ на запрос
     */
    public Mono<String> proxyMoviesRequest(String path) {
        if (shouldRouteToMoviesService()) {
            LOG.info("Routing to movies microservice: {}", path);
            return forwardRequest(moviesUrl + "/api", path);
        } else {
            LOG.info("Routing to monolith: {}", path);
            return forwardRequest(monolithUrl + "/api", path);
        }
    }

    /**
     * Прокси запрос к монолиту
     * @param path эндпоинт запроса
     * @return ответ на запрос
     */
    public Mono<String> proxyMonolithRequest(String path) {
        LOG.info("Routing to monolith: {}", path);
        return forwardRequest(monolithUrl + "/api", path);
    }

    /**
     * Прокси запрос к сервису событий
     * @param path эндпоинт запроса
     * @return ответ на запрос
     */
    public Mono<String> proxyEventsRequest(String path) {
        LOG.info("Routing to events service: {}", path);
        return forwardRequest(eventsUrl, path);
    }

    /**
     * Определяет, направлять ли запрос к микросервису фильмов
     * При включенной миграции использует вероятностный подход
     * @return направлять ли запрос к микросервису
     */
    private boolean shouldRouteToMoviesService() {
        if (!migrationEnabled) {
            return false;
        }
        int threshold = random.nextInt(100);
        boolean routeToMovies = threshold < moviesMigrationPercent;
        LOG.debug("Migration decision: {} (threshold={}, percent={})",
            routeToMovies ? "movies" : "monolith", threshold, moviesMigrationPercent);
        return routeToMovies;
    }

    /**
     * Выполняет прямой запрос к целевому сервису
     * @param baseUrl url, на который выполняется запрос
     * @param path эндпоинт запроса
     * @return ответ на запрос
     */
    private Mono<String> forwardRequest(String baseUrl, String path) {
        WebClient webClient = webClientBuilder
            .baseUrl(baseUrl)
            .build();

        return webClient.get()
            .uri(path)
            .retrieve()
            .bodyToMono(String.class)
            .doOnError(error -> LOG.error("Error forwarding request to {}{}: {}",
                baseUrl, path, error.getMessage()))
            .onErrorResume(error -> Mono.error(
                new RuntimeException("Service unavailable: " + error.getMessage(), error)));
    }

    /**
     * Установка процента миграции
     * @param percent процент запросов, которые будут отправляться в новый сервис
     */
    public void setMoviesMigrationPercent(int percent) {
        this.moviesMigrationPercent = Math.max(0, Math.min(100, percent));
        LOG.info("Movies migration percent updated to: {}", this.moviesMigrationPercent);
    }

    /**
     * Получение текущего статуса миграции
     */
    public MigrationStatus getMigrationStatus() {
        return new MigrationStatus(migrationEnabled, moviesMigrationPercent);
    }

    public static class MigrationStatus {
        private boolean enabled;
        private int moviesMigrationPercent;

        public MigrationStatus(boolean enabled, int moviesMigrationPercent) {
            this.enabled = enabled;
            this.moviesMigrationPercent = moviesMigrationPercent;
        }

        public boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public int getMoviesMigrationPercent() {
            return moviesMigrationPercent;
        }

        public void setMoviesMigrationPercent(int moviesMigrationPercent) {
            this.moviesMigrationPercent = moviesMigrationPercent;
        }
    }
}

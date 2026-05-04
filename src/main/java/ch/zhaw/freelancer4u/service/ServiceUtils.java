package ch.zhaw.freelancer4u.service;

import org.slf4j.Logger;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

public class ServiceUtils {

    public static ExchangeFilterFunction logRequest(Logger logger) {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            logger.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) ->
                values.forEach(value -> logger.info("Header: {}={}", name, value))
            );
            return Mono.just(clientRequest);
        });
    }
}

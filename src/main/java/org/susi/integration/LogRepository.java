package org.susi.integration;

import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface LogRepository extends ReactiveCosmosRepository<MessageLog, String> {
    Flux<MessageLog> findByDestination(String destination);
}
package go.euro.client;

import go.euro.client.response.Results;

import com.google.common.base.Optional;

public interface GoEuroClient {
    Optional<Results> get(String argument);
}

package io.hhplus.tdd.point.dto.response;

public record UserPointDTO(
        long id,
        long point,
        long updateMillis
) {
}

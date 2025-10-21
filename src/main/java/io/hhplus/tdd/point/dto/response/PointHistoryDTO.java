package io.hhplus.tdd.point.dto.response;

import io.hhplus.tdd.point.domain.TransactionType;

public record PointHistoryDTO(
        long id,
        long userId,
        long amount,
        TransactionType type,
        long updateMillis
) {
}

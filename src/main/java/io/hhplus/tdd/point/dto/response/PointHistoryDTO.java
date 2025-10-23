package io.hhplus.tdd.point.dto.response;

import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.TransactionType;

public record PointHistoryDTO(
        long id,
        long userId,
        long amount,
        TransactionType type,
        long updateMillis
) {
    public static PointHistoryDTO from(PointHistory pointHistory){
        return new PointHistoryDTO(pointHistory.id() , pointHistory.userId() , pointHistory.amount() , pointHistory.type() , pointHistory.updateMillis());
    }
}

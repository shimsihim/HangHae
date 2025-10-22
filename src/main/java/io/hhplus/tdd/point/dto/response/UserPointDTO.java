package io.hhplus.tdd.point.dto.response;

import io.hhplus.tdd.point.domain.UserPoint;

public record UserPointDTO(
        long id,
        long point,
        long updateMillis
) {

    public static UserPointDTO from(UserPoint userPoint){
        return new UserPointDTO(userPoint.id(),userPoint.point(),userPoint.updateMillis());
    }
}

package io.hhplus.tdd.point.dto.request;

import jakarta.validation.constraints.Positive;

public record PointChargeDTO(
        @Positive(message = "포인트 충전은 양수만 가능합니다.")
        long amount
) {
}

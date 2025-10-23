package io.hhplus.tdd.point.dto.request;

import jakarta.validation.constraints.Positive;

public record PointUseDTO(
        @Positive(message = "포인트 사용은 양수만 가능합니다.")
      long amount
) {
}

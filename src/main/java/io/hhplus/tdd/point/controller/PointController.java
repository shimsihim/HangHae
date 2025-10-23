package io.hhplus.tdd.point.controller;

import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.dto.request.PointChargeDTO;
import io.hhplus.tdd.point.dto.request.PointUseDTO;
import io.hhplus.tdd.point.dto.response.PointHistoryDTO;
import io.hhplus.tdd.point.dto.response.UserPointDTO;
import io.hhplus.tdd.point.service.PointHistoryService;
import io.hhplus.tdd.point.service.UserPointService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/point")
@RequiredArgsConstructor
@Validated
public class PointController {

    private static final Logger log = LoggerFactory.getLogger(PointController.class);

    private final UserPointService userPointService;
    private final PointHistoryService pointHistoryService;

    @GetMapping("{id}")
    public UserPointDTO point(
            @PathVariable @Positive(message = "사용자 ID는 양수(0보다 큰 값)여야 합니다.") long id
    ) {
        return userPointService.getUserPoint(id);
    }

    @GetMapping("{id}/histories")
    public List<PointHistoryDTO> history(
            @PathVariable @Positive(message = "사용자 ID는 양수(0보다 큰 값)여야 합니다.") long id
    ) {
        return pointHistoryService.getHistoryById(id);
    }

    @PatchMapping("{id}/charge")
    public UserPointDTO charge(
            @PathVariable @Positive(message = "사용자 ID는 양수(0보다 큰 값)여야 합니다.") long id,
            @RequestBody @Valid PointChargeDTO pointChargeDTO
            ) {
        return userPointService.addUserPoint(id , pointChargeDTO.amount());
    }

    @PatchMapping("{id}/use")
    public UserPointDTO use(
            @PathVariable @Positive(message = "사용자 ID는 양수(0보다 큰 값)여야 합니다.") long id,
            @RequestBody @Valid PointUseDTO pointUseDTO
    ) {
        return userPointService.useUserPoint(id , pointUseDTO.amount());
    }
}

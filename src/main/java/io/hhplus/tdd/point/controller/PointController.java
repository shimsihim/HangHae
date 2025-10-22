package io.hhplus.tdd.point.controller;

import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.dto.request.PointChargeDTO;
import io.hhplus.tdd.point.dto.request.PointUseDTO;
import io.hhplus.tdd.point.dto.response.PointHistoryDTO;
import io.hhplus.tdd.point.dto.response.UserPointDTO;
import io.hhplus.tdd.point.service.PointHistoryService;
import io.hhplus.tdd.point.service.UserPointService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/point")
@RequiredArgsConstructor
public class PointController {

    private static final Logger log = LoggerFactory.getLogger(PointController.class);

    private final UserPointService userPointService;
    private final PointHistoryService pointHistoryService;

    @GetMapping("{id}")
    public UserPointDTO point(
            @PathVariable long id
    ) {
        return userPointService.getUserPoint(id);
    }

    @GetMapping("{id}/histories")
    public List<PointHistoryDTO> history(
            @PathVariable long id
    ) {
        return pointHistoryService.getHistoryById(id);
    }

    @PatchMapping("{id}/charge")
    public UserPointDTO charge(
            @PathVariable long id,
            @RequestBody PointChargeDTO pointChargeDTO
            ) {
        UserPointDTO ret = userPointService.addUserPoint(id , pointChargeDTO.amount());
        pointHistoryService.addChargeHistory(id , pointChargeDTO.amount());
        return ret;
    }

    @PatchMapping("{id}/use")
    public UserPointDTO use(
            @PathVariable long id,
            @RequestBody PointUseDTO pointUseDTO
    ) {
        UserPointDTO ret = userPointService.useUserPoint(id , pointUseDTO.amount());
        pointHistoryService.addUseHistory(id , pointUseDTO.amount());
        return ret;
    }
}

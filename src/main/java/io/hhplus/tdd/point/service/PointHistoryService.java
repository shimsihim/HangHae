package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.dto.response.PointHistoryDTO;

import java.util.List;

public interface PointHistoryService {

    List<PointHistoryDTO> getHistoryById(Long userId);
    PointHistoryDTO addUseHistory(Long userId , Long amount);
    PointHistoryDTO addChargeHistory(Long userId , Long amount);
}

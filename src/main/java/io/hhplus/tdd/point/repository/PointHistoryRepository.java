package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.point.domain.PointHistory;

import java.util.List;

public interface PointHistoryRepository {
    List<PointHistory> getHistoryById(Long userId);
    PointHistory addHistory(PointHistory pointHistory);
}

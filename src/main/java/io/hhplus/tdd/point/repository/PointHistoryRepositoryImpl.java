package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.TransactionType;
import io.hhplus.tdd.point.dto.response.PointHistoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PointHistoryRepositoryImpl implements  PointHistoryRepository {

    private final PointHistoryTable pointHistoryTable;

    @Override
    public List<PointHistory> getHistoryById(Long userId) {
        return pointHistoryTable.selectAllByUserId(userId);
    }

    @Override
    public PointHistory addHistory(PointHistory pointHistory) {
        return pointHistoryTable.insert(pointHistory.userId() , pointHistory.amount() , pointHistory.type() , pointHistory.updateMillis());
    }

}

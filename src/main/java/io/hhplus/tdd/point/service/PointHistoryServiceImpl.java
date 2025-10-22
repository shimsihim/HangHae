package io.hhplus.tdd.point.service;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.TransactionType;
import io.hhplus.tdd.point.dto.response.PointHistoryDTO;
import io.hhplus.tdd.point.dto.response.UserPointDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointHistoryServiceImpl implements PointHistoryService {

    private final PointHistoryTable pointHistoryTable;

    @Override
    public List<PointHistoryDTO> getHistoryById(Long userId) {
        return pointHistoryTable.selectAllByUserId(userId).stream().map(PointHistoryDTO :: from).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public PointHistoryDTO addUseHistory(Long userId, Long amount) {
        PointHistory pointHistory = PointHistory.getAddPointHistory(userId , amount , TransactionType.USE);
        PointHistory afterSave = pointHistoryTable.insert(pointHistory.userId() , pointHistory.amount() , pointHistory.type() , pointHistory.updateMillis());
        return PointHistoryDTO.from(afterSave);
    }

    @Override
    public PointHistoryDTO addChargeHistory(Long userId, Long amount) {
        PointHistory pointHistory = PointHistory.getAddPointHistory(userId , amount , TransactionType.CHARGE);
        PointHistory afterSave = pointHistoryTable.insert(pointHistory.userId() , pointHistory.amount() , pointHistory.type() , pointHistory.updateMillis());
        return PointHistoryDTO.from(afterSave);
    }
}

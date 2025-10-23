package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.domain.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserPointRepositoryImpl implements UserPointRepository {

    private final UserPointTable userPointTable;

    @Override
    public Optional<UserPoint> getUserPointByUserId(Long userId) {
        return Optional.of(userPointTable.selectById(userId));
    }

    @Override
    public UserPoint chargeUserPoint(UserPoint userPoint) {
        return userPointTable.insertOrUpdate(userPoint.id() , userPoint.point());
    }

    @Override
    public UserPoint useUserPoint(UserPoint userPoint) {
        return userPointTable.insertOrUpdate(userPoint.id() , userPoint.point());
    }

}

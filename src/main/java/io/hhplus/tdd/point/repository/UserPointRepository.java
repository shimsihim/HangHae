package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.point.domain.UserPoint;

import java.util.Optional;

public interface UserPointRepository {
    Optional<UserPoint> getUserPointByUserId(Long userId);
    UserPoint chargeUserPoint(UserPoint userPoint);
    UserPoint useUserPoint(UserPoint userPoint);
}
package io.hhplus.tdd.point.service;

import io.hhplus.tdd.common.lock.LockAnn;
import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.dto.response.UserPointDTO;
import io.hhplus.tdd.point.exception.UserNotFoundException;
import io.hhplus.tdd.point.repository.UserPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPointServiceImpl implements UserPointService {

    private final UserPointRepository userPointRepository;

    @Override
    public UserPointDTO getUserPoint(Long userId) {
        return UserPointDTO.from(getUserPointById(userId));
    }

    @LockAnn
    @Override
    public UserPointDTO addUserPoint(Long userId, Long amount) {
        UserPoint userPoint = getUserPointById(userId);
        UserPoint chargePoint = userPoint.chargePoint(amount);
        return UserPointDTO.from(userPointRepository.chargeUserPoint(chargePoint));
    }

    @LockAnn
    @Override
    public UserPointDTO useUserPoint(Long userId, Long amount) {
        UserPoint userPoint = getUserPointById(userId);
        UserPoint usePoint = userPoint.usePoint(amount);
        return UserPointDTO.from(userPointRepository.useUserPoint(usePoint));
    }

    private UserPoint getUserPointById(Long userId){
        return userPointRepository.getUserPointByUserId(userId)
            .orElseThrow(()-> new UserNotFoundException(userId));
    }
}

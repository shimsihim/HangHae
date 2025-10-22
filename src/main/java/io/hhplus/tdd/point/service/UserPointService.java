package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.dto.response.UserPointDTO;

public interface UserPointService {
    UserPointDTO getUserPoint(Long userId);
    UserPointDTO addUserPoint(Long userId , Long amount);
    UserPointDTO useUserPoint(Long userId , Long amount);
}

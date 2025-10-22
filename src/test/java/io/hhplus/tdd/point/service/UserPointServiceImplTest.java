package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.dto.response.UserPointDTO;
import io.hhplus.tdd.point.exception.PointRangeException;
import io.hhplus.tdd.point.exception.UserNotFoundException;
import io.hhplus.tdd.point.repository.UserPointRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserPointServiceImplTest {

    @InjectMocks
    UserPointServiceImpl userPointService;

    @Mock
    UserPointRepository userPointRepository;

    @Nested
    @DisplayName("유저 포인트 조회")
    class SelectByUserId{

        @ParameterizedTest(name = "id = {0} , point : {1} 조회")
        @CsvSource({
                "1, 0",
                "1, 1000",
                "3, 3000",
        })
        void 유저_포인트_조회_테스트(long id , long point){
            //given
            UserPoint up = new UserPoint(id ,point , System.currentTimeMillis());
            given(userPointRepository.getUserPointByUserId(any(Long.class))).willReturn(Optional.of(up));

            //when
            UserPointDTO updto = userPointService.getUserPoint(id);

            //then
            assertThat(updto).usingRecursiveComparison().isEqualTo(UserPointDTO.from(up));
        }

        @Test
        void 유저_미존재시_테스트(){
            //given
            long id = 1l;
            long point = 10l;
            UserPoint up = new UserPoint(id ,point , System.currentTimeMillis());
            given(userPointRepository.getUserPointByUserId(any(Long.class))).willReturn(Optional.empty());

            //when
            assertThatThrownBy(()-> userPointService.getUserPoint(id))
            //then
            .isInstanceOf(UserNotFoundException.class);

        }
    }

    @Nested
    @DisplayName("유저 포인트 충전")
    class ChargePoint{

        @ParameterizedTest(name = "충전 point : {0}")
        @CsvSource({"1000,2000,100000000"})
        void 충전_정상_테스트(long chargeAmount){
            //given
            long userId = 1l;
            long originPoint = 0l;
            UserPoint originUp = new UserPoint(userId , originPoint , System.currentTimeMillis());
            given(userPointRepository.getUserPointByUserId(userId)).willReturn(Optional.of(originUp));
            when(userPointRepository.chargeUserPoint(any(UserPoint.class))).thenAnswer(invocation -> invocation.getArgument(0));

            //when
            UserPointDTO afterChargeDto = userPointService.addUserPoint(userId , chargeAmount);

            //then
            assertThat(afterChargeDto.id()).isEqualTo(originUp.id());
            assertThat(afterChargeDto.point()).isEqualTo(originUp.point() + chargeAmount);
        }

        @ParameterizedTest(name = "충전 point : {0}")
        @CsvSource({"0 , -1000","0 , -1", "0 , -1000000"})
        void 충전_비정상_테스트(long originPoint , long chargeAmount){
            //given
            long userId = 1l;
            UserPoint originUp = new UserPoint(userId , originPoint , System.currentTimeMillis());
            given(userPointRepository.getUserPointByUserId(userId)).willReturn(Optional.of(originUp));

            //when
            PointRangeException ex = org.junit.jupiter.api.Assertions.assertThrows(PointRangeException.class , ()->userPointService.addUserPoint(userId , chargeAmount));
            //then
            assertThat(ex.getErrCode().getErrCode()).isEqualTo("U0002");
        }
    }

    @Nested
    @DisplayName("유저 포인트 사용")
    class UsePoint{
        @ParameterizedTest(name = "원금 : {0} , 사용 point : {1}")
        @CsvSource({"20000 , 1000","10000000 , 10000000", "100 , 100"})
        void 사용_정상_테스트(long originPoint , long chargeAmount){
            //given
            long userId = 1l;
            UserPoint originUp = new UserPoint(userId , originPoint , System.currentTimeMillis());
            given(userPointRepository.getUserPointByUserId(userId)).willReturn(Optional.of(originUp));
            when(userPointRepository.useUserPoint(any(UserPoint.class))).thenAnswer(invocation -> invocation.getArgument(0));

            //when
            UserPointDTO afterChargeDto = userPointService.useUserPoint(userId , chargeAmount);

            //then
            assertThat(afterChargeDto.id()).isEqualTo(originUp.id());
            assertThat(afterChargeDto.point()).isEqualTo(originUp.point() - chargeAmount);
        }

        @ParameterizedTest(name = "충전 point : {0}")
        @CsvSource({"0 , 1000","100000 , 100001", "11 , 12"})
        void 사용_비정상_테스트(long originPoint , long chargeAmount){
            //given
            long userId = 1l;
            UserPoint originUp = new UserPoint(userId , originPoint , System.currentTimeMillis());
            given(userPointRepository.getUserPointByUserId(userId)).willReturn(Optional.of(originUp));

            //when
            PointRangeException ex = org.junit.jupiter.api.Assertions.assertThrows(PointRangeException.class , ()->userPointService.useUserPoint(userId , chargeAmount));
            //then
            assertThat(ex.getErrCode().getErrCode()).isEqualTo("U0003");
        }
    }


}
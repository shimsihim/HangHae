package io.hhplus.tdd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.tdd.point.dto.request.PointChargeDTO;
import io.hhplus.tdd.point.dto.response.UserPointDTO;
import io.hhplus.tdd.point.service.UserPointService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TddApplicationTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserPointService userPointService;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        System.out.println("setup");
        for (int i = 0; i < 10; i++) {
            UserPointDTO dto = userPointService.getUserPoint((long)i);
            userPointService.useUserPoint(dto.id() , dto.point());
        }
    }

    @Nested
    @DisplayName("유저 포인트 Get")
    class GetUserPoint{

        @Test
        void 유저포인트_획득_정상() throws Exception {
            //given
            long userId = 1l;
            //when
            mvc.perform(get("/point/{id}" , userId))
                    //then
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(userId))
                    .andExpect(jsonPath("$.point").value(0))
                    .andDo(print());
        }

        @Test
        void 유저포인트_획득_오류_음수_아이디() throws Exception {
            //given
            long userId = -1l;
            //when
            mvc.perform(get("/point/{id}" , userId))
                    //then
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(containsString("양수")))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("유저 포인트 내역 Get")
    class GetUserHistory{

    }

    @Nested
    @DisplayName("유저 포인트 충전")
    class ChargePoint{
        
        @Test
        void 충전_1회_정상_테스트() throws Exception {
            //given
            long userId = 1l;
            PointChargeDTO pt = new PointChargeDTO(1000L);
            //when
            mvc.perform(patch("/point/{id}/charge" , userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pt))
                    )
                    //then
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(userId))
                    .andExpect(jsonPath("$.point").value(1000))
                    .andDo(print());
        }

        @Test
        void 충전_1회_실패_음수충전_테스트(){

        }

        @Test
        void 충전_1회_실패_음수아이디_테스트(){

        }

        @Test
        void 충전_다회_동시성_테스트() throws InterruptedException {

            //given
            long userId = 1l;
            PointChargeDTO pt = new PointChargeDTO(1000L);

            int threadPool = 7;
            CountDownLatch startLatch = new CountDownLatch(1);
            CountDownLatch endLatch = new CountDownLatch(threadPool);

            ExecutorService executorService = Executors.newFixedThreadPool(threadPool);

            //when
            for (int i = 0; i < threadPool; i++) {
                executorService.submit(() -> {
                    try {
                        startLatch.await();
                        mvc.perform(patch("/point/{id}/charge" , userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(pt))
                        );
                    }
                    catch(InterruptedException e){
                        Thread.currentThread().interrupt();
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    finally {
                        endLatch.countDown();
                    }
                });
            }
            startLatch.countDown();
            endLatch.await();
            executorService.shutdown();

            //then
            UserPointDTO dto = userPointService.getUserPoint(userId);
            assertThat(dto.point()).isEqualTo(pt.amount() * threadPool);

        }
    }

    @Nested
    @DisplayName("유저 포인트 사용")
    class UsePoint{

    }

}
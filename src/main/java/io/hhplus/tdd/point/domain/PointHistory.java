package io.hhplus.tdd.point.domain;

public record PointHistory(
        long id,
        long userId,
        long amount,
        TransactionType type,
        long updateMillis
) {
    public static PointHistory getAddPointHistory(long userId , long amount , TransactionType type){
        return new PointHistory(0,userId , amount , type , System.currentTimeMillis());
    }
}

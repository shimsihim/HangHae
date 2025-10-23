package io.hhplus.tdd.point.domain;

import io.hhplus.tdd.common.exception.ErrorCode;
import io.hhplus.tdd.point.exception.PointRangeException;

public record UserPoint(
        long id,
        long point,
        long updateMillis
) {

    private static final long MAX_POINT = 1_000_000_000L;
    private static final long MIN_CHARGE_AMOUNT = 1_000L;
    private static final long MIN_USE_AMOUNT = 100L;

    public static UserPoint empty(long id) {

        return new UserPoint(id, 0, System.currentTimeMillis());
    }

    public UserPoint chargePoint(Long chargePoint){

        if(chargePoint < 0) throw new PointRangeException(ErrorCode.USER_POINT_MUST_POSITIVE , this.id , chargePoint);

        if(chargePoint < MIN_CHARGE_AMOUNT) throw new PointRangeException(ErrorCode.USER_POINT_CHARGE_MIN_AMOUNT , this.id , chargePoint);

        long newPoint;
        try{
            newPoint = Math.addExact(this.point, chargePoint);
        }
        catch(ArithmeticException e){
            throw new PointRangeException(ErrorCode.USER_POINT_OVERFLOW ,this.id , this.point , chargePoint);
        }

        if(newPoint > MAX_POINT) throw new PointRangeException(ErrorCode.USER_POINT_MAX_EXCEEDED , this.id , this.point , chargePoint);

        return new UserPoint(this.id, newPoint, System.currentTimeMillis());
    }

    public UserPoint usePoint(Long usePoint){
        if(usePoint < 0) throw new PointRangeException(ErrorCode.USER_POINT_MUST_POSITIVE , this.id , usePoint);

        if(usePoint < MIN_USE_AMOUNT) throw new PointRangeException(ErrorCode.USER_POINT_USE_MIN_AMOUNT , this.id , usePoint);

        long newPoint;
        try{
            newPoint = Math.subtractExact(this.point, usePoint);
            if(newPoint < 0) throw new PointRangeException(ErrorCode.USER_POINT_NOT_ENOUGH , this.id , this.point , usePoint);
        }
        catch(ArithmeticException e){
            throw new PointRangeException(ErrorCode.USER_POINT_OVERFLOW ,this.id , this.point , usePoint );
        }

        return new UserPoint(this.id, newPoint, System.currentTimeMillis());
    }
}

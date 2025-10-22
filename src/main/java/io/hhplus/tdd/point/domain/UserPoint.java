package io.hhplus.tdd.point.domain;

import ch.qos.logback.core.util.TimeUtil;
import io.hhplus.tdd.common.exception.ErrorCode;
import io.hhplus.tdd.point.exception.PointRangeException;

public record UserPoint(
        long id,
        long point,
        long updateMillis
) {

    public static UserPoint empty(long id) {

        return new UserPoint(id, 0, System.currentTimeMillis());
    }

    public UserPoint chargePoint(Long chargePoint){
        if(chargePoint < 0) throw new PointRangeException(ErrorCode.USER_POINT_MUST_POSITIVE , this.id , chargePoint);
        long newPoint;
        try{
            newPoint = Math.addExact(this.point, chargePoint);
        }
        catch(ArithmeticException e){
            throw new PointRangeException(ErrorCode.USER_POINT_OVERFLOW ,this.id , this.point , chargePoint);
        }

        // 💡 새로운 인스턴스를 생성하여 반환 (불변성 유지)
        return new UserPoint(this.id, newPoint, System.currentTimeMillis());
    }

    public UserPoint usePoint(Long usePoint){
        if(usePoint < 0) throw new PointRangeException(ErrorCode.USER_POINT_MUST_POSITIVE , this.id , usePoint);
        long newPoint;
        try{
            newPoint = Math.subtractExact(this.point, usePoint);
            if(newPoint < 0) throw new PointRangeException(ErrorCode.USER_POINT_NOT_ENOUGH , this.id , this.point , usePoint);
        }
        catch(ArithmeticException e){
            throw new PointRangeException(ErrorCode.USER_POINT_OVERFLOW ,this.id , this.point , usePoint );
        }

        // 💡 새로운 인스턴스를 생성하여 반환 (불변성 유지)
        return new UserPoint(this.id, newPoint, System.currentTimeMillis());
    }
}

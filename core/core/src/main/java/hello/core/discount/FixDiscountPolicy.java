package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
//@Qualifier("fixDiscountPolicy")
public class FixDiscountPolicy implements DiscountPolicy{

    private int discountFixAmount = 1000;//1000원할인

    @Override
    public int discount(Member member, int price) {//VIP면 1000원 할인, 아니면 할인 없음
        if(member.getGrade() == Grade.VIP){//enum 타입은 == 을 사용한다
            return discountFixAmount;
        }else {
            return 0;
        }

    }
}

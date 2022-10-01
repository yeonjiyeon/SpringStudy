package hello.core.order;

public interface OrderService {
    //order결과 반환하기
    Order createOrder(Long memberId, String itemName, int itemPrice);
}

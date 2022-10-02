package hello.core.scan.filter;

import java.lang.annotation.*;

@Target(ElementType.TYPE) //Type은 클래스레벨에 붙는 것
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyIncludeComponent {

}

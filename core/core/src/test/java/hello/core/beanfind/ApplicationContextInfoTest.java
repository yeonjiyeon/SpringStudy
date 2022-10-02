package hello.core.beanfind;

import hello.core.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationContextInfoTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
    //1.스프링 컨테이너 생성 -AppConfig.class를 구성 정보로 지정

    @Test
    @DisplayName("모든 빈 출력하기")
    void findAllBean(){
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) { //배열이나 리스트 출력시 iter입력하면 for문자동생성됨
                Object bean = ac.getBean(beanDefinitionName);//타입을 알 수 없기 때문에 object로 지정됨//cnt+alt+v:변수명 자동생성
                System.out.println("name = " + beanDefinitionName + "object = " + bean); //soutv 변수명 찍어준다
            }//for
    }

    @Test
    @DisplayName("애플리케이션 빈 출력하기")
    void findApplicationBean(){
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);//빈 하나하나에 대한 메타데이터정보
            if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION){//내가 등록한 빈들만 볼 수 있게 하기 위해
                //Role ROLE_APPLICATION: 직접 등록한 애플리케이션 빈
                //Role ROLE_INFRASTRUCTURE: 스프링이 내부에서 사용하는 빈

                Object bean = ac.getBean(beanDefinitionName);
                System.out.println("name = " + beanDefinitionName + "object = " + bean);
            }
        }//for
    }
}

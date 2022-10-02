package hello.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(//@component를 찾아서 스캔해준다
//뺄 것이 있을때는  excludeFilters를 사용한
// basePackages = "hello.core",
// basePackageClasses = AutoAppConfig.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
        //appconfig를 잠시 막아주기 위해 사용한다
)
public class AutoAppConfig {
}

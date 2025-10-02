package hello.login;

import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 스프링 부트로 Filter 등록하기
 * FilterRegistrationBean 사용
 */
@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean logFilter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter()); //등록할 필터 지정.
        filterRegistrationBean.setOrder(1); //필터 체인에서의 순서
        filterRegistrationBean.addUrlPatterns("/*"); //모든url 적용

        return filterRegistrationBean;
    }
    @Bean
    public FilterRegistrationBean loginCheckFilter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter()); //등록할 필터 지정.
        filterRegistrationBean.setOrder(2); //필터 체인에서의 순서
        filterRegistrationBean.addUrlPatterns("/*"); //모든url 적용

        return filterRegistrationBean;
    }
}

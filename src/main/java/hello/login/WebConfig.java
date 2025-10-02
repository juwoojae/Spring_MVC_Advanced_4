package hello.login;

import hello.login.web.argumentresolver.LoginMemberArgumentResolver;
import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.interceptor.LogInterceptor;
import hello.login.web.interceptor.LoginCheckIntercepter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 스프링 부트로 Filter 등록하기
 * FilterRegistrationBean 사용
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor()) //등록할 인터셉터 지정
                .order(1)  //인터셉터의 호출 순서를 지정한다.
                .addPathPatterns("/**")  //인터셉터를 적용할 URL 패턴을 지정한다.
                .excludePathPatterns("/css/", "/*.ico", "/error");   //인터셉터에서 제외할 패턴을 지정한다.

        registry.addInterceptor(new LoginCheckIntercepter()) //등록할 인터셉터 지정
                .order(2)  //인터셉터의 호출 순서를 지정한다.
                .addPathPatterns("/**")  //인터셉터를 적용할 URL 패턴을 지정한다. (모든 경로에 대해서
                .excludePathPatterns("/", "members/add", "/login", "logout", "/css/**", "/*.ico", "/error");   //인터셉터에서 제외할 패턴을 지정한다.

    }

    //@Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter()); //등록할 필터 지정.
        filterRegistrationBean.setOrder(1); //필터 체인에서의 순서
        filterRegistrationBean.addUrlPatterns("/*"); //모든url 적용

        return filterRegistrationBean;
    }

    //@Bean
    public FilterRegistrationBean loginCheckFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter()); //등록할 필터 지정.
        filterRegistrationBean.setOrder(2); //필터 체인에서의 순서
        filterRegistrationBean.addUrlPatterns("/*"); //모든url 적용

        return filterRegistrationBean;
    }
}

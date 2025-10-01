package hello.login.web.filter;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
/**
 *  HTTP 요청 -> WAS -> 필터1 -> 필터2 -> 필터3 -> 서블릿 -> 컨트롤러
 *
 * 1. 필터를 사용하려면 Filter interface 를 구현해야 한다
 * 2. HTTP 요청이 오면 doFilter 가 호출된다. (ServletRequest request 는 Http 요청이 아닌 경우까지 고려해서 만든 인터페이스이다
 * HTTP를 사용하려면 다운캐스팅을 해야한다
 */
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("log filter doFilter");

        //ServletRequest 를 받아오는데 HttpServletRequest 의 부모이다. 다운캐스팅 해줘야한다
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        //HTTP 요청을 구분하기 위해 요청당 임의의 uuid 를 생성해 둔다.
        String uuid = UUID.randomUUID().toString();
        try {
            log.info("REQUEST [{}][{}]", uuid, requestURI);
            chain.doFilter(request, response); //다음 필터가 있다면 다음필터 호출(필터 체인) 아니라면 서블릿호출
        } catch (Exception e) {

        } finally {
            log.info("RESPONSE [{}][{}]", uuid, requestURI);
        }

    }
    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
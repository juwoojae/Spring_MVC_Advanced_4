package hello.login.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    public static final String LOG_ID = "logId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString(); //요청로그를 구분하기 위한 uuid
        /**
         * 서블릿 필터의 경우 지역변수로 해결이 가능했지만, 스프링 인터셉터는 호출 시점이 완전히 분리되어있다.
         * 따라서 postHandle,afterHandle 에서 사용하려면 request.setAttribute() 사용하자
         * LogInterceptor 는 싱글톤 처럼 사용되기때문에 클래스의 속성으로 두면 큰일난다
         */
        request.setAttribute("logId", uuid);
        /**
         * 참고
         * @RequestMapping : HandlerMethod
         * 정적 리소스: ResourceHttpRequestHandler
         */
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler; //호출할 컨트롤러 메서드의 모든 정보가 포함되어 있다.
        }

        log.info("REQUEST [{}][{}][{}]", uuid, requestURI, handler);
        return true; //다음과정으로 넘어간다. handlerAdapter 단계로 넘어감
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandler [{}]", modelAndView);
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = (String) request.getAttribute(LOG_ID);
        log.info("RESPONSE [{}][{}][{}]", uuid, requestURI, handler);
        if(ex!=null){
            log.error("afterCompletion error!", ex);
        }

    }
}

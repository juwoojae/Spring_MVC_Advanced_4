package hello.login.web.filter;

import hello.login.web.SessionConst;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] whiteList = {"/", "/members/add", "/login", "/logout", "/css/*"}; //모든 url 호출에 대해서 이 경로들은 필터를 피해가야한다.

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            log.info("인증 체크 필터 시작{}", requestURI);

            if(isLoginCheckkPath(requestURI)){
                log.info("인증 체크 로직 실행 {}", requestURI);
                HttpSession session = httpRequest.getSession(false); //세션 가지고 오기 , 없으면 null
                if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER)==null){

                    log.info("미인증 사용자 요청 {}", requestURI);
                    //로그인으로 redirect
                    /**
                     * 만약 url 로 접근을 하려고 했는데 세션이 없다면 /login 으로 redirect .
                     * 여기서 로그인에 성공하면 접근하고자했던 url 로 다시 redirect하기
                     */
                    httpResponse.sendRedirect("/login?redirectURL="+requestURI);
                    return;
                }
            }
            chain.doFilter(request,response); //다음 필터 & 서블릿으로 넘어가기
        }catch (Exception e){
            throw e; //예외 로깅 가능 하지만, 톰켓까지 예외를 보내주어야 함
        }finally {
            log.info("인증 체크 필터 종료 {}", requestURI);
        }
    }
    /**
     * 화이트 리스트의 경우 인증 체크 X
     */
    private boolean isLoginCheckkPath(String requestURI){
        return !PatternMatchUtils.simpleMatch(whiteList, requestURI);
    }
}

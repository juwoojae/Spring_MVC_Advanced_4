package hello.login.web.session;

import hello.login.domain.member.Member;
import jakarta.servlet.http.HttpServletResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    void sessionTest(){
        //서블릿 환경을 모킹(mocking)하기 위해 제공되는 가짜(HttpServletResponse 구현체), 서블릿 컨테이너가 굳이 필요 없다.
        MockHttpServletResponse response = new MockHttpServletResponse();
        //세션 생성
        Member member = new Member();
        sessionManager.createSession(member, response); //세션이 생성됨 (sessionId, Member)

        //요청에 응답 쿠키 저장
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies()); //쿠키 저장소에 쿠키가 저장된다 요청헤더에 Cookie 가 온다

        //세션 조회
        Object result = sessionManager.getSession(request);
        assertThat(result).isEqualTo(member);

        //세션 만료
        sessionManager.expire(request);
        Object expired = sessionManager.getSession(request);
        assertThat(expired).isEqualTo(null);
    }
}
package hello.login.web.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);//getSession() HttpServletRequest , getCookie() Cookie[]
        if (session == null) {
            return "세션이 없습니다";
        }

        //세션 데이터 출력
        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name = {}, value={}", name, session.getAttribute(name)));

        log.info("sessionId={}", session.getId()); // 세션 id
        log.info("getMaxInactiveInterval={}", session.getMaxInactiveInterval()); //세션의 유효 시간, 예) 1800 초
        log.info("creationTime={}", new Date(session.getCreationTime()));    //세션 생성일시
        log.info("lastAccessedTime={}", new Date(session.getLastAccessedTime()));  //세션과 연결된 사용자가 최근에 서버에 접근한 시간, 클라이언트에서 서버로
        log.info("isName={}", session.isNew()); //이미 생성된 세션을 가지고 사용하기 때문

        return "세션 출력";
    }
}
/**
 * 대부분의 사용자는 로그아웃을 하지 않고, 그냥 웹 브라우저를 종료한다. 문제는 Http 가 비연결성(Stateless) 이므로 서버 입장에서는 해당
 * 사용자가 웹 브라우저를 종료한 것인지 아닌지를 인식할 수 없다. 따라서 Session 은 서버에 계속 남아 있게 된다.
 * 문제점
 * 1. 해커가 쿠키에서 SessionId 를 가지고와서 악의적인 요청을 할수도 있다.
 * 2. 세션은 기본적으로 메모리에 생성이 된다 . 사용자 10만명이 들어온뒤 바로 브라우저를 끄면 -> 10만명의 session이 남아있게 되고, 비경제적
 * 해결
 * 가장 단순하게 생각하면 새션 생성지점으로부터 30분 정도로 종료시점을 지정하는것이다. 게임을하는데 30분뒤 로그아웃된다?
 * 그래서 대안은 마지막 요청을 기준으로 30분 을 종료시점으로 삼게 되는것이다.
 * HttpSession 은 이 방식을 사용한다.
 */
package hello.login.web.session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 세션 관리
 */
@Component
public class SessionManager {

    public static final String SESSION_COOKIE_NAME = "mySessionId";
    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    /**
     * 세션 생성
     * sessionId 생성 (임의의 추청 불가능한 랜덥 값)
     * 세션 저장소에 sessionId 와 보관할 값 저장
     * sessionId 로 응답 쿠키를 생성해서 클라이언트에 전달
     */
    public void createSession(Object value, HttpServletResponse response) {

        //sessionid 를 생성하고, 값을 세션에 저장
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);

        //쿠키 생성
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        response.addCookie(mySessionCookie); //응답 헤더에 쿠키 포함
    }

    /**
     * 세션 조회
     * 서버에서 request(요청) 을 받으면 쿠키를 일단 조회해본다 -> 해당 이름의 쿠키가 없다? 시마이/있다면 이 쿠키의 value(sessionId 로 조회) 한다.
     */
    public Object getSession(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie == null) return null; // 해당 쿠키를 세션

        return sessionStore.get(sessionCookie.getValue());
    }

    /**
     * 세션 만료
     */
    public void expire(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie != null) {
            sessionStore.remove(sessionCookie.getValue()); //세션저장소의 해당 세션 만료
        }
    }

    /**
     * request.getCookie 는 반환값이 Cookie[] 이기 때문에
     * 원하는 쿠키를 찾는 과정이 필요하다 support() + 해당쿠키 찾아서 리턴하는 과정
     */
    private static Cookie findCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) {
            return null;
        } //Arrays.stream() 배열을 stream 으로 바꿔준다
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);
    }
}

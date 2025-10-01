package hello.login.web.login;


import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.session.SessionManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form){
        return "login/loginForm";
    }

   //@PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response){
        if (bindingResult.hasErrors()){
            return "login/loginForm"; //실패한다면 loginForm  에서 뷰로 오류넘기고 th:errors, th:errorclass
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        /**
         * 이렇듯 글로벌 오류는 객체 안에서 끝나지 않는 경우가 많다 (dp 에서 값을 검증한다던지)
         */
        if(loginMember == null){
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다");
            return "login/loginForm";
        }
        //로그인 성공 처리 ToDo
        /**
         * 1. 로그인을 한다
         * 2. 응답에서 Set-Cookie:sessionId 를 header 에 넣어 보낸다
         * 3. 쿠키 저장소에 쿠키를 저장하고 , 요청이 올때마다 Cookie:sessionId 를 header 에 넣어서 보낸다
         * 4. 브라우저 종료시 로그아웃 되어야 하므로 세션쿠키로 구현해야한다
         *
         * 실제 구현
         * 로그인에 성공하면 Cookie 인스턴스를 생성하고 HttpServletResponse 에 담는다, 쿠키의 이름은 "memberId" 이고
         * 값은 회원의 id 를 담아둔다
         */

        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(idCookie);
        return "redirect:/";  //일단 홈 화면으로
    }

    @PostMapping("/login")
    public String loginV2(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response){
        if (bindingResult.hasErrors()){
            return "login/loginForm"; //실패한다면 loginForm  에서 뷰로 오류넘기고 th:errors, th:errorclass
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if(loginMember == null){
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다");
            return "login/loginForm";
        }
        //세션 관리자를 통해 세션을 생성하고, 회원 데이터 보관
        sessionManager.createSession(loginMember, response);
        return "redirect:/";  //일단 홈 화면으로
    }

    //@PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        expireCookie(response, "memberId");
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logoutV2(HttpServletRequest request) {
        sessionManager.expire(request);
        return "redirect:/";
    }

    private static void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
/**
 * 쿠키와 보안 문제
 * 쿠키 값은 임의로 변경할 수 있다.
 * 쿠키에 중요한 값을 노출하지 않고, 사용자 별로 예측 불가능한 임의의 토큰(랜덤 값) 을 노출하고 서버에서 토큰과 사용자 id를 매핑해서
 * 인식한다. 그리고 서버에서 토큰을 관리한다.
 * 토큰은 해커가 임의의 값을 넣어도 찾을 수 없도록 해야한다.
 * 해커가 토큰을 털어가도 시간이 지나면 사용할 수 없도록 서버에서 해당 토큰의 만료시간을 짧게(예: 30분) 유지한다. 또는 해킹이 의심되는
 * 경우 서버에서 해당 토큰을 강제로 제거하면 된다.
 */

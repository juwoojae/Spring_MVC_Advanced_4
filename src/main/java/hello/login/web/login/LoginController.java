package hello.login.web.login;


import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import jakarta.servlet.http.Cookie;
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

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form){
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse httpServletResponse){
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
        httpServletResponse.addCookie(idCookie);
        return "redirect:/";  //일단 홈 화면으로
    }
}

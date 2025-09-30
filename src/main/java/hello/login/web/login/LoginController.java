package hello.login.web.login;


import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
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
    public String login(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult){
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

        return "redirect:/";  //일단 홈 화면으로
    }
}

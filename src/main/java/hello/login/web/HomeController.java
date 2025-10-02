package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.argumentresolver.Login;
import hello.login.web.session.SessionManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

/**
 * 비즈니스 요구사항
 * "/"
 * 로그인 전이라면 회원가입, 로그인 폼 보여주기
 * 로그인 후 쿠키가 생성된 뒤라면 홈화면 보여주기
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    //@GetMapping("/")
    public String home() {
        return "home";
    }

    //@GetMapping("/")  //쿠키의 값은 String 이지만 , 자동으로 형변환되어서 바인딩 된다
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model){

        if(memberId == null){
            return "home";
        }

        //로그인 성공 (쿠키 존재)
        Member loginMember = memberRepository.findById(memberId);
        if(loginMember == null){ //쿠키를 받아온 도중에 쿠키 세션이 만료되는 경우
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";

    }

    //@GetMapping("/")  //쿠키의 값은 String 이지만 , 자동으로 형변환되어서 바인딩 된다
    public String homeLoginV2(HttpServletRequest request, Model model){

       //세션 관리자에 저장된 회원 정보 조회
        Member member = (Member)sessionManager.getSession(request);

        if(member == null){ //쿠키를 받아온 도중에 쿠키 세션이 만료되는 경우
            return "home";
        }

        //로그인 성공 (쿠키 존재)
        model.addAttribute("member", member);
        return "loginHome";

    }

    //@GetMapping("/")  //쿠키의 값은 String 이지만 , 자동으로 형변환되어서 바인딩 된다
    public String homeLoginV3(HttpServletRequest request, Model model){
        HttpSession session = request.getSession(false);

        if(session == null){ //쿠키를 받아온 도중에 쿠키 세션이 만료되는 경우
            return "home";
        }
        //세션안에있는 멤버 값 가지고 오기
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        //세션은 있는데 Member 가 세션안에 없는경우 임의로 조작해서 넣기
        if(loginMember == null){ //쿠키를 받아온 도중에 쿠키 세션이 만료되는 경우
            return "home";
        }
        //로그인 성공 (쿠키 존재)
        model.addAttribute("member", loginMember);
        return "loginHome";

    }

    /**
     * @SessionAttribute
     * 세션 에서 name = SessionConst 라는 Value 를 가지고와서
     * 자동으로 바인딩한다.
     * require = false 라면 nullpointException 이 터지면 그냥 loginMember 를 null 로 반환하고 만다.
     * SessionAttribute 는 세션객체를 생성하지는 않는다
     */
    //@GetMapping("/")
    public String homeLoginV3Spring(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER ,required = false) Member loginMember, Model model){
        //세션은 있는데 Member 가 세션안에 없는경우 임의로 조작해서 넣기
        if(loginMember == null){ //쿠키를 받아온 도중에 쿠키 세션이 만료되는 경우
            return "home";
        }
        //로그인 성공 (쿠키 존재)
        model.addAttribute("member", loginMember);
        return "loginHome";

    }
    @GetMapping("/")
    public String homeLoginV3ArgumentResolver(@Login Member loginMember, Model model){
        //세션은 있는데 Member 가 세션안에 없는경우 임의로 조작해서 넣기
        if(loginMember == null){ //쿠키를 받아온 도중에 쿠키 세션이 만료되는 경우
            return "home";
        }
        //로그인 성공 (쿠키 존재)
        model.addAttribute("member", loginMember);
        return "loginHome";

    }

}
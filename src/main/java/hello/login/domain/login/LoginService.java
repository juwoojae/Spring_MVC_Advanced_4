package hello.login.domain.login;


import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     * @return null 로그인 실패
     */
//    public Member login(String loginId, String password) {
//        Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);
//        Member member = findMemberOptional.get();
//        if (member.getPassword().equals(password)){
//            return member;
//        } else {
//            return null;
//        }
//    }
    /**
     * 회원 가입이 아님 주의!!
     * 클라이언트가 로그인을 하는데 회원정보에 있으면 Member 를 , 없으면 null 을 반환하는 메서드임!!
     * loginId, password 를 파라메터로 입력 받은후
     * (loginId,password) 가 있다면
     * @return Member 반환
     * @return null 로그인 실패
     */
    public Member login(String loginId, String password) {
      return memberRepository.findByLoginId(loginId)
              .filter(m -> m.getPassword().equals(password))
              .orElse(null);
    }
}
/**
 * 간단하게
 * Stream vs Optional 비교하기
 * filter, map, orElse 는 stream , 아니면 Optional 로 변환해야 사용이 가능하다
 * Stream: 많은 값 을 다루는 파이프라인 (대부분 컬렉션 프레임 웤)
 * Optional: 값이 있을 수도, 없을 수도 있는 한 개의 값
 * 즉,  Optional 은 일종의 단일 원소 스트림 으로 쓸수 있게 만든 도구 라고 생각하자
 */
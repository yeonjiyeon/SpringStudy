package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional//테스트시 먼저 실행 테스트 종료시 반영시키지 않고 rollback을 해준다
public class MemberServiceIntegrationTest {
    @Autowired  MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {//join 테스트에서는 한글로 바꾸어도 괜찮음
        //Given 어떤 데이터를 기반으로 하는지
        Member member = new Member();
        member.setName("spring");

        //When 어떤것을 검증하는지
        Long saveId = memberService.join(member);

        //Then
        Member findMember  = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void 중복_회원_예외() throws Exception {//예외 사항에 대한 검증도 중요하다!!!
        //Given
        Member member1= new Member();
        member1.setName("spring");

        Member member2= new Member();
        member2.setName("spring");

        //When
        memberService.join(member1);
//        try{
//            memberService.join(member2);
//            fail();
//        }catch (IllegalStateException e){
//            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
//        } --> 결과가 애매함
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.join(member2));//예외가 발생해야 한다
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }

}

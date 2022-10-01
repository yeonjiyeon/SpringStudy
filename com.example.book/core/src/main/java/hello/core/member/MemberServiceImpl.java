package hello.core.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component//빈이 자동으로 등록 @Component("memberservice2")로 이름지정이 가능하다
public class MemberServiceImpl implements MemberService{

    //private MemberRepository memberRepository = new MemoryMemberRepository();--> AppConfig에 등록해서 구현체를 지워준다
    //null값이 들어오지 않게 구현체를 생성해 준다
    private final MemberRepository memberRepository;

    @Autowired//MemberRepository타입에 맞는 memoryMemberRepository를 주입해 준다= ac.getBean(MemberRepository.class)
    public MemberServiceImpl(MemberRepository memberRepository){//AppConfig에서 생성자를 호출하면서 구현체를 넣어준다
        this.memberRepository = memberRepository;
    }
    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
    
    
    //테스트용도
    public MemberRepository getMemberRepository(){
        return memberRepository;
    }
}

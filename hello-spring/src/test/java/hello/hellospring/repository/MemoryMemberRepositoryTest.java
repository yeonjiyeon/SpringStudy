package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemoryMemberRepositoryTest {
    MemoryMemberRepository repository = new MemoryMemberRepository();

    @AfterEach
    public void afterEach() {//테스트후 실행되는 함수
        repository.clearStore();//순서에 영향을 받지 않게 하기 위해 각각 테스트후 메모리를 비워주는 함수
    }

    @Test
    public void save(){
        //given
        Member member = new Member();
        member.setName(("spring"));

        //when
        repository.save(member);

        //then
        Member result = repository.findById(member.getId()).get();
        //optional에서 꺼낼 때는 get사용
        // System.out.println("result = " + (result == member));
        //Assertions.assertEquals(member, result);// 기대값== 실제값 (문자가 아니라 실제값을 바교할 수 있다)
        assertThat(result).isEqualTo(member);//static import
    }

    @Test
    public void findByName(){
        //given
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        //when
        Member result = repository.findByName("spring1").get();

        //then
        assertThat(result).isEqualTo(member1);
    }

    @Test
    public void findAll(){
        //given
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        //when
        List<Member> result = repository.findAll();

        //then
        assertThat(result.size()).isEqualTo(2);
    }
}

package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class MemberRepositoryTest {

  @Autowired
  MemberService memberService;
  @Autowired
  MemberRepository memberRepository;

  @Test
  void 회원가입() throws Exception {

    //given
    Member member = new Member();
    member.setName("kim");

    //when
    Long saveId = memberService.join(member);
    System.out.println("saveId >>>>>>>>>>>>>>>>>>>>>>>>>>" + saveId);

    //then
    assertEquals(member,memberRepository.findOne(saveId));
  }

  @Test
  void 중복_회원_가입() {
    // given
    Member member1 = new Member();
    member1.setName("kim11");

    Member member2 = new Member();
    member2.setName("kim11");

    // when & then
    memberService.join(member1);
    assertThrows(IllegalStateException.class, () -> {
      memberService.join(member2);
    });
  }
//  @Test
//  void 중복_회원_가입() throws Exception{
//    //given
//    Member member1 = new Member();
//    member1.setName("kim11");
//
//    Member member2 = new Member();
//    member2.setName("kim11");
//
//    //when
//    memberService.join(member1);
//    try {
//      memberService.join(member2);
//    }catch (IllegalStateException e){
//      return;
//    }
//    //then
//    fail("예외가 발생해야 합니다.");
//  }
}
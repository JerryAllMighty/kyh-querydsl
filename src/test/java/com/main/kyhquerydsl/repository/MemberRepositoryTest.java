package com.main.kyhquerydsl.repository;

import com.main.kyhquerydsl.dto.MemberSearchCondition;
import com.main.kyhquerydsl.dto.MemberTeamDto;
import com.main.kyhquerydsl.dto.QMemberTeamDto;
import com.main.kyhquerydsl.entity.Member;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.main.kyhquerydsl.entity.QMember.member;
import static com.main.kyhquerydsl.entity.QTeam.team;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void test() {
        //given
        Member member = new Member("member1", 10);
        memberRepository.save(member);
        Member findMember = memberRepository.findById(member.getId()).get();
        assertEquals(findMember, member);

        List<Member> result1 = memberRepository.findAll();
        assertThat(result1).containsExactly(member);

        List<Member> result2 = memberRepository.findByUsername("member1");
        assertThat(result2).containsExactly(member);

        //when

        //then
    }

    public List<MemberTeamDto> searchByWhereParameter(MemberSearchCondition condition) {
        //테스트 조건들 필요하면 셋팅
        return memberRepository.search(condition);
    }

    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe != null ? member.age.loe(ageLoe) : null;
    }

    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe != null ? member.age.goe(ageGoe) : null;
    }

    private BooleanExpression teamNameEq(String teamName) {
        return StringUtils.hasText(teamName) ? team.name.eq(teamName) : null;
    }

    private BooleanExpression usernameEq(String username) {
        return StringUtils.hasText(username) ? member.username.eq(username) : null;
    }

    @Test
    public void queryDslPredicateExecutorTest() {
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);

        Iterable<Member> members = memberRepository.findAll(
                member.age.between(20, 40)
                        .and(member.username.eq("member1"))
        );
        for (Member member2 : members) {
            System.out.println(member2);

        }
        //given

        //when

        //then
    }
}
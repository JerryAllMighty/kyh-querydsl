package com.main.kyhquerydsl.repository;

import com.main.kyhquerydsl.entity.Member;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {
    @Autowired
    EntityManager em;

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void test() {
        //given
        Member member = new Member("member1", 10);
        memberJpaRepository.sava(member);
        Member findMember = memberJpaRepository.findById(member.getId()).get();
        assertEquals(findMember, member);
        //when

        //then
    }

}
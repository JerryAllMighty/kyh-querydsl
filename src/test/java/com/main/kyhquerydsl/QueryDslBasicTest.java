package com.main.kyhquerydsl;

import com.main.kyhquerydsl.entity.Member;
import com.main.kyhquerydsl.entity.QMember;
import com.main.kyhquerydsl.entity.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class QueryDslBasicTest {
    @PersistenceContext
    EntityManager em;

    @BeforeEach
    public void beforeEach() {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);

        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();

        //when
        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
        for (Member member : members) {
            System.out.println(member);

        }
        //then
    }

    @Test
    public void startJpal() {
        //given
        String qlString = "select m from Member m"
                + " where m.username = :username";
        Member findMember = em.createQuery(qlString, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        //when
        assertEquals(findMember.getUsername(), "member1");

        //then

    }

    @Test
    public void setQueryDsl() {
        //given
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QMember m = new QMember("m");

        //when
        Member findMember = queryFactory.select(m)
                .from(m)
                .where(m.username.eq("member1")).fetchOne();
        //then
        assertEquals(findMember.getUsername(), "member1");
    }

}

package com.main.kyhquerydsl;

import com.main.kyhquerydsl.entity.Member;
import com.main.kyhquerydsl.entity.QMember;
import com.main.kyhquerydsl.entity.Team;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.main.kyhquerydsl.entity.QMember.member;
import static com.main.kyhquerydsl.entity.QTeam.team;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class QueryDslBasicTest {
    @PersistenceContext
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void beforeEach() {
        queryFactory = new JPAQueryFactory(em);

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

//        QMember m = new QMember("m"); //같은 테이블 조인 해야하는 경우 아니면 사용 안 해도 됨

        //when
        Member findMember = queryFactory.select(member)
                .from(member)
                .where(member.username.eq("member1")).fetchOne();
        //then
        assertEquals(findMember.getUsername(), "member1");
    }

    @Test
    public void search() {
        //given
        Member member1 = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();
        //when

        //then
        assertEquals(member1.getUsername(), "member1");
    }

    @Test
    public void searchAndParam() {
        //given
        Member member1 = queryFactory
                .selectFrom(member)
                .where(
                        member.username.eq("member1"),
                        member.age.eq(10)
                )
                .fetchOne();
        //when

        //then
        assertEquals(member1.getUsername(), "member1");
    }

    @Test
    public void resultFetch() {
        //given
        List<Member> fetch = queryFactory.
                selectFrom(member)
                .fetch();
//
//        Member fetchOne = queryFactory.
//                selectFrom(member)
//                .fetchOne();

        Member fetchFirst = queryFactory
                .selectFrom(member)
                .fetchFirst();

        QueryResults<Member> results = queryFactory
                .selectFrom(member)
                .fetchResults();

        results.getTotal();
        List<Member> content = results.getResults();

        long count = queryFactory
                .selectFrom(member)
                .fetchCount();

    }

    @Test
    public void sort() {
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        //given

        //when

        //then
    }

    @Test
    public void paging1() {
        //given
        List<Member> result = queryFactory.selectFrom(member)
                .orderBy(member.username.desc())
                .offset(0)
                .limit(2)
                .fetch();

        //when

        //then
    }

    @Test
    public void paging2() {
        //given
        QueryResults<Member> result = queryFactory.selectFrom(member)
                .orderBy(member.username.desc())
                .offset(0)
                .limit(2)
                .fetchResults();

        result.getTotal();
        result.getLimit();
        result.getOffset();
        result.getResults().size();

        //when

        //then
    }

    @Test
    public void aggregation() {
        List<Tuple> result = queryFactory
                .select(
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()
                ).from(member)
                .fetch();
        Tuple tuple = result.get(0);
        assertEquals(tuple.get(member.count()), 4);
        assertEquals(tuple.get(member.age.sum()), 100);
        assertEquals(tuple.get(member.age.avg()), 25);
    }

    @Test
    public void group() {
        List<Tuple> result = queryFactory
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertEquals(teamA.get(team.name), "teamA");
        assertEquals(teamA.get(member.age.avg()), 15);

        assertEquals(teamB.get(team.name), "teamB");
        assertEquals(teamB.get(member.age.avg()), 35);
    }

}

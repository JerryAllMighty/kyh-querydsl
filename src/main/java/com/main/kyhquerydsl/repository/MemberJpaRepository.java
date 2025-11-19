package com.main.kyhquerydsl.repository;

import com.main.kyhquerydsl.dto.MemberSearchCondition;
import com.main.kyhquerydsl.dto.MemberTeamDto;
import com.main.kyhquerydsl.entity.Member;
import com.main.kyhquerydsl.entity.QMember;
import com.main.kyhquerydsl.entity.QTeam;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.main.kyhquerydsl.entity.QMember.member;
import static com.main.kyhquerydsl.entity.QTeam.team;

@Repository
public class MemberJpaRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public MemberJpaRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public void sava(Member member) {
        em.persist(member);
    }

    public Optional<Member> findById(Long id) {
        Member findMember = em.find(Member.class, id);
        return Optional.ofNullable(findMember);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public List<Member> findAll_QueryDsl() {
        return queryFactory
                .selectFrom(member)
                .fetch();
    }

    public List<Member> findByUsername() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public List<Member> findByUsername_queryDsl(String username) {
        return queryFactory.selectFrom(member)
                .where(member.username.eq(username))
                .fetch();
    }

//    public List<MemberTeamDto> searchByBuilder(MemberSearchCondition memberSearchCondition) {
//        return queryFactory.selectFrom(new QMemberTeamDto(
//
//                ))
//                .from(member)
//                .leftJoin(member.team, team)
//                .where()
//                .fetch();
//    }
}

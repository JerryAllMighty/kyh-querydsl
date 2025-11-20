package com.main.kyhquerydsl.repository;

import com.main.kyhquerydsl.dto.MemberSearchCondition;
import com.main.kyhquerydsl.dto.MemberTeamDto;
import com.main.kyhquerydsl.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, QuerydslPredicateExecutor<Member> {
    List<Member> findByUsername(String username);

    @Override
    Page<MemberTeamDto> searchPageSimple(MemberSearchCondition memberSearchCondition, Pageable pageable);
}

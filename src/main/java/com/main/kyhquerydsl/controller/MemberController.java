package com.main.kyhquerydsl.controller;

import com.main.kyhquerydsl.dto.MemberSearchCondition;
import com.main.kyhquerydsl.dto.MemberTeamDto;
import com.main.kyhquerydsl.entity.Member;
import com.main.kyhquerydsl.repository.MemberJpaRepository;
import com.main.kyhquerydsl.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberJpaRepository memberJpaRepository;
    private final MemberRepository memberRepository;

    @GetMapping("/v1/members")
    public List<MemberTeamDto> searchMemberV1(MemberSearchCondition memberSearchCondition) {
        return memberJpaRepository.searchByWhereParameter(memberSearchCondition);
    }

    @GetMapping("/v2/members")
    public Page<MemberTeamDto> searchMemberV2(MemberSearchCondition memberSearchCondition, Pageable pageable) {
        return memberRepository.searchPageSimple(memberSearchCondition, pageable);
    }

    @GetMapping("/v3/members")
    public Page<MemberTeamDto> searchMemberV3(MemberSearchCondition memberSearchCondition, Pageable pageable) {
        return memberRepository.searchPageComplex(memberSearchCondition, pageable);
    }


}

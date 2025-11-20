package com.main.kyhquerydsl.repository;

import com.main.kyhquerydsl.dto.MemberSearchCondition;
import com.main.kyhquerydsl.dto.MemberTeamDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberRepositoryCustom {
    List<MemberTeamDto> search(MemberSearchCondition memberSearchCondition);

    Page<MemberTeamDto> searchPageSimple(MemberSearchCondition memberSearchCondition, Pageable pageable);

    Page<MemberTeamDto> searchPageComplex(MemberSearchCondition memberSearchCondition, Pageable pageable);
}

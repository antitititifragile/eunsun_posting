package com.eunsunzzang.posting.member.service;

import com.eunsunzzang.posting.member.Member;
import com.eunsunzzang.posting.member.dto.MemberSignUpDto;
import com.eunsunzzang.posting.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public void signUp(MemberSignUpDto memberSignUpDto) throws Exception {
        Member member = memberSignUpDto.toEntity();
        member.addUserAuthority();

        if(memberRepository.findByEmail(memberSignUpDto.email()).isPresent()){
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        memberRepository.save(member);
    }
}

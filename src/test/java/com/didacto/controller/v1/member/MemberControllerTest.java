package com.didacto.controller.v1.member;

import com.didacto.config.security.AuthConstant;
import com.didacto.config.security.SecurityUtil;
import com.didacto.config.security.custom.CustomUser;
import com.didacto.config.security.custom.CustomUserDetails;
import com.didacto.domain.Authority;
import com.didacto.domain.Grade;
import com.didacto.domain.Member;
import com.didacto.dto.member.MemberModificationRequest;
import com.didacto.dto.member.MemberResponse;
import com.didacto.repository.member.MemberRepository;
import com.didacto.service.member.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = MemberController.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;


    @DisplayName("전체회원을 조회한다.")
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void findAllMembers() throws Exception {
        //given
        List<MemberResponse> result = List.of();
        given(memberService.queryAll()).willReturn(result);

        //when, then
        mockMvc.perform(get("/api/v1/members"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("회원 조회에 성공했습니다."))
                .andExpect(jsonPath("$.response").isArray());

    }


    @DisplayName("특정 id로 한명의 회원을 조회한다.")
    @Test
    @WithMockUser(username = "user", roles = {AuthConstant.AUTH_USER})
    void findMember() throws Exception {
        //given
        MemberResponse result = new MemberResponse();
        given(memberService.query(1L)).willReturn(result);

        //when, then
        mockMvc.perform(get("/api/v1/members/{id}", 1L))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("회원 조회에 성공했습니다."));
    }


    @DisplayName("회원의 정보중 비밀번호, 이름, 생년월일 수정을 할 수 있다.")
    @Test
    @WithMockUser(username = "user", roles = {AuthConstant.AUTH_ALL})
    //회원정보수정
    public void edidMember() throws Exception {
        // given
        Member member1 = Member.builder()
                .id(1L)
                .email("gildong1@naver.com")
                .password("gildong123!@")
                .name("회원1")
                .birth(memberService.parseBirth("20000513"))
                .build();

        MemberModificationRequest req = new MemberModificationRequest("gildong456!@", "회원2", "19990513");


        // when, then
        mockMvc.perform(
                put("/api/v1/members")
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

//
//    @Test
//    // 회원 탈퇴
//    public void deleteMemberInfo() throws Exception {
//        // given
//        Member member = createMember(1L,"gildong456@naver.com","홍길동","gildong123456!@","19960129", Authority.ROLE_USER, Grade.Premium);
//        CustomUser customUser = new CustomUser(member); // CustomUser 생성
//        CustomUserDetails userDetails = new CustomUserDetails(customUser); // CustomUserDetails 생성
//
//        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", Collections.emptyList());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        Long userId = member.getId(); // 회원의 ID를 얻어옴
//
//        // when
//        mockMvc.perform(delete("/api/v1/members"));
//
//        // then
//        verify(memberService).delete(userId);
//    }
//


}
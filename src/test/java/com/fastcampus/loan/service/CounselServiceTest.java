package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Counsel;
import com.fastcampus.loan.dto.CounselDTO;
import com.fastcampus.loan.exception.BaseException;
import com.fastcampus.loan.exception.ResultType;
import com.fastcampus.loan.repository.CounselRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CounselServiceTest {

    @InjectMocks
    CounselServiceImpl counselService;

    @Mock
    private CounselRepository counselRepository;

    @Spy
    private ModelMapper modelMapper;
    
    @Test
    public void Should_ReturnResponseOfNewCounselEntity_When_RequestCounsel() throws Exception {
        // given
        final Counsel entity = Counsel.builder()
                .name("Member Kim")
                .cellPhone("010-1111-2222")
                .email("abc@def.g")
                .memo("저는 대출을 받고 싶어요. 연락을 주세요.")
                .zipCode("12345")
                .address("서울특별시 어딘구 모른동")
                .addressDetail("101동 101호")
                .build();

        final CounselDTO.Request request = CounselDTO.Request.builder()
                .name("Member Kim")
                .cellPhone("010-1111-2222")
                .email("abc@def.g")
                .memo("저는 대출을 받고 싶어요. 연락을 주세요.")
                .zipCode("12345")
                .address("서울특별시 어딘구 모른동")
                .addressDetail("101동 101호")
                .build();

        // when
        when(counselRepository.save(ArgumentMatchers.any(Counsel.class))).thenReturn(entity);
        final CounselDTO.Response actual = counselService.create(request);

        //then
        assertThat(actual.getName()).isSameAs(entity.getName());
    }

    @Test
    public void Should_ReturnResponseOfExistCounselEntity_When_RequestExistCounselId() throws Exception {
        // given
        final long findId = 1L;
        final Counsel entity = Counsel.builder()
                .counselId(1L)
                .build();

        // when
        when(counselRepository.findById(findId)).thenReturn(Optional.ofNullable(entity));
        final CounselDTO.Response actual = counselService.get(findId);

        //then
        assertThat(actual.getCounselId()).isSameAs(findId);
    }
    
    @Test
    public void Should_ThrowException_When_RequestNotExitsCounselId() throws Exception {
        // given
        final long findId = 2L;

        // when
        when(counselRepository.findById(findId)).thenThrow(new BaseException(ResultType.SYSTEM_ERROR));
        
        //then
        assertThatThrownBy(() -> counselService.get(findId)).isInstanceOf(BaseException.class);
    }

    @Test
    public void Should_ReturnUpdatedResponseOfExistCounselEntity_When_RequestUpdateExistCounselInfo() throws Exception {
        // given
        final long findId = 1L;

        final Counsel entity = Counsel.builder()
                .counselId(1L)
                .name("Member Kim")
                .build();

        final CounselDTO.Request request = CounselDTO.Request.builder()
                .name("Member Kang")
                .build();

        // when
        when(counselRepository.save(ArgumentMatchers.any(Counsel.class))).thenReturn(entity);
        when(counselRepository.findById(findId)).thenReturn(Optional.ofNullable(entity));

        final CounselDTO.Response actual = counselService.update(findId, request);

        //then
        assertThat(actual.getCounselId()).isSameAs(findId);
        assertThat(actual.getName()).isSameAs(request.getName());
    }
    
    @Test
    public void Should_DeleteCounselEntity_When_RequestExistCounselInfo() throws Exception {
        // given
        final long targetId = 1L;

        final Counsel entity = Counsel.builder()
                .counselId(1L)
                .build();

        // when
        when(counselRepository.save(ArgumentMatchers.any(Counsel.class))).thenReturn(entity);
        when(counselRepository.findById(targetId)).thenReturn(Optional.ofNullable(entity));

        counselService.delete(targetId);

        //then
        assertThat(entity.getIsDeleted()).isSameAs(true);
    }

}
package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Counsel;
import com.fastcampus.loan.dto.CounselDTO;
import com.fastcampus.loan.exception.BaseException;
import com.fastcampus.loan.exception.ResultType;
import com.fastcampus.loan.repository.CounselRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CounselServiceImpl implements CounselService {

    private final CounselRepository counselRepository;
    private final ModelMapper modelMapper;

    @Override
    public CounselDTO.Response create(final CounselDTO.Request request) {
        final Counsel counsel = modelMapper.map(request, Counsel.class);
        counsel.setAppliedAt(LocalDateTime.now());

        final Counsel entity = counselRepository.save(counsel);

        return modelMapper.map(entity, CounselDTO.Response.class);
    }

    @Override
    public CounselDTO.Response get(final Long counselId) {
        final Counsel counsel = counselRepository.findById(counselId).orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR));

        return modelMapper.map(counsel, CounselDTO.Response.class);
    }

    @Override
    public CounselDTO.Response update(final Long counselId, final CounselDTO.Request request) {
        final Counsel counsel = counselRepository.findById(counselId).orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR));

        counsel.setName(request.getName());
        counsel.setCellPhone(request.getCellPhone());
        counsel.setEmail(request.getEmail());
        counsel.setMemo(request.getMemo());
        counsel.setAddress(request.getAddress());
        counsel.setAddressDetail(request.getAddressDetail());
        counsel.setZipCode(request.getZipCode());

        counselRepository.save(counsel);

        return modelMapper.map(counsel, CounselDTO.Response.class);
    }

    @Override
    public void delete(final Long counselId) {
        final Counsel counsel = counselRepository.findById(counselId).orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR));

        counsel.setIsDeleted(true);

        counselRepository.save(counsel);
    }
}

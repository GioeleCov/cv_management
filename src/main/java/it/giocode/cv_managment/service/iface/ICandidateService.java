package it.giocode.cv_managment.service.iface;

import it.giocode.cv_managment.dto.req.candidate.CandidateReqDto;
import it.giocode.cv_managment.dto.req.candidate.UpdateCandidateReqDto;
import it.giocode.cv_managment.dto.resp.candidate.CandidateRespDto;

import java.util.List;

public interface ICandidateService {

    boolean createCandidate(Long userId, CandidateReqDto candidateReqDto);
    boolean updateCandidate(Long candidateId, UpdateCandidateReqDto updateCandidateReqDto);
    boolean deleteCandidate(Long candidateId);
    List<CandidateRespDto> findByName(String name);
    List<CandidateRespDto> findAll();
    List<CandidateRespDto> findByExperiences(String experiences);
    List<CandidateRespDto> findBySkills(String skills);
}

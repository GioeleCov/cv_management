package it.giocode.cv_managment.service.iface;

import it.giocode.cv_managment.dto.req.cv.CVReqDto;
import it.giocode.cv_managment.dto.req.cv.UpdateCVReqDto;
import it.giocode.cv_managment.dto.resp.cv.CVRespDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ICVService {

    boolean createCV(Long candidateId, CVReqDto cvReqDto);
    boolean updateCV(Long cvId, UpdateCVReqDto updateCVReqDto);
    boolean deleteCV(Long candidateId, Long cvId);
    List<CVRespDto> findAll();
    Resource downloadCV(String fileName);
    String saveCV(MultipartFile file, Long candidateId, CVReqDto cvReqDto) throws IOException;
}

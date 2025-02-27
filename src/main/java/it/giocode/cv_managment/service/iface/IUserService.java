package it.giocode.cv_managment.service.iface;

import it.giocode.cv_managment.dto.req.user.UserCreationReqDto;
import it.giocode.cv_managment.dto.req.user.UserLoginReqDto;
import it.giocode.cv_managment.dto.resp.candidate.CandidateRespDto;

public interface IUserService {

    boolean userRegistration(UserCreationReqDto userCreationReqDto);
    String userLogin(UserLoginReqDto userLoginReqDto);
}

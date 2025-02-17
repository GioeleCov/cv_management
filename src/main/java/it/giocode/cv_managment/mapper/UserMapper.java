package it.giocode.cv_managment.mapper;

import it.giocode.cv_managment.dto.req.user.UserCreationReqDto;
import it.giocode.cv_managment.entity.UserEntity;

public class UserMapper {

    private UserMapper() {

    }

    public static UserEntity mapToUserEntity(UserCreationReqDto userCreationReqDto) {
        return UserEntity.builder()
                .email(userCreationReqDto.getEmail())
                .password(userCreationReqDto.getPassword())
                .build();
    }
}

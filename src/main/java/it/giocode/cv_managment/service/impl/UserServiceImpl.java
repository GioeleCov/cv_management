package it.giocode.cv_managment.service.impl;

import it.giocode.cv_managment.dto.req.user.UserCreationReqDto;
import it.giocode.cv_managment.dto.req.user.UserLoginReqDto;
import it.giocode.cv_managment.entity.UserEntity;
import it.giocode.cv_managment.exception.exception_class.AlreadyExistsException;
import it.giocode.cv_managment.exception.exception_class.IncorrectPasswordsException;
import it.giocode.cv_managment.exception.exception_class.NotFoundException;
import it.giocode.cv_managment.mapper.UserMapper;
import it.giocode.cv_managment.repository.UserRepository;
import it.giocode.cv_managment.service.iface.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    @Override
    public boolean userRegistration(UserCreationReqDto userCreationReqDto) {

        if (userRepository.existsByEmail(userCreationReqDto.getEmail())) {
            throw new AlreadyExistsException("Email", userCreationReqDto.getEmail());
        }

        UserEntity user = UserMapper.mapToUserEntity(userCreationReqDto);
        setUserRole(userCreationReqDto, user);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean userLogin(UserLoginReqDto userLoginReqDto) {

        UserEntity user = userRepository.findByEmail(userLoginReqDto.getEmail())
                .orElseThrow(() -> new NotFoundException("User", "email", userLoginReqDto.getEmail()));

        if (!userLoginReqDto.getPassword().equals(user.getPassword())) {
            throw new IncorrectPasswordsException();
        }

        return true;
    }

    private void setUserRole(UserCreationReqDto userCreationReqDto, UserEntity user) {
        String email = userCreationReqDto.getEmail();
        String password = userCreationReqDto.getPassword();

        if (email.equals("admin@amdin.com") && password.equals("adminPassword")) {
            user.setRole("ADMIN");
            return;
        }

        user.setRole("USER");
    }
}

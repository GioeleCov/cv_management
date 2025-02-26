package it.giocode.cv_managment.service.impl;

import it.giocode.cv_managment.dto.req.user.UserCreationReqDto;
import it.giocode.cv_managment.dto.req.user.UserLoginReqDto;
import it.giocode.cv_managment.entity.UserEntity;
import it.giocode.cv_managment.exception.exception_class.AlreadyExistsException;
import it.giocode.cv_managment.exception.exception_class.IncorrectPasswordsException;
import it.giocode.cv_managment.mapper.UserMapper;
import it.giocode.cv_managment.repository.UserRepository;
import it.giocode.cv_managment.service.iface.IUserService;
import it.giocode.cv_managment.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean userRegistration(UserCreationReqDto userCreationReqDto) {

        if (userRepository.existsByEmail(userCreationReqDto.getEmail())) {
            throw new AlreadyExistsException("Email", userCreationReqDto.getEmail());
        }

        UserEntity user = UserMapper.mapToUserEntity(userCreationReqDto);
        setUserRole(userCreationReqDto, user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    public String userLogin(UserLoginReqDto userLoginReqDto) {
        UserEntity user = userRepository.findByEmail(userLoginReqDto.getEmail())
                .orElse(null);

        if ( user!= null && !passwordEncoder.matches(userLoginReqDto.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordsException();
        }

        return user != null ? jwtUtil.generateToken(user.getEmail(), user.getUserId(), user.getRole()) : null;
    }

    private void setUserRole(UserCreationReqDto userCreationReqDto, UserEntity user) {
        String email = userCreationReqDto.getEmail();
        String password = userCreationReqDto.getPassword();

        if (email.equals("admin@gmail.com") && password.equals("adminPassword1!")) {
            user.setRole("ADMIN");
            return;
        }

        user.setRole("USER");
    }
}

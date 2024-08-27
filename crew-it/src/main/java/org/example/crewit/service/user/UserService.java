package org.example.crewit.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.crewit.model.entity.user.User;
import org.example.crewit.repository.user.UserRepository;
import org.example.crewit.security.jwt.JwtUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * packageName : org.example.crewit.service
 * fileName : UserService
 * author : PC
 * date : 2024-08-07
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-07         PC          최초 생성
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    // ID로 사용자 조회
    public User findById(String userEmail) {
        return userRepository.findById(userEmail).get();
    }

    // 사용자 저장
    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(String userEmail){
        boolean userExists = userRepository.existsById(userEmail);
        if (userExists) {
            userRepository.deleteById(userEmail);
        }else {
            throw new RuntimeException("존재하지 않는 유저");
        }
    }


    // 사용자 ID 존재 여부 확인
    public boolean existsById(String userEmail) {
        try {
            return userRepository.existsById(userEmail);
        } catch (DataAccessException e) {
            // 데이터베이스 접근 예외 처리
            throw new RuntimeException("사용자 찾기 에러", e);
        }
    }

    public User getUserFromToken(String token){
        String jwt = jwtUtils.getJwtFromToken(token);
        String userEmail = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = findById(userEmail);
        return user;
    }

}

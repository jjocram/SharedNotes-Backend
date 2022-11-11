package it.marcof.sharednotes.service.impl;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import it.marcof.sharednotes.Repository.NoteRepository;
import it.marcof.sharednotes.Repository.UserNoteRepository;
import it.marcof.sharednotes.Repository.UserRepository;
import it.marcof.sharednotes.model.Entity.*;
import it.marcof.sharednotes.service.UserService;
import it.marcof.sharednotes.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private static final String USER_NOT_FOUND_MESSAGE = "User with username %s not found";
    private final UserRepository userRepository;
    private final NoteRepository noteRepository;
    private final UserNoteRepository userNoteRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserEntity save(UserEntity user) {
        log.info("Saving user {} to the database", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public UserEntity findByUsername(String username) {
        log.info("Retrieving user {}", username);
        return userRepository.findByUsername(username);
    }

    @Override
    public List<UserEntity> findAll() {
        log.info("Retrieving all users");
        return userRepository.findAll();
    }

    @Override
    public Map<String, String> refreshToken(String authorizationHeader, String issuer) throws BadJOSEException, ParseException, JOSEException {
        String refreshToken = authorizationHeader.substring("Bearer ".length());
        UsernamePasswordAuthenticationToken authenticationToken = JwtUtil.parseToken(refreshToken);
        String username = authenticationToken.getName();
        UserEntity userEntity = findByUsername(username);
        String accessToken = JwtUtil.createAccessToken(username,
                issuer,
                userEntity.getNotes().stream()
                        .map(userNoteEntity -> userNoteEntity.getId().getNoteId().toString())
                        .collect(Collectors.toList())
        );
        return Map.of("access_token", accessToken, "refresh_token", refreshToken);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        if(user == null) {
            String message = String.format(USER_NOT_FOUND_MESSAGE, username);
            log.error(message);
            throw new UsernameNotFoundException(message);
        } else {
            log.debug("User found in the database: {}", username);
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            user.getNotes().forEach(note -> {
                authorities.add(new SimpleGrantedAuthority(note.getId().getNoteId().toString()));
            });
            return new User(user.getUsername(), user.getPassword(), authorities);
        }
    }
}

package it.marcof.sharednotes.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import it.marcof.sharednotes.model.Entity.UserEntity;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface UserService {
    UserEntity save(UserEntity user);

    UserEntity findByUsername(String username);

    List<UserEntity> findAll();

    Map<String, String> refreshToken(String authorizationHeader, String issuer) throws BadJOSEException, ParseException, JOSEException;
}

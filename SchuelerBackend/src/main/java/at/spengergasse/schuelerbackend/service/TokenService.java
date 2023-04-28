package at.spengergasse.schuelerbackend.service;

import at.spengergasse.schuelerbackend.foundation.NanoIdFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor

@Slf4j
@Service
public class TokenService {
    private final NanoIdFactory nanoIdFactory;
    public final int DEFAULT_NANO_ID_LENGTH = 8;

    public String createNanoId(){
        return createNanoId(DEFAULT_NANO_ID_LENGTH);
    }

    public String createNanoId(int size){
        log.trace("Trying to create token with size {}", size);
        String token = nanoIdFactory.randomNanoId(size);
        log.debug("Created token with value {}", token);
        return token;
    }
}

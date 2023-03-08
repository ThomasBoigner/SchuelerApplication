package at.spengergasse.schuelerbackend.service;

import at.spengergasse.schuelerbackend.domain.Class;
import at.spengergasse.schuelerbackend.foundation.TemporalValueFactory;
import at.spengergasse.schuelerbackend.persistence.ClassRepository;
import at.spengergasse.schuelerbackend.service.dto.command.MutateClassCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor

@Service
public class ClassService {
    private final ClassRepository classRepository;
    private final TokenService tokenService;
    private final TemporalValueFactory temporalValueFactory;

    public List<Class> getClasses(){
        return classRepository.findAll();
    }

    public Optional<Class> getClass(String id){
        return classRepository.getByToken(id);
    }

    public Class createClass(MutateClassCommand command){
        Objects.requireNonNull(command, "Command must not be null!");
        Objects.requireNonNull(command.name(), "Name must not be null!");

        if(command.name().isEmpty() || command.name().isEmpty()){
            throw new IllegalArgumentException("Class name must not be null or empty!");
        }

        return classRepository.save(Class.builder()
                .name(command.name())
                .token(tokenService.createNanoId())
                .creationTS(temporalValueFactory.now())
                .build());
    }
}

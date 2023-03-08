package at.spengergasse.schuelerbackend.service;

import at.spengergasse.schuelerbackend.domain.Class;
import at.spengergasse.schuelerbackend.foundation.TemporalValueFactory;
import at.spengergasse.schuelerbackend.persistence.ClassRepository;
import at.spengergasse.schuelerbackend.service.dto.command.MutateClassCommand;
import at.spengergasse.schuelerbackend.service.dto.command.UpdateClassCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor

@Service
@Transactional(readOnly = true)
public class ClassService {
    private final ClassRepository classRepository;
    private final TokenService tokenService;
    private final TemporalValueFactory temporalValueFactory;

    public List<Class> getClasses(){
        return classRepository.findAll();
    }

    public Optional<Class> getClass(String token){
        return classRepository.getClassByToken(token);
    }

    @Transactional(readOnly = false)
    public Class createClass(MutateClassCommand command){
        Objects.requireNonNull(command, "Command must not be null!");

        return _createClass(Optional.empty(), command);
    }

   public Class replaceClass(String token, MutateClassCommand command){
       Objects.requireNonNull(command, "Command must not be null!");

        if (!classRepository.existsClassByToken(token)){
            throw new IllegalArgumentException(String.format("Class with token %s can not be found!", token));
        }

        classRepository.deleteClassByToken(token);
        return _createClass(Optional.of(token), command);
   }

   @Transactional(readOnly = false)
   public Class partiallyUpdateStudent(String token, UpdateClassCommand command){
       Objects.requireNonNull(command, "Command must not be null!");

       Optional<Class> entity = classRepository.getClassByToken(token);

       if (!entity.isPresent()) {
           throw new IllegalArgumentException(String.format("Class with token %s can not be found!", token));
       }

       Class _class = entity.get();
       if (command.name() != null && !command.name().isBlank()) _class.setName(command.name());

       return classRepository.save(_class);
   }

   @Transactional(readOnly = false)
   public void deleteClasses(){
        classRepository.deleteAll();
   }

   @Transactional(readOnly = false)
   public void deleteClass(String token){
        classRepository.deleteClassByToken(token);
   }

    private Class _createClass(Optional<String> token, MutateClassCommand command){
        LocalDateTime creationTS = temporalValueFactory.now();
        String tokenValue = token.orElseGet(() -> tokenService.createNanoId());

        Class _class = Class.builder()
                .name(command.name())
                .token(tokenValue)
                .creationTS(creationTS)
                .build();

        return classRepository.save(_class);
    }
}

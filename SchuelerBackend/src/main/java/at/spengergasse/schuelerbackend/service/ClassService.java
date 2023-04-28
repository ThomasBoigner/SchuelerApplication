package at.spengergasse.schuelerbackend.service;

import at.spengergasse.schuelerbackend.domain.Class;
import at.spengergasse.schuelerbackend.foundation.TemporalValueFactory;
import at.spengergasse.schuelerbackend.persistence.ClassRepository;
import at.spengergasse.schuelerbackend.service.dto.command.MutateClassCommand;
import at.spengergasse.schuelerbackend.service.dto.command.UpdateClassCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor

@Slf4j
@Service
@Transactional(readOnly = true)
public class ClassService {
    private final ClassRepository classRepository;
    private final TokenService tokenService;
    private final TemporalValueFactory temporalValueFactory;

    public List<Class> getClasses(){
        log.debug("Trying to get all classes");
        List<Class> allClasses = classRepository.findAll();
        log.info("Retrieved all ({}) classes", allClasses.size());
        return allClasses;
    }

    public Optional<Class> getClass(String token){
        log.debug("Trying to get the class with token {}", token);
        Optional<Class> foundClass = classRepository.getClassByToken(token);
        if (foundClass.isPresent()) {
            log.info("Found class {} with token {}", foundClass.get(), foundClass.get().getId());
        } else {
            log.info("No class with token {} found", token);
        }
        return foundClass;
    }

    @Transactional(readOnly = false)
    public Class createClass(MutateClassCommand command){
        log.debug("Trying to create class with command {}", command);
        Objects.requireNonNull(command, "Command must not be null!");

        Class createdClass = _createClass(Optional.empty(), command);
        log.info("Created class {}", createdClass);
        return createdClass;
    }

    @Transactional(readOnly = false)
    public Class replaceClass(String token, MutateClassCommand command){
        log.debug("Trying to replace class with token {} with command {}", token, command);
        Objects.requireNonNull(command, "Command must not be null!");

        if (!classRepository.existsClassByToken(token)){
            log.warn("Class with token {} can not be found!", token);
            throw new IllegalArgumentException(String.format("Class with token %s can not be found!", token));
        }

        classRepository.deleteClassByToken(token);
        log.trace("Deleted class with token {}", token);

        Class replacedClass = _createClass(Optional.of(token), command);
        log.info("Successfully replaced class {}", replacedClass);
        return replacedClass;
    }

   @Transactional(readOnly = false)
   public Class partiallyUpdateStudent(String token, UpdateClassCommand command){
       log.debug("Trying to update class with token {} with command {}", token, command);
       Objects.requireNonNull(command, "Command must not be null!");

       Optional<Class> entity = classRepository.getClassByToken(token);

       if (entity.isEmpty()) {
           log.warn("Class with token {} can not be found!", token);
           throw new IllegalArgumentException(String.format("Class with token %s can not be found!", token));
       }

       Class _class = entity.get();
       if (command.name() != null && !command.name().isBlank()) _class.setName(command.name());

       log.info("Successfully updated class {}", _class);
       return classRepository.save(_class);
   }

   @Transactional(readOnly = false)
   public void deleteClasses(){
       log.info("Successfully deleted all classes");
        classRepository.deleteAll();
   }

   @Transactional(readOnly = false)
   public void deleteClass(String token){
        log.info("Successfully deleted class with token {}", token);
        classRepository.deleteClassByToken(token);
   }

    private Class _createClass(Optional<String> token, MutateClassCommand command){
        String tokenValue = token.orElseGet(tokenService::createNanoId);
        log.trace("Token value for new class {}", tokenValue);

        Class _class = Class.builder()
                .name(command.name())
                .token(tokenValue)
                .build();

        log.trace("Mapped command {} to class object {}", command, _class);
        return classRepository.save(_class);
    }
}
package at.spengergasse.schuelerbackend.service;

import at.spengergasse.schuelerbackend.domain.Teacher;
import at.spengergasse.schuelerbackend.persistence.TeacherRepository;
import at.spengergasse.schuelerbackend.service.dto.command.MutateTeacherCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor

@Slf4j
@Service
@Transactional(readOnly = true)
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final TokenService tokenService;

    public List<Teacher> getTeachers() {
        log.debug("Trying to get all teachers");
        List<Teacher> teachers = teacherRepository.findAll();
        log.info("Retrieved all ({}) teachers", teachers.size());
        return teachers;
    }

    public Optional<Teacher> getTeacher(String token) {
        log.debug("Trying to get Teacher with token {}", token);
        Optional<Teacher> foundTeacher = teacherRepository.findByToken(token);
        if (foundTeacher.isPresent()) {
            log.info("Found teacher {} with token {}", foundTeacher.get(), foundTeacher.get().getToken());
        } else {
            log.info("No teacher with token {} found", token);
        }
        return foundTeacher;
    }

    @Transactional(readOnly = false)
    public Teacher createTeacher(MutateTeacherCommand command) {
        log.debug("Trying to create Teacher with command {}", command);
        Objects.requireNonNull(command, "Command must not be null!");

        Teacher createdTeacher = _createTeacher(Optional.empty(), command);
        log.info("Created Teacher {}", createdTeacher);
        return createdTeacher;
    }

    @Transactional(readOnly = false)
    public Teacher replaceTeacher(String token, MutateTeacherCommand command) {
        log.debug("Trying to replace teacher with token {} with command {}", token, command);
        Objects.requireNonNull(command, "Command must not be null!");

        if (!teacherRepository.existsTeacherByToken(token)) {
            log.warn("Teacher with token {} can not be found!", token);
            throw new IllegalArgumentException(String.format("Teacher with token %s can not be found!", token));
        }

        teacherRepository.deleteTeacherByToken(token);
        log.trace("Deleted Teacher with token {}", token);

        Teacher replacedTeacher = _createTeacher(Optional.of(token), command);
        log.info("Successfully replaced teacher {}", replacedTeacher);
        return replacedTeacher;
    }

    @Transactional(readOnly = false)
    public Teacher partiallyUpdateTeacher(String token, MutateTeacherCommand command) {
        log.debug("Trying to update teacher with token {} with command {}", token, command);
        Objects.requireNonNull(command, "Command must not be null!");

        Optional<Teacher> entity = teacherRepository.findByToken(token);
        if (entity.isEmpty()) {
            log.warn("Teacher with token {} can not be found!", token);
            throw new IllegalArgumentException(String.format("teacher with token %s can not be found!", token));
        }

        Teacher teacher = entity.get();
        if (command.firstname() != null) teacher.setFirstname(command.firstname());
        if (command.lastname() != null) teacher.setLastname(command.lastname());
        if (command.email() != null) teacher.setEmail(command.email());

        log.info("Successfully updated teacher {}", teacher);
        return teacherRepository.save(teacher);
    }

    @Transactional(readOnly = false)
    public void deleteTeachers() {
        log.info("Successfully deleted all teachers");
        teacherRepository.deleteAll();
    }

    @Transactional(readOnly = false)
    public void deleteTeacher(String token) {
        log.info("Successfully deleted teacher with token {}", token);
        teacherRepository.deleteTeacherByToken(token);
    }

    private Teacher _createTeacher(Optional<String> token, MutateTeacherCommand command) {
        String tokenValue = token.orElseGet(tokenService::createNanoId);
        log.trace("Token value for new teacher {}", tokenValue);

        Teacher teacher = Teacher.builder()
                .firstname(command.firstname())
                .lastname(command.lastname())
                .email(command.email())
                .token(tokenValue)
                .build();

        log.trace("Mapped command {} to teacher object {}", command, teacher);
        return teacherRepository.save(teacher);
    }
}

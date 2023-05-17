package at.spengergasse.schuelerbackend.service;

import at.spengergasse.schuelerbackend.domain.Lesson;
import at.spengergasse.schuelerbackend.domain.Subject;
import at.spengergasse.schuelerbackend.persistence.ClassRepository;
import at.spengergasse.schuelerbackend.persistence.LessonRepository;
import at.spengergasse.schuelerbackend.persistence.TeacherRepository;
import at.spengergasse.schuelerbackend.service.dto.command.MutateLessonCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor

@Slf4j
@Service
@Transactional(readOnly = true)
public class LessonService {
    private final LessonRepository lessonRepository;
    private final TeacherRepository teacherRepository;
    private final ClassRepository classRepository;
    private final TokenService tokenService;

    public List<Lesson> getLessons(){
        log.debug("Trying to get all Lessons");
        List<Lesson> lessons = lessonRepository.findAll();
        log.info("Retrieved all ({}) lessons", lessons.size());
        return lessons;
    }

    public Optional<Lesson> getLesson(String token){
        log.debug("Trying to get the lesson with token {}", token);
        Optional<Lesson> foundLesson = lessonRepository.findLessonByToken(token);
        if (foundLesson.isPresent()){
            log.info("Found lesson {} with token {}", foundLesson.get(), foundLesson.get().getToken());
        } else {
            log.info("No lesson with token {} found", token);
        }
        return foundLesson;
    }

    @Transactional(readOnly = false)
    public Lesson createLesson(MutateLessonCommand command){
        log.debug("Trying to create lesson with command {}", command);
        Objects.requireNonNull(command, "Command must not be null!");

        Lesson createdLesson = _createLesson(Optional.empty(), command);
        log.info("Created Lesson {}", createdLesson);
        return createdLesson;
    }

    @Transactional(readOnly = false)
    public Lesson replaceLesson(String token, MutateLessonCommand command){
        log.debug("Trying to replace lesson with token {} with command {}", token, command);
        Objects.requireNonNull(command, "Command must not be null!");

        if (!lessonRepository.existsLessonByToken(token)){
            log.warn("Lesson with token {} can not be found!", token);
            throw new IllegalArgumentException(String.format("Lesson with token %s can not be found!", token));
        }

        lessonRepository.deleteLessonByToken(token);
        log.trace("Deleted Lesson with token {}", token);

        Lesson replacedLesson = _createLesson(Optional.of(token), command);
        log.info("Successfully replaced lesson {}", replacedLesson);
        return replacedLesson;
    }

    @Transactional(readOnly = false)
    public Lesson partiallyUpdateLesson(String token, MutateLessonCommand command){
        log.debug("Trying to update lesson with token {} with command {}", token, command);
        Objects.requireNonNull(command, "Command must not be null!");

        Optional<Lesson> entity = lessonRepository.findLessonByToken(token);

        if (entity.isEmpty()){
            log.warn("Lesson with token {} can not be found!", token);
            throw new IllegalArgumentException(String.format("Lesson with token %s can not be found!", token));
        }

        Lesson lesson = entity.get();
        if (command.longName() != null) lesson.getSubject().setLongName(command.longName());
        if (command.shortName() != null) lesson.getSubject().setShortName(command.shortName());
        if (command._class() != null) lesson.set_class(classRepository.getClassByToken(command._class()).orElseThrow(() -> new IllegalArgumentException(String.format("Class with Token %s can not be found!", command._class()))));
        if (command.teacher() != null) lesson.setTeacher(teacherRepository.findByToken(command.teacher()).orElseThrow(() -> new IllegalArgumentException(String.format("Teacher with Token %s can not be found!", command.teacher()))));

        log.info("Successfully updated lesson {}", lesson);
        return lessonRepository.save(lesson);
    }

    @Transactional(readOnly = false)
    public void deleteLessons(){
        log.info("Successfully deleted all lessons");
        lessonRepository.deleteAll();
    }

    @Transactional(readOnly = false)
    public void deleteLesson(String token){
        log.info("Successfully deleted lesson with token {}", token);
        lessonRepository.deleteLessonByToken(token);
    }

    private Lesson _createLesson(Optional<String> token, MutateLessonCommand command) {
        String tokenValue = token.orElseGet(tokenService::createNanoId);
        log.trace("Token value for new lesson {}", tokenValue);

        Lesson lesson = Lesson.builder()
                .token(tokenValue)
                .subject(Subject.builder()
                        .longName(command.longName())
                        .shortName(command.shortName())
                        .build())
                .teacher(teacherRepository.findByToken(command.teacher()).orElseThrow(() -> new IllegalArgumentException(String.format("Teacher with Token %s can not be found!", command.teacher()))))
                ._class(classRepository.getClassByToken(command._class()).orElseThrow(() -> new IllegalArgumentException(String.format("Class with Token %s can not be found!", command._class()))))
                .build();

        log.trace("Mapped command {} to lesson object {}", command, lesson);
        return lessonRepository.save(lesson);
    }
}

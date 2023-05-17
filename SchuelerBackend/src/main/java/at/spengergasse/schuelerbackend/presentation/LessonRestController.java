package at.spengergasse.schuelerbackend.presentation;

import at.spengergasse.schuelerbackend.domain.Exam;
import at.spengergasse.schuelerbackend.domain.Lesson;
import at.spengergasse.schuelerbackend.service.LessonService;
import at.spengergasse.schuelerbackend.service.dto.LessonDto;
import at.spengergasse.schuelerbackend.service.dto.command.MutateLessonCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor

@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173/"})
@RestController
@RequestMapping(LessonRestController.BASE_URL)
public class LessonRestController {

    private final LessonService lessonService;

    public static final String BASE_URL = "/api/lesson";
    public static final String PATH_INDEX = "/";
    public static final String PATH_VAR_ID = "/{id}";
    public static final String ROUTE_ID = BASE_URL + PATH_VAR_ID;

    @GetMapping({"", PATH_INDEX})
    public HttpEntity<List<Lesson>> getLessons(){
        log.debug("Incoming Http GET all lessons request received");
        List<Lesson> lessons = lessonService.getLessons();
        return (lessons.isEmpty())
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(lessons);
    }

    @GetMapping(PATH_VAR_ID)
    public HttpEntity<LessonDto> getLesson(@PathVariable String id){
        log.debug("Incoming Http GET lesson with id {} request received", id);
        return lessonService.getLesson(id)
                .map(LessonDto::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping({"", PATH_INDEX})
    public HttpEntity<LessonDto> createLesson(@RequestBody MutateLessonCommand command){
        log.debug("Incoming Http POST request with lesson command {} received", command);
        Lesson lesson = lessonService.createLesson(command);
        return ResponseEntity.created(createSelfLink(lesson)).body(new LessonDto(lesson));
    }

    @PutMapping(PATH_VAR_ID)
    public HttpEntity<LessonDto> replaceLesson(@PathVariable String id, @RequestBody MutateLessonCommand command){
        log.debug("Incoming Http PUT request for id {} with lesson command {} received", id, command);
        return ResponseEntity.ok(new LessonDto(lessonService.replaceLesson(id, command)));
    }

    @PatchMapping(PATH_VAR_ID)
    public HttpEntity<LessonDto> partiallyUpdate(@PathVariable String id, @RequestBody MutateLessonCommand command){
        log.debug("Incoming Http PATCH request for id {} with lesson command {} received", id, command);
        return ResponseEntity.ok(new LessonDto(lessonService.partiallyUpdateLesson(id, command)));
    }

    @DeleteMapping({"", PATH_INDEX})
    public HttpEntity<Void> deleteLessons(){
        log.debug("Incoming Http DELETE request to delete all lessons received");
        lessonService.deleteLessons();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(PATH_VAR_ID)
    public HttpEntity<Void> deleteLesson(@PathVariable String id){
        log.debug("Incoming Http DELETE request to delete lesson with token {}", id);
        lessonService.deleteLesson(id);
        return ResponseEntity.ok().build();
    }

    private URI createSelfLink(Lesson lesson) {
        URI selfLink = UriComponentsBuilder.fromPath(ROUTE_ID)
                .uriVariables(Map.of("token", lesson.getToken()))
                .build().toUri();
        log.debug("Created self link {} for lesson {}", selfLink, lesson);
        return selfLink;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.debug("Incoming request had the following errors: {}", errors);
        return errors;
    }
}

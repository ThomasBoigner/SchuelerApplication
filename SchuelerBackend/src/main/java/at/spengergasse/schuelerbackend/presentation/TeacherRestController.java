package at.spengergasse.schuelerbackend.presentation;

import at.spengergasse.schuelerbackend.domain.Exam;
import at.spengergasse.schuelerbackend.domain.Teacher;
import at.spengergasse.schuelerbackend.service.TeacherService;
import at.spengergasse.schuelerbackend.service.dto.ExamDto;
import at.spengergasse.schuelerbackend.service.dto.StudentDto;
import at.spengergasse.schuelerbackend.service.dto.TeacherDto;
import at.spengergasse.schuelerbackend.service.dto.command.MutateExamCommand;
import at.spengergasse.schuelerbackend.service.dto.command.MutateStudentCommand;
import at.spengergasse.schuelerbackend.service.dto.command.MutateTeacherCommand;
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
@RequestMapping(TeacherRestController.BASE_URL)
public class TeacherRestController {

    private final TeacherService teacherService;

    public static final String BASE_URL = "/api/teacher";
    public static final String PATH_INDEX = "/";
    public static final String PATH_VAR_ID = "/{id}";
    public static final String ROUTE_ID = BASE_URL + PATH_VAR_ID;

    @GetMapping({"", PATH_INDEX})
    public HttpEntity<List<TeacherDto>> getTeachers(){
        log.debug("Incoming Http GET all teachers request received");
        List<Teacher> teachers = teacherService.getTeachers();
        return (teachers.isEmpty())
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(teachers.stream().map(TeacherDto::new).toList());
    }

    @GetMapping(PATH_VAR_ID)
    public HttpEntity<TeacherDto> getTeacher(@PathVariable String id){
        log.debug("Incoming Http GET teacher with id {} request received", id);
        return teacherService.getTeacher(id)
                .map(TeacherDto::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping({"", PATH_INDEX})
    public HttpEntity<TeacherDto> createTeacher(@RequestBody MutateTeacherCommand command){
        log.debug("Incoming Http POST request with teacher command {} received", command);
        Teacher teacher = teacherService.createTeacher(command);
        return ResponseEntity.created(createSelfLink(teacher)).body(new TeacherDto(teacher));
    }

    @PutMapping(PATH_VAR_ID)
    public HttpEntity<TeacherDto> replaceTeacher(@PathVariable String id, @RequestBody MutateTeacherCommand command){
        log.debug("Incoming Http PUT request for id {} with teacher command {} received", id, command);
        return ResponseEntity.ok(new TeacherDto(teacherService.replaceTeacher(id, command)));
    }

    @PatchMapping(PATH_VAR_ID)
    public HttpEntity<TeacherDto> partiallyUpdateTeacher(@PathVariable String id, @RequestBody MutateTeacherCommand command){
        log.debug("Incoming Http PATCH request for id {} with teacher command {} received", id, command);
        return ResponseEntity.ok(new TeacherDto(teacherService.partiallyUpdateTeacher(id, command)));
    }

    @DeleteMapping({"", PATH_INDEX})
    public HttpEntity<Void> deleteTeachers(){
        log.debug("Incoming Http DELETE request to delete all students received");
        teacherService.deleteTeachers();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(PATH_VAR_ID)
    public HttpEntity<Void> deleteTeacher(@PathVariable String id){
        log.debug("Incoming Http DELETE request to delete student with id {} received", id);
        teacherService.deleteTeacher(id);
        return ResponseEntity.ok().build();
    }

    private URI createSelfLink(Teacher teacher) {
        URI selfLink = UriComponentsBuilder.fromPath(ROUTE_ID)
                .uriVariables(Map.of("token", teacher.getToken()))
                .build().toUri();
        log.debug("Created self link {} for Teacher {}", selfLink, teacher);
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

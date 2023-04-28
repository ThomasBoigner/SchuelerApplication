package at.spengergasse.schuelerbackend.presentation;

import at.spengergasse.schuelerbackend.domain.Student;
import at.spengergasse.schuelerbackend.service.StudentService;
import at.spengergasse.schuelerbackend.service.dto.StudentDto;
import at.spengergasse.schuelerbackend.service.dto.command.MutateStudentCommand;
import at.spengergasse.schuelerbackend.service.dto.command.UpdateStudentCommand;
import jakarta.validation.Valid;
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
@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173/"})
@RequestMapping(StudentRestController.BASE_URL)
public class StudentRestController {

    private final StudentService studentService;

    public static final String BASE_URL = "/api/student";
    public static final String PATH_INDEX = "/";
    public static final String PATH_VAR_ID = "/{id}";
    public static final String ROUTE_ID = BASE_URL + PATH_VAR_ID;

    @GetMapping({"", PATH_INDEX})
    public HttpEntity<List<StudentDto>> getStudents(){
        log.debug("Incoming Http GET all students request received");
        List<Student> students = studentService.getStudents();
        return (students.isEmpty())
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(students.stream().map(StudentDto::new).toList());
    }

    @GetMapping(PATH_VAR_ID)
    public HttpEntity<StudentDto> getStudent(@PathVariable String id){
        log.debug("Incoming Http GET student with id {} request received", id);
        return studentService.getStudent(id)
                .map(StudentDto::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping({PATH_INDEX, ""})
    public HttpEntity<StudentDto> createStudent(@RequestBody @Valid MutateStudentCommand command){
        log.debug("Incoming Http POST request with student command {} received", command);
        Student student = studentService.createStudent(command);
        return ResponseEntity.created(createSelfLink(student)).body(new StudentDto(student));
    }

    @PutMapping(PATH_VAR_ID)
    public HttpEntity<StudentDto> replaceStudent(@PathVariable String id, @RequestBody @Valid MutateStudentCommand command){
        log.debug("Incoming Http PUT request for id {} with student command {} received", id, command);
        return ResponseEntity.ok(new StudentDto(studentService.replaceStudent(id, command)));
    }

    @PatchMapping(PATH_VAR_ID)
    public HttpEntity<StudentDto> partiallyUpdateStudent(@PathVariable String id, @RequestBody @Valid UpdateStudentCommand command){
        log.debug("Incoming Http PATCH request for id {} with student command {} received", id, command);
        return ResponseEntity.ok(new StudentDto(studentService.partiallyUpdateStudent(id, command)));
    }

    @DeleteMapping({"", PATH_INDEX})
    public HttpEntity<Void> deleteStudents(){
        log.debug("Incoming Http DELETE request to delete all students received");
        studentService.deleteStudents();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(PATH_VAR_ID)
    public HttpEntity<Void> deleteStudent(@PathVariable String id){
        log.debug("Incoming Http DELETE request to delete student with id {} received", id);
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    private URI createSelfLink(Student student) {
        URI selfLink = UriComponentsBuilder.fromPath(ROUTE_ID)
                .uriVariables(Map.of("token", student.getToken()))
                .build().toUri();
        log.debug("Created self link {} for class {}", selfLink, student);
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

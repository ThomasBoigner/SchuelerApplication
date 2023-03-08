package at.spengergasse.schuelerbackend.presentation;

import at.spengergasse.schuelerbackend.domain.Student;
import at.spengergasse.schuelerbackend.service.StudentService;
import at.spengergasse.schuelerbackend.service.dto.StudentDto;
import at.spengergasse.schuelerbackend.service.dto.command.MutateStudentCommand;
import at.spengergasse.schuelerbackend.service.dto.command.UpdateStudentCommand;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequestMapping(StudentRestController.BASE_URL)
public class StudentRestController {

    private final StudentService studentService;

    public static final String BASE_URL = "/api/student";
    public static final String PATH_INDEX = "/";
    public static final String PATH_VAR_ID = "/{id}";
    public static final String ROUTE_ID = BASE_URL + PATH_VAR_ID;

    @GetMapping({"", PATH_INDEX})
    public HttpEntity<List<StudentDto>> getStudents(){
        List<Student> students = studentService.getStudents();
        return (students.isEmpty())
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(students.stream().map(StudentDto::new).toList());
    }

    @GetMapping(PATH_VAR_ID)
    public HttpEntity<StudentDto> getStudent(@PathVariable String id){
        return studentService.getStudent(id)
                .map(StudentDto::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping({PATH_INDEX, ""})
    public HttpEntity<StudentDto> createStudent(@RequestBody MutateStudentCommand command){
        Student student = studentService.createStudent(command);
        return ResponseEntity.created(createSelfLink(student)).body(new StudentDto(student));
    }

    @PutMapping(PATH_VAR_ID)
    public HttpEntity<StudentDto> replaceStudent(@PathVariable String id, @RequestBody MutateStudentCommand command){
        return ResponseEntity.ok(new StudentDto(studentService.replaceStudent(id, command)));
    }

    @PatchMapping(PATH_VAR_ID)
    public HttpEntity<StudentDto> partiallyUpdateStudent(@PathVariable String id, @RequestBody UpdateStudentCommand command){
        return ResponseEntity.ok(new StudentDto(studentService.partiallyUpdateStudent(id, command)));
    }

    @DeleteMapping(PATH_INDEX)
    public HttpEntity<Void> deleteStudents(){
        studentService.deleteStudents();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(PATH_VAR_ID)
    public HttpEntity<Void> deleteStudent(@PathVariable String id){
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    private URI createSelfLink(Student student) {
        return UriComponentsBuilder.fromPath(ROUTE_ID)
                .uriVariables(Map.of("token", student.getToken()))
                .build().toUri();
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
        return errors;
    }
}

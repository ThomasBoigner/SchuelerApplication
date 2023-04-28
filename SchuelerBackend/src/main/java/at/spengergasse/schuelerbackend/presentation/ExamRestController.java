package at.spengergasse.schuelerbackend.presentation;

import at.spengergasse.schuelerbackend.domain.Exam;
import at.spengergasse.schuelerbackend.domain.Student;
import at.spengergasse.schuelerbackend.service.ExamService;
import at.spengergasse.schuelerbackend.service.dto.ExamDto;
import at.spengergasse.schuelerbackend.service.dto.command.MutateExamCommand;
import at.spengergasse.schuelerbackend.service.dto.command.UpdateExamCommand;
import jakarta.validation.Valid;
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

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173/"})
@RestController
@RequestMapping(ExamRestController.BASE_URL)
public class ExamRestController {

    private final ExamService examService;

    public static final String BASE_URL = "/api/exam";
    public static final String PATH_INDEX = "/";
    public static final String PATH_VAR_ID = "/{id}";
    public static final String ROUTE_ID = BASE_URL + PATH_VAR_ID;

    @GetMapping({"", PATH_INDEX})
    public HttpEntity<List<ExamDto>> getExams(){
        List<Exam> exams = examService.getExams();
        return (exams.isEmpty())
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(exams.stream().map(ExamDto::new).toList());
    }

    @GetMapping(PATH_VAR_ID)
    public HttpEntity<ExamDto> getExam(@PathVariable String id){
        return examService.getExam(id)
                .map(ExamDto::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping({"", PATH_INDEX})
    public HttpEntity<ExamDto> createExam(@RequestBody @Valid MutateExamCommand command){
        Exam exam = examService.createExam(command);
        return ResponseEntity.created(createSelfLink(exam)).body(new ExamDto(exam));
    }

    @PutMapping(PATH_VAR_ID)
    public HttpEntity<ExamDto> replaceExam(@PathVariable String id, @RequestBody @Valid MutateExamCommand command){
        return ResponseEntity.ok(new ExamDto(examService.replaceExam(id, command)));
    }

    @PatchMapping(PATH_VAR_ID)
    public HttpEntity<ExamDto> partiallyUpdateExam(@PathVariable String id, @RequestBody @Valid UpdateExamCommand command){
        return ResponseEntity.ok(new ExamDto(examService.partiallyUpdateExam(id, command)));
    }

    @DeleteMapping({"", PATH_INDEX})
    public HttpEntity<Void> deleteExams(){
        examService.deleteExams();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(PATH_VAR_ID)
    public HttpEntity<Void> deleteExam(@PathVariable String id){
        examService.deleteExam(id);
        return ResponseEntity.ok().build();
    }

    private URI createSelfLink(Exam exam) {
        return UriComponentsBuilder.fromPath(ROUTE_ID)
                .uriVariables(Map.of("token", exam.getToken()))
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

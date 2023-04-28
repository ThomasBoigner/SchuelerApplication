package at.spengergasse.schuelerbackend.presentation;

import at.spengergasse.schuelerbackend.domain.Grade;
import at.spengergasse.schuelerbackend.service.GradeService;
import at.spengergasse.schuelerbackend.service.dto.GradeDto;
import at.spengergasse.schuelerbackend.service.dto.command.MutateGradeCommand;
import at.spengergasse.schuelerbackend.service.dto.command.UpdateGradeCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor

@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173/"})
@RestController
@RequestMapping(GradeRestController.BASE_URL)
public class GradeRestController {
    private final GradeService gradeService;

    public static final String BASE_URL = "/api/grade";
    public static final String PATH_INDEX = "/";
    public static final String PATH_VAR_ID = "/{id}";
    public static final String ROUTE_ID = BASE_URL + PATH_VAR_ID;

    @GetMapping({PATH_INDEX, ""})
    public HttpEntity<List<GradeDto>> getGrades(){
        log.debug("Incoming HTTP GET all grades request received");
        List<Grade> grades = gradeService.getGrades();
        return (grades.isEmpty())
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(grades.stream().map(GradeDto::new).toList());
    }

    @GetMapping(PATH_VAR_ID)
    public HttpEntity<GradeDto> getGrade(@PathVariable String id){
        log.debug("Incoming HTTP GET grade with id {} request received", id);
        return gradeService.getGrade(id)
                .map(GradeDto::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping({PATH_INDEX, ""})
    public HttpEntity<GradeDto> createGrade(@RequestBody MutateGradeCommand command){
        log.debug("Incoming HTTP POST request with grade command {} received", command);
        Grade grade = gradeService.createGrade(command);
        return ResponseEntity.created(createSelfLink(grade)).body(new GradeDto(grade));
    }

    @PutMapping(PATH_VAR_ID)
    public HttpEntity<GradeDto> replaceGrade(@PathVariable String id, @RequestBody MutateGradeCommand command){
        log.debug("Incoming HTTP PUT request for id {} with grade command {} received", id, command);
        return ResponseEntity.ok(new GradeDto(gradeService.replaceGrade(id, command)));
    }

    @PatchMapping(PATH_VAR_ID)
    public HttpEntity<GradeDto> updateGrade(@PathVariable String id, @RequestBody UpdateGradeCommand command){
        log.debug("Incoming HTTP PATCH request for id {} with grade command {} received", id, command);
        return ResponseEntity.ok(new GradeDto(gradeService.partiallyUpdateGrade(id, command)));
    }

    @DeleteMapping({"", PATH_INDEX})
    public HttpEntity<Void> deleteGrades(){
        log.debug("Incoming HTTP DELETE request to delete all grades received");
        gradeService.deleteGrades();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(PATH_VAR_ID)
    public HttpEntity<Void> deleteGrade(@PathVariable String id){
        log.debug("Incoming HTTP DELETE request to delete grade with id {} received", id);
        gradeService.deleteGrade(id);
        return ResponseEntity.ok().build();
    }

    private URI createSelfLink(Grade grade){
        URI selfLink = UriComponentsBuilder.fromPath(ROUTE_ID)
                .uriVariables(Map.of("token", grade.getToken()))
                .build()
                .toUri();
        log.debug("Created self link {} for grade {}", selfLink, grade);
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

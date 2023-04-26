package at.spengergasse.schuelerbackend.presentation;

import at.spengergasse.schuelerbackend.domain.Class;
import at.spengergasse.schuelerbackend.service.ClassService;
import at.spengergasse.schuelerbackend.service.dto.ClassDto;
import at.spengergasse.schuelerbackend.service.dto.command.MutateClassCommand;
import at.spengergasse.schuelerbackend.service.dto.command.UpdateClassCommand;
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
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173/"})
@RestController
@RequestMapping(ClassRestController.BASE_URL)
public class ClassRestController {

    private final ClassService classService;

    public static final String BASE_URL = "/api/class";
    public static final String PATH_INDEX = "/";
    public static final String PATH_VAR_ID = "/{id}";
    public static final String ROUTE_ID = BASE_URL + PATH_VAR_ID;

    @CrossOrigin
    @GetMapping({"", PATH_INDEX})
    public HttpEntity<List<ClassDto>> getClasses(){
        log.debug("Incoming Http GET all classes request received");
        List<Class> classes = classService.getClasses();
        return (classes.isEmpty())
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(classes.stream().map(ClassDto::new).toList());
    }

    @GetMapping(PATH_VAR_ID)
    public HttpEntity<ClassDto> getClass(@PathVariable String id){
        log.debug("Incoming Http GET class with id {} request received", id);
        return classService.getClass(id)
                .map(ClassDto::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping({PATH_INDEX, ""})
    public HttpEntity<ClassDto> createClass(@RequestBody @Valid MutateClassCommand command){
        log.debug("Incoming Http POST request with class command {} received", command);
        Class _class = classService.createClass(command);
        return ResponseEntity.created(createSelfLink(_class)).body(new ClassDto(_class));
    }

    @PutMapping(PATH_VAR_ID)
    public HttpEntity<ClassDto> replaceClass(@PathVariable String id, @RequestBody @Valid MutateClassCommand command){
        log.debug("Incoming Http PUT request with class command {} for id {} received", command, id);
        return ResponseEntity.ok(new ClassDto(classService.replaceClass(id, command)));
    }

    @PatchMapping(PATH_VAR_ID)
    public HttpEntity<ClassDto> partiallyUpdateClass(@PathVariable String id, @RequestBody @Valid UpdateClassCommand command){
        log.debug("Incoming Http PATCH request with class command {} for id {} received", command, id);
        return ResponseEntity.ok(new ClassDto(classService.partiallyUpdateStudent(id, command)));
    }

    @DeleteMapping({"", PATH_INDEX})
    public HttpEntity<Void> deleteClasses(){
        log.debug("Incoming Http DELETE request to delete all classes received");
        classService.deleteClasses();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(PATH_VAR_ID)
    public HttpEntity<Void> deleteClass(@PathVariable String id){
        log.debug("Incoming Http DELETE request to delete class with id {} received", id);
        classService.deleteClass(id);
        return ResponseEntity.ok().build();
    }

    private URI createSelfLink(Class _class) {
         URI selfURI = UriComponentsBuilder.fromPath(ROUTE_ID)
                .uriVariables(Map.of("token", _class.getToken()))
                .build().toUri();
         log.debug("Created selfURI {} for class {}", selfURI, _class);
         return selfURI;
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

package at.spengergasse.schuelerbackend.presentation;

import at.spengergasse.schuelerbackend.domain.Class;
import at.spengergasse.schuelerbackend.service.ClassService;
import at.spengergasse.schuelerbackend.service.dto.ClassDto;
import at.spengergasse.schuelerbackend.service.dto.command.MutateClassCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor

@RestController
@RequestMapping(ClassRestController.BASE_URL)
public class ClassRestController {

    private final ClassService classService;

    public static final String BASE_URL = "/api/class";
    public static final String PATH_INDEX = "/";

    @GetMapping({"", PATH_INDEX})
    public HttpEntity<List<ClassDto>> getClasses(){
        List<Class> classes = classService.getClasses();
        return (classes.isEmpty())
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(classes.stream().map(ClassDto::new).toList());
    }

    @PostMapping({PATH_INDEX, ""})
    public HttpEntity<ClassDto> createClass(@RequestBody MutateClassCommand command){
        Class _class = classService.createClass(command);
        return ResponseEntity.ok(new ClassDto(_class));
    }
}

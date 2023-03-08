package at.spengergasse.schuelerbackend.service;

import at.spengergasse.schuelerbackend.domain.Exam;
import at.spengergasse.schuelerbackend.domain.Grade;
import at.spengergasse.schuelerbackend.foundation.TemporalValueFactory;
import at.spengergasse.schuelerbackend.persistence.ExamRepository;
import at.spengergasse.schuelerbackend.persistence.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor

@Service
public class ExamService {
    private final ExamRepository examRepository;
    private final GradeRepository gradeRepository;


    private final TemporalValueFactory temporalValueFactory;
    private final TokenService tokenService;

    public List<Exam> getExams(){
        return examRepository.findAll();
    }

    public Optional<Exam> getExam(String id){
        return examRepository.getByToken(id);
    }

}

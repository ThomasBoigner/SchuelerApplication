package at.spengergasse.schuelerbackend.service.dto.command;


public record MutateGradeCommand(String student, String lesson, Integer gradeValue) { }

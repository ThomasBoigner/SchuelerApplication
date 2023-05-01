package at.spengergasse.schuelerbackend.service.dto.command;

public record MutateStudentCommand (String firstname, String lastname, String email, String _class, Boolean conferenceDecision){ }

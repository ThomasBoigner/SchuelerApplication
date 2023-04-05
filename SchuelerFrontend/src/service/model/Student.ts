import type { Class } from "./Class";
import type { Grade } from "./Grade";

export interface Student{
    firstname: string,
    lastname: string,
    email: string,
    class: Class,
    conferenceDecision: boolean,
    gradeValues: Grade[],
    token: string,
    creationTS: Date
}
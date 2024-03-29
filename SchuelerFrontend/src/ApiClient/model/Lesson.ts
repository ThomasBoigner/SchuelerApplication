import type { Class } from "./Class";
import type { Subject } from "./Subject";
import type { Teacher } from "./Teacher";

export type Lesson = {
    subject: Subject,
    class: Class,
    teacher: Teacher,
    token: string,
    creationTS: Date
}
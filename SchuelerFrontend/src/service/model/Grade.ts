import type { Lesson } from "./Lesson";

export interface Grade{
    lesson: Lesson,
    gradeValue: number,
    token: string,
    creationTS: Date
}
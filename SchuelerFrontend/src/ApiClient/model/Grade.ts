import type { Lesson } from "./Lesson";

export type Grade = {
    lesson: Lesson,
    gradeValue: number,
    token: string,
    creationTS: Date
}
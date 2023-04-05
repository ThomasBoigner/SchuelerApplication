import type { Grade } from "./Grade"

export interface Exam{
    date: Date,
    examResult: number,
    newGradeValue: number,
    grade: Grade,
    token: string,
    creationTS: Date
}
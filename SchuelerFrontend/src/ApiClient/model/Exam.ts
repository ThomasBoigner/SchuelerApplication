import type { Grade } from "./Grade"

export type Exam = {
    date: Date,
    examResult: number,
    newGradeValue: number,
    grade: Grade,
    token: string,
    creationTS: Date
}
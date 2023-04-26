import axios from "axios";
import type { Exam } from "./model/Exam";
import type { ExamForm } from "./forms/ExamForm";

export function getExam(token: string): Promise<Exam | void>{
    console.group(`Sending a HTTP GET request to /exam/${token}`)

    return axios.get(`/exam/${token}`)
    .then(function (response){
        console.debug(`Got response: ${JSON.stringify(response)}`)

        let exam: Exam = response.data as Exam

        console.debug(`Mapped response data to object ${JSON.stringify(exam)}`)
        console.groupEnd()
        return exam;
    })
    .catch(function(error){
        console.error(`Got error: ${error}`)
        console.groupEnd()
    })
}

export function getExams(): Promise<Exam[] | void>{
    console.group(`Sending a HTTP GET request to /exam`)

    return axios.get(`/exam`)
    .then(function (response){
        console.debug(`Got response: ${JSON.stringify(response)}`)

        let exams: Exam[] = new Array<Exam>
        if(response.data){
            response.data.forEach((exam: any) => {
                exams.push(exam as Exam)
            })
        }

        console.debug(`Mapped response data to List of exams ${JSON.stringify(exams)}`)
        console.groupEnd()
        return exams
    })
    .catch(function(error){
        console.error(`Got error: ${error}`)
        console.groupEnd()
    })
}

export function createExam(form: ExamForm): Promise<Exam | void>{
    console.group(`Sending a HTTP POST request to /exam with body ${JSON.stringify(form)}`)

    return axios.post('/exam', form)
    .then((response) => {
        console.debug(`Got response: ${JSON.stringify(response)}`)

        let exam: Exam = response.data as Exam 
        
        console.debug(`Mapped response data to object ${JSON.stringify(exam)}`)
        console.groupEnd()
        return exam
    })
    .catch((error) => {
        console.error(`Got error: ${JSON.stringify(error)}`)
        console.groupEnd()
    })
}

export function replaceExam(token: string, form: ExamForm): Promise<Exam | void>{
    console.group(`Sending a HTTP PUT request to /exam/${token} with body ${JSON.stringify(form)}`)

    return axios.put(`/exam/${token}`, form)
    .then((response) => {
        console.debug(`Got response: ${JSON.stringify(response)}`)

        let exam: Exam = response.data as Exam
        
        console.debug(`Mapped response data to object ${JSON.stringify(exam)}`)
        console.groupEnd()
        return exam
    })
    .catch((error) => {
        console.error(`Got error: ${JSON.stringify(error)}`)
        console.groupEnd()
    })
}

export function updateExam(token: string, form: ExamForm): Promise<Exam | void>{
    console.group(`Sending a HTTP PATCH request to /exam/${token} with body ${JSON.stringify(form)}`)

    return axios.patch(`/exam/${token}`, form)
    .then((response) => {
        console.debug(`Got response: ${JSON.stringify(response)}`)

        let exam : Exam = response.data as Exam 
        
        console.debug(`Mapped response data to object ${JSON.stringify(exam)}`)
        console.groupEnd()
        return exam 
    })
    .catch((error) => {
        console.error(`Got error: ${JSON.stringify(error)}`)
        console.groupEnd()
    })
}

export function deleteExam(token:string): Promise<void>{
    console.group(`Sending a HTTP DELETE request to /exam/${token}`)

    return axios.delete(`/exam/${token}`)
    .then((response) => {
        console.debug(`Got response: ${JSON.stringify(response)}`)
        console.groupEnd()
    })
    .catch((error) => {
        console.error(`Got error: ${JSON.stringify(error)}`)
        console.groupEnd()
    })
}

export function deleteExams(): Promise<void>{
    console.group(`Sending a HTTP DELETE request to /exam`)

    return axios.delete('/exam')
    .then((response) => {
        console.debug(`Got response: ${JSON.stringify(response)}`)
        console.groupEnd()
    })
    .catch((error) => {
        console.error(`Got error: ${JSON.stringify(error)}`)
        console.groupEnd()
    })
}
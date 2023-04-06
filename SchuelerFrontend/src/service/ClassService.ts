import type { AxiosResponse } from "axios";
import axios from "axios";
import type { ClassForm } from "./forms/ClassForm";
import type { Class } from "./model/Class";

export function getClass(token:string): Promise<Class | void>{

    return axios.get('/class/' + token)
    .then(function (response){
        return response.data as Class
    })
    .catch(function (error) {
        console.error(error)
    })
}

export function getClasses(): Promise<Class[] | void>{
    return axios.get('/class')
    .then(function (response){
        const classes: Class[] = new Array<Class> 
        response.data.forEach((_class: any) => {
            classes.push(_class as Class)
        })
        return classes
    })
    .catch(function (error) {
        console.error(error)
    })
}

export function createClass(form: ClassForm): Promise<Class | void>{
    console.log(form)
    return axios.post('/class', form)
    .then(function (response){
        return response.data as Class
    })
    .catch(function (error){
        console.error(error)
    })
}

export function replaceClass(token: string, form: ClassForm): Promise<Class | void>{
    return axios.put('/class/' + token, form)
    .then(function (response){
        return response.data as Class
    })
    .catch(function (error){
        console.error(error)
    })
}

export function updateClass(token: string, form: ClassForm): Promise<Class | void>{
    return axios.patch('/class/' + token, form)
    .then(function (response){
        return response.data as Class
    })
    .catch(function (error){
        console.error(error)
    })
}

export function deleteClass(token:string): Promise<void>{
    return axios.delete('/class/' + token)
    .then(function (response){
        return
    })
    .catch(function (error){
        console.error(error)
    })
}

export function deleteClasses(): Promise<void>{
    return axios.delete('/class')
    .then(function (response){
        return
    })
    .catch(function (error){
        console.error(error)
    })
}
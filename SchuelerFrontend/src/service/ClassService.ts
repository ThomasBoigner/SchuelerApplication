import type { AxiosResponse } from "axios";
import axios from "axios";
import type { ClassForm } from "./forms/ClassForm";
import type { Class } from "./model/Class";

export async function getClass(token:string): Promise<Class | void>{

    return await axios.get('/class/' + token)
    .then(function (response){
        return response.data as Class
    })
    .catch(function (error) {
        console.log(error)
    })
}

export async function getClasses(): Promise<Class[] | void>{
    return await axios.get('/class')
    .then(function (response){
        const classes: Class[] = new Array<Class> 
        for(const _class of response.data){
            classes.push(_class as Class)
        }
        return classes
    })
    .catch(function (error) {
        console.log(error)
    })
}

export async function createClass(form: ClassForm): Promise<Class | void>{
    return await axios.post('/class', form)
    .then(function (response){
        return response.data as Class
    })
    .catch(function (error){
        console.log(error)
    })
}

export async function deleteClass(token:string){
    await axios.delete('/class/' + token)
    .catch(function (error){
        console.log(error)
    })
}

export async function deleteClasses(){
    await axios.delete('/class')
    .catch(function (error){
        console.log(error)
    })
}
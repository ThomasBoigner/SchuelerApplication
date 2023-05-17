import axios from "axios";
import type { ClassForm } from "./forms/ClassForm";
import type { Class } from "./model/Class";

export function getClass(token:string): Promise<Class | void>{
    console.group(`Sending a HTTP GET request to /class/${token}`)

    return axios.get(`/class/${token}`)
    .then((response) => {
        console.debug(`Got response: ${JSON.stringify(response)}`)

        let _class: Class = response.data as Class 
        
        console.debug(`Mapped response data to object ${JSON.stringify(_class)}`)
        console.groupEnd()
        return _class 
    })
    .catch((error) => {
        console.error(`Got error: ${JSON.stringify(error)}`)
        console.groupEnd()
    })
}

export function getClasses(): Promise<Class[] | void>{
    console.group(`Sending a HTTP GET request to /class`)

    return axios.get('/class')
    .then((response) => {
        console.debug(`Got response: ${JSON.stringify(response)}`)

        let classes: Class[] = new Array<Class>
        if(response.data)
        {
            response.data.forEach((_class: any) => {
                classes.push(_class as Class)
            })
        }

        console.debug(`Mapped response data to List of classes ${JSON.stringify(classes)}`)
        console.groupEnd()
        return classes
    })
    .catch((error) => {
        console.error(`Got error: ${JSON.stringify(error)}`)
        console.groupEnd()
    })
}

export function createClass(form: ClassForm): Promise<Class | void>{
    console.group(`Sending a HTTP POST request to /class with body ${JSON.stringify(form)}`)

    return axios.post('/class', form)
    .then((response) => {
        console.debug(`Got response: ${JSON.stringify(response)}`)

        let _class: Class = response.data as Class 
        
        console.debug(`Mapped response data to object ${JSON.stringify(_class)}`)
        console.groupEnd()
        return _class 
    })
    .catch((error) => {
        console.error(`Got error: ${JSON.stringify(error)}`)
        console.groupEnd()
    })
}

export function replaceClass(token: string, form: ClassForm): Promise<Class | void>{
    console.group(`Sending a HTTP PUT request to /class/${token} with body ${JSON.stringify(form)}`)

    return axios.put(`/class/${token}`, form)
    .then((response) => {
        console.debug(`Got response: ${JSON.stringify(response)}`)

        let _class: Class = response.data as Class 
        
        console.debug(`Mapped response data to object ${JSON.stringify(_class)}`)
        console.groupEnd()
        return _class
    })
    .catch((error) => {
        console.error(`Got error: ${JSON.stringify(error)}`)
        console.groupEnd()
    })
}

export function updateClass(token: string, form: ClassForm): Promise<Class | void>{
    console.group(`Sending a HTTP PATCH request to /class/${token} with body ${JSON.stringify(form)}`)

    return axios.patch(`/class/${token}`, form)
    .then((response) => {
        console.debug(`Got response: ${JSON.stringify(response)}`)

        let _class: Class = response.data as Class 
        
        console.debug(`Mapped response data to object ${JSON.stringify(_class)}`)
        console.groupEnd()
        return _class
    })
    .catch((error) => {
        console.error(`Got error: ${JSON.stringify(error)}`)
        console.groupEnd()
    })
}

export function deleteClass(token:string): Promise<void>{
    console.group(`Sending a HTTP DELETE request to /class/${token}`)

    return axios.delete(`/class/${token}`)
    .then((response) => {
        console.debug(`Got response: ${JSON.stringify(response)}`)
        console.groupEnd()
    })
    .catch((error) => {
        console.error(`Got error: ${JSON.stringify(error)}`)
        console.groupEnd()
    })
}

export function deleteClasses(): Promise<void>{
    console.group(`Sending a HTTP DELETE request to /class`)

    return axios.delete('/class')
    .then((response) => {
        console.debug(`Got response: ${JSON.stringify(response)}`)
        console.groupEnd()
    })
    .catch((error) => {
        console.error(`Got error: ${JSON.stringify(error)}`)
        console.groupEnd()
    })
}
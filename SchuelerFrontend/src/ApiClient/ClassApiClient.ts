import axios, { Axios, AxiosError } from "axios";
import type { Class } from "./model/Class";
import { useClassStore } from "./ClassStore";
import type { AxiosResponse } from "axios";

export function getClass(token:string): Promise<Class | void>{
    const classStore = useClassStore();
    console.group(`Sending a HTTP GET request to /class/${token}`)

    return axios.get(`/class/${token}`)
    .then((response: AxiosResponse) => {
        console.debug(`Got response: ${JSON.stringify(response)}`)

        let _class: Class = response.data as Class 
        
        console.debug(`Mapped response data to object ${JSON.stringify(_class)}`)
        console.groupEnd()

        classStore._class = _class;
        return _class 
    })
    .catch((error: AxiosError) => {
        if(error.response){
            console.error(`Http request failed with code ${error.response.status}, data ${JSON.stringify(error.response.data)} and headers ${error.response.headers}`)
            classStore.classError = error.message;
        } else if(error.request){
            console.log(`Http request received no response. Request details: ${error.request}`)
            classStore.classError = "Server did not respond"
        } else {
            console.error(`Http request failed with message ${error.message}`)
            classStore.classError = "Request failed!"
        }
        console.log(`config of the failed request: ${error.status}`)
        console.groupEnd()
    })
}

export function getClasses(): Promise<Class[] | void>{
    const classStore = useClassStore();
    console.group(`Sending a HTTP GET request to /class`)

    return axios.get('/class')
    .then((response: AxiosResponse) => {
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

        classStore.classes = classes
        return classes
    })
    .catch((error: AxiosError) => {
        if(error.response){
            console.error(`Http request failed with code ${error.response.status}, data ${JSON.stringify(error.response.data)} and headers ${error.response.headers}`)
            classStore.classError = error.message;
        } else if(error.request){
            console.log(`Http request received no response. Request details: ${error.request}`)
            classStore.classError = "Server did not respond"
        } else {
            console.error(`Http request failed with message ${error.message}`)
            classStore.classError = "Request failed!"
        }
        console.log(`config of the failed request: ${error.status}`)
        console.groupEnd()
    })
}

export function createClass(): Promise<Class | void>{
    const classStore = useClassStore();
    console.group(`Sending a HTTP POST request to /class with body ${JSON.stringify(classStore.classForm)}`)

    return axios.post('/class', classStore.classForm)
    .then((response: AxiosResponse) => {
        console.debug(`Got response: ${JSON.stringify(response)}`)

        let _class: Class = response.data as Class 
        
        console.debug(`Mapped response data to object ${JSON.stringify(_class)}`)
        console.groupEnd()

        classStore.classError = ""
        getClasses()
        return _class 
    })
    .catch((error: AxiosError) => {
        if(error.response){
            console.error(`Http request failed with code ${error.response.status}, data ${JSON.stringify(error.response.data)} and headers ${error.response.headers}`)
            classStore.classError = error.message;
        } else if(error.request){
            console.log(`Http request received no response. Request details: ${error.request}`)
            classStore.classError = "Server did not respond"
        } else {
            console.error(`Http request failed with message ${error.message}`)
            classStore.classError = "Request failed!"
        }
        console.log(`config of the failed request: ${error.status}`)
        console.groupEnd()
    })
}

export function replaceClass(token: string): Promise<Class | void>{
    const classStore = useClassStore();
    console.group(`Sending a HTTP PUT request to /class/${token} with body ${JSON.stringify(classStore.classForm)}`)

    return axios.put(`/class/${token}`, classStore.classForm)
    .then((response) => {
        console.debug(`Got response: ${JSON.stringify(response)}`)

        let _class: Class = response.data as Class 
        
        console.debug(`Mapped response data to object ${JSON.stringify(_class)}`)
        console.groupEnd()

        classStore.classError = ""
        getClasses()
        return _class
    })
    .catch((error) => {
        if(error.response){
            console.error(`Http request failed with code ${error.response.status}, data ${JSON.stringify(error.response.data)} and headers ${error.response.headers}`)
            classStore.classError = error.message;
        } else if(error.request){
            console.log(`Http request received no response. Request details: ${error.request}`)
            classStore.classError = "Server did not respond"
        } else {
            console.error(`Http request failed with message ${error.message}`)
            classStore.classError = "Request failed!"
        }
        console.log(`config of the failed request: ${error.status}`)
        console.groupEnd()
    })
}

export function updateClass(token: string): Promise<Class | void>{
    const classStore = useClassStore();
    console.group(`Sending a HTTP PATCH request to /class/${token} with body ${JSON.stringify(classStore.classForm)}`)

    return axios.patch(`/class/${token}`, classStore.classForm)
    .then((response) => {
        console.debug(`Got response: ${JSON.stringify(response)}`)

        let _class: Class = response.data as Class 
        
        console.debug(`Mapped response data to object ${JSON.stringify(_class)}`)
        console.groupEnd()

        classStore.classError = ""
        getClasses()
        return _class
    })
    .catch((error) => {
        if(error.response){
            console.error(`Http request failed with code ${error.response.status}, data ${JSON.stringify(error.response.data)} and headers ${error.response.headers}`)
            classStore.classError = error.message;
        } else if(error.request){
            console.log(`Http request received no response. Request details: ${error.request}`)
            classStore.classError = "Server did not respond"
        } else {
            console.error(`Http request failed with message ${error.message}`)
            classStore.classError = "Request failed!"
        }
        console.log(`config of the failed request: ${error.status}`)
        console.groupEnd()
    })
}

export function deleteClass(token:string): Promise<void>{
    const classStore = useClassStore();
    console.group(`Sending a HTTP DELETE request to /class/${token}`)

    return axios.delete(`/class/${token}`)
    .then((response) => {
        getClasses()
        console.debug(`Got response: ${JSON.stringify(response)}`)
        console.groupEnd()
    })
    .catch((error) => {
        if(error.response){
            console.error(`Http request failed with code ${error.response.status}, data ${JSON.stringify(error.response.data)} and headers ${error.response.headers}`)
            classStore.classError = error.message;
        } else if(error.request){
            console.log(`Http request received no response. Request details: ${error.request}`)
            classStore.classError = "Server did not respond"
        } else {
            console.error(`Http request failed with message ${error.message}`)
            classStore.classError = "Request failed!"
        }
        console.log(`config of the failed request: ${error.status}`)
        console.groupEnd()
    })
}

export function deleteClasses(): Promise<void>{
    const classStore = useClassStore();
    console.group(`Sending a HTTP DELETE request to /class`)

    return axios.delete('/class')
    .then((response) => {
        getClasses()
        console.debug(`Got response: ${JSON.stringify(response)}`)
        console.groupEnd()
    })
    .catch((error) => {
        if(error.response){
            console.error(`Http request failed with code ${error.response.status}, data ${JSON.stringify(error.response.data)} and headers ${error.response.headers}`)
            classStore.classError = error.message;
        } else if(error.request){
            console.log(`Http request received no response. Request details: ${error.request}`)
            classStore.classError = "Server did not respond"
        } else {
            console.error(`Http request failed with message ${error.message}`)
            classStore.classError = "Request failed!"
        }
        console.log(`config of the failed request: ${error.status}`)
        console.groupEnd()
    })
}
import axios from "axios";
import { describe, expect, it, test } from "vitest";
import { createClass, deleteClass, deleteClasses, getClass, getClasses, replaceClass, updateClass } from "../../src/service/ClassService"
import { Class } from "../../src/service/model/Class";

axios.defaults.baseURL = 'http://localhost:8080/api'

describe('ClassServiceTests', () => {
    //test('getClassWorksProperly', async () =>{
        //const _class = await getClass("Xb26qEwJ")
        //expect(_class?.name).equals("5AHIF")
        //expect(_class?.token).equals("Xb26qEwJ")
    //})

    //test('getClassesWorksProperly', async () =>{
        //const classes: Class[] = await getClasses() as Class[]
        ////expect(classes.length).equals(4)
        //expect(classes[0].name).equals("5AHIF")
    //})

    //test('createClassWorksProperly', async () => {
        //const _class = await createClass({name: "Domas Boghner"})
        //expect(_class?.name).equals("Domas Boghner")
    //})

    //test('replaceClassWorksProperly', async () => {
        //const _class = await replaceClass('rJGWzzwX', {name: "5FHIF"})
        //expect(_class?.name).equal("5FHIF")
    //})

    //test('updateClassWorksProperly', async () => {
        //const _class = await updateClass('HH6mcxVE', {name: "5GHIF"})
        //expect(_class?.name).equal("5GHIF")
    //})

    //test('deleteClassWorksProperly', async () => {
        //await deleteClass("Xb26qEwJ")
    //})

    //test('deleteClassesWorksProperly', async () => {
        //await deleteClasses()
    //})
})
import type { Class } from "./model/Class";

export function getClass(token:String): Class{
    return {name: "Test", token: "test", creationTS: new Date()};
}
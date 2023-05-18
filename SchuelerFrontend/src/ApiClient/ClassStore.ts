import { defineStore } from "pinia";
import { ref } from "vue";
import type { Class } from "./model/Class";
import type { ClassForm } from "./forms/ClassForm";

export const useClassStore = defineStore('classStore', () => {
    const _class = ref<Class>();
    const classes = ref<Class[]>();
    const classForm = ref<ClassForm>({name: ""});
    const classError = ref<String>();

    return { _class, classes, classForm, classError};
})
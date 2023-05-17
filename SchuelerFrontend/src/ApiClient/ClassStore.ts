import { defineStore } from "pinia";
import { ref } from "vue";
import type { Class } from "./model/Class";

export const useClassStore = defineStore('classStore', () => {
    const _class = ref<Class>();
    const classes = ref<Class>();
    const classError = ref<String>();

    return { _class, classes, classError };
})
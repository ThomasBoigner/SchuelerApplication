<script setup lang="ts">
import { deleteClass, getClasses, deleteClasses, createClass, updateClass } from '@/service/ClassService';
import type { ClassForm } from '@/service/forms/ClassForm';
import type { Class } from '@/service/model/Class';
import { onMounted, ref } from 'vue';

const classes = ref<Class[] | void>();
const form = ref<ClassForm>({name: ''});


onMounted(async () => {
    classes.value = await getClasses()
})

async function removeClass(token: string){
    deleteClass(token).then(() => refresh())
}
async function removeAllClasses(){
    deleteClasses().then(() => refresh())
}

async function refresh(){
    classes.value = await getClasses()
}

async function addClass(form: ClassForm){
    createClass(form).then(() => refresh())
}

async function updateClassFromList(token: string, form: ClassForm){
    updateClass(token, form).then(() => refresh())
}

</script>
<template>
    <h1>Classes</h1>
    <table>
        <tr v-for="_class in classes" :key="_class.token">
            <td>{{_class.name}}</td>
            <td><button @click="removeClass(_class.token)">Delete</button></td>
            <td><button @click="updateClassFromList(_class.token, form)">Update</button></td>
        </tr>
    </table>
    
    <input v-model="form.name"><br>
    <button @click="addClass(form)">Create Class</button><br>
    <button @click="refresh()">Refresh</button><br>
    <button @click="removeAllClasses()">Delete All</button>
</template>
<style scoped>

</style>
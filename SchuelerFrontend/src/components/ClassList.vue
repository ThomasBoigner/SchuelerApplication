<script setup lang="ts">
import { deleteClass, getClasses, deleteClasses, createClass, updateClass} from '@/ApiClient/ClassApiClient';
import { useClassStore } from '@/ApiClient/ClassStore';

const classStore = useClassStore();

</script>
<template>
    <h1>Classes</h1>
    {{classStore.classError}}
    <input v-model="classStore.classForm.name"><br>
    <button @click="createClass">Create Class</button><br>
    <button @click="getClasses()">Refresh</button><br>
    <button @click="deleteClasses()">Delete All</button>
    <table>
        <tr v-for="_class in classStore.classes" :key="_class.token">
            <td>{{_class.name}}</td>
            <td><button @click="deleteClass(_class.token)">Delete</button></td>
            <td><button @click="updateClass(_class.token)">Update</button></td>
        </tr>
    </table>
</template>
<style scoped>

</style>
import axios from 'axios'
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'

const app = createApp(App)
const pinia = createPinia()

axios.defaults.baseURL = 'http://localhost:8080/api'

app.use(router)
app.use(pinia)

app.mount('#app')

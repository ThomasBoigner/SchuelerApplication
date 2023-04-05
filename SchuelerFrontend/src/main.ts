import axios from 'axios'
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

const app = createApp(App)

axios.defaults.baseURL = 'http://localhost:8080/api'

app.use(router)

app.mount('#app')

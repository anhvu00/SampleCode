import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

import './assets/main.css'
import { useCounterStore } from '@/stores/counter'

const app = createApp(App)

app.use(createPinia())
app.use(router)

app.mount('#app')

// Expose a store to window during testing
if (window.Cypress && process.env.NODE_ENV === 'development') {
    const counterStore = useCounterStore();
    window.counterStore = counterStore;
}

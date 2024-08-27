import { createApp } from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
import store from './store'
// TODO: bootstrap vue3 import
import BootstrapVue3 from 'bootstrap-vue-3'
// TODO: bootstartp vue3 css import
import 'bootstrap-vue-3/dist/bootstrap-vue-3.css'


createApp(App)
// TODO: bootstrap vue3 lib 뷰에 넣기
.use(BootstrapVue3)
.use(store)
.use(router)
.mount('#app')
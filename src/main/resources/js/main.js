import { createApp } from 'vue'
import root from './pages/App.vue'
import axios from 'axios'
import VueAxios from 'vue-axios'
// Vuetify
import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import '@babel/polyfill'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import router from "./router/router";
import VCalendar from 'v-calendar';
import 'v-calendar/style.css';


const vuetify = createVuetify({
  components,
  directives,
  ssr: true,
})


const app = createApp(
  root,
  {
  data() {
    frontendData: frontendData
  }
})

app.use(router)
app.use(vuetify)
app.use(VCalendar, {})
app.use(VueAxios, axios)


app.mount('#app')


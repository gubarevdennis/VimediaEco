import {createRouter, createWebHistory} from 'vue-router'
import userList from "../components/userList.vue";
import facilityList from "../components/facilityList.vue";
import reportList from "../components/reportList.vue";
import calendarReport from "../components/calendarReport.vue";
import loginPage from "../pages/loginPage.vue";


const router = createRouter({
  routes: [
    {
      path: '/reports', component: reportList
    },
    {
      path: '/facilities', component: facilityList
    },
    {
      path: '/users', component: userList
    },
    {
      path: '/byUser', component: calendarReport
    },
    {
      path: '/login', component: loginPage
    }
  ],
  history: createWebHistory()
})

export default router;


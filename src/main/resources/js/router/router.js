import {createRouter, createWebHistory} from 'vue-router'
import userList from "../components/users/userList.vue";
import facilityList from "../components/facilities/facilityList.vue";
import reportList from "../components/reports/reportList.vue";
import calendarReport from "../components/calendar/calendarReport.vue";
import loginPage from "../pages/loginPage.vue";
import cabinet from "../components/personalCabinet/cabinet.vue";
import mainReport from "../components/mainReport/mainReport.vue";


const router = createRouter({
  routes: [
    {
      path: '/',
      redirect: '/reports'
    },
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
    },
    {
      path: '/cabinet', component: cabinet
    },
    {
      path: '/month', component: mainReport
    }
  ],
  history: createWebHistory()
})

export default router;


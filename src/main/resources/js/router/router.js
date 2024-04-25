import {createRouter, createWebHistory} from 'vue-router'
import userList from "../components/users/userList.vue";
import facilityList from "../components/facilities/facilityList.vue";
import reportList from "../components/reports/reportList.vue";
import calendarReport from "../components/calendar/calendarReport.vue";
import loginPage from "../pages/loginPage.vue";
import cabinet from "../components/personalCabinet/cabinet.vue";
import mainReport from "../components/mainReport/mainReport.vue";
import mainReportForDepDirectors from "../components/mainReport/mainReportForDepDirectors.vue";
import mainTableTools from "../components/tools/mainTableTools.vue";
import mainEventsTable from "../components/events/mainEventsTable.vue";
import mainReportForEach from "../components/mainReport/mainReportForEach.vue";
import mainTableToolsAll from "../components/tools/mainTableToolsAll.vue";
import test from "../components/tools/test.vue";
import jobs from "../components/jobs/facilityList.vue";
import bonus from "../components/individualJob/facilityList.vue";
import bonusByMyObjects from "../components/jobs/facilityListForManagers.vue";
import bonusByUser from "../components/bonusByUser/bonusByUser.vue";

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
      path: '/mainReport', component: mainReport
    },
    {
      path: '/mainReportForEach', component: mainReportForEach
    },
    {
      path: '/mainReportForDepDirectors', component: mainReportForDepDirectors
    },
    {
      path: '/mainTableToolsAll', component: mainTableToolsAll
    },
    {
      path: '/mainTableTools', component: mainTableTools
    },
    {
      path: '/events', component: mainEventsTable
    },
    {
      path: '/jobs', component: jobs
    },
    {
      path: '/bonus', component: bonus
    },
    {
      path: '/bonusByMyObjects', component: bonusByMyObjects
    },
    {
      path: '/bonusByUser', component: bonusByUser
    },
    {
      path: '/test', component: test
    }
  ],
  history: createWebHistory()
})

export default router;


<template>
  <v-row justify="center" >
    <v-col align="center" cols="auto" >
        <user-by-hour v-for="report in filteredByDayReports(day)" :report="report" v-bind:key="report.id"></user-by-hour>
    </v-col>
  </v-row>
</template>

<script>
import UserByHour from "./userByHour.vue";
export default {
  components: {UserByHour},
  props: ['reports', 'day'],
  data() {
    return {
      reportsPerDay: [],
    }
  },
  methods: {
    filteredByDayReports(day) {
      return this.reports.filter(r => (new Date(
          r.reportDay.substr(6, 4),
          r.reportDay.substr(3, 2) - 1,
          r.reportDay.substr(0, 2))).toString() === day.toString()).sort((a, b) => {
        if (a.user.name < b.user.name) {
          return -1;
        }
        if (a.user.name > b.user.name) {
          return 1;
        }
        return 0;
      });
    }
  }
}
</script>

<style scoped>
</style>

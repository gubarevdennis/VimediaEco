<template>
  <v-sheet align="center"
          class="moneyPerDay"
          label
          rounded="lg"
  >
    <v-card-title style="font-size: 15px; color: #EBB652">
      {{hoursByDay(day) + ' ч.'}}
    </v-card-title>
  </v-sheet>
</template>

<script>
export default {
  props: ['day', 'reports', 'url', 'port'],
  data() {
    return {
      hours: 0,
    }
  },
  methods: {
    hoursByDay(day) {
      this.hours = 0;
      var calcHours = 0;

      var filteredByDayReports = this.reports.filter(r => (new Date(
          r.reportDay.substr(6, 4),
          r.reportDay.substr(3, 2) - 1,
          r.reportDay.substr(0, 2))).toString() === day.toString())

      filteredByDayReports.forEach(r => calcHours += r.hoursOfWorking)
      return this.hours = calcHours
    },
  }
}
</script>

<style scoped>
.moneyPerDay{

  width: 120px;

  text-align: center;
  background: #393a34;
}
</style>

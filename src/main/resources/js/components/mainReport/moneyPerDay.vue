<template>
  <v-sheet align="center"
          class="moneyPerDay"
          label
          rounded="lg"
  >
    <v-card-text style="font-size: 15px; color: #EBB652">
      {{hoursByDay(day)*625 + ' р.'}}
    </v-card-text>
  </v-sheet>
</template>

<script>
export default {
  name: "moneyPerDay",
  props: ['day', 'reports'],
  data() {
    return {
      hours: 0,
    }
  },
  methods: {
    hoursByDay(day) {
      this.hours = 0;

      var filteredByDayReports = this.reports.filter(r => (new Date(
          r.reportDay.substr(6, 4),
          r.reportDay.substr(3, 2) - 1,
          r.reportDay.substr(0, 2))).toString() === day.toString())

      filteredByDayReports.forEach(r => this.hours += r.hoursOfWorking)
      return this.hours
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

<template>
  <v-sheet align="center"
          class="moneyPerDay"
          label
          rounded="lg"
  >
    <v-card-title align="center" style="font-size: 14px; color: #D88E32">
      {{hoursByDay(day) + ' ч'}}
    </v-card-title>
    <v-card-title align="center" style="font-size: 14px; color: #EBB652">
      {{moneyByDay(day) + ' руб'}}
    </v-card-title>
  </v-sheet>
</template>

<script>
export default {
  props: ['day', 'reports', 'url', 'port'],
  data() {
    return {
      hours: 0,
      money: 0
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
    moneyByDay(day) {
      this.money = 0;
      var calcMoney = 0;

      var filteredByDayReports = this.reports.filter(r => (new Date(
          r.reportDay.substr(6, 4),
          r.reportDay.substr(3, 2) - 1,
          r.reportDay.substr(0, 2))).toString() === day.toString())


      filteredByDayReports.forEach(r => calcMoney += r.hoursOfWorking > 8 ? 8 : r.hoursOfWorking * r.user.salary )
      return this.money =  Math.round(calcMoney/8) // попросил Антон
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

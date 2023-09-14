<template>
  <v-sheet color="#393a34">
    <v-row justify="center">
      <v-col align="center" cols="auto" v-for="day in daysPerOneWeek(w)" v-bind:key="day">
        <day :role="role" :reports="reports" :day="day"></day>
      </v-col>
    </v-row>
<br>
  </v-sheet>
</template>

<script>

// function getWeekNumber(date, exact) {
//   var month = date.getMonth()
//       , year = date.getFullYear()
//       , firstWeekday = new Date(year, month, 1).getDay()
//       , lastDateOfMonth = new Date(year, month + 1, 0).getDate()
//       , offsetDate = date.getDate() + firstWeekday - 1
//       , index = 1 // start index at 0 or 1, your choice
//       , weeksInMonth = index + Math.ceil((lastDateOfMonth + firstWeekday - 7) / 7)
//       , week = index + Math.floor(offsetDate / 7)
//   ;
//   if (exact || week < 2 + index) return week;
//   return week === weeksInMonth ? index + 5 : week;
// }

import Day from "./day.vue";
export default {
  props: ['w', 'reports', 'daysPerWeek', 'role'],
  components: {Day},
  data() {
    return {

    }
  },
  methods: {
    daysPerOneWeek: function (w) {
     return  this.daysPerWeek.filter(d => d.week === w)

    }
  }
}
</script>

<style scoped>

</style>

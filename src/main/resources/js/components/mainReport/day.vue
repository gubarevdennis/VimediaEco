<template>
  <v-sheet style=" width: 200px; height: 550px;" align="center" color="#393a34" >
    <br>
    <v-row align="center" justify="center" style="min-height: 450px" align-content="end">
      <users-by-day :reports="reports" :day="day.day"></users-by-day>
    </v-row>
    <v-row align="center" align-content="center" justify="center">
      <day-number :day="day.day"></day-number>
    </v-row>
    <v-row align="center" align-content="center" justify="center" >
      <money-per-day :reports="reports" :day="day.day"></money-per-day>
    </v-row>
    <br>
  </v-sheet>
</template>




<script>

import UsersByDay from "./usersByDay.vue";
import DayNumber from "./dayNumber.vue";
import MoneyPerDay from "./moneyPerDay.vue";
const url = 'http://localhost:'
const port = '9000/'
//const url = 'http://reports.vimedia.ru/'

// Функция для определения индекса элементов коллекции
function getIndex(list, id) {
  for (let i = 0; i < list.length; i++) {
    if(list[i].id === id) {
      return i;
    }
  }
  return -1;
}

export default {
  components: {MoneyPerDay, DayNumber, UsersByDay},
  props: ['day', 'reports'], // чтобы рабоать с данной переменной и передавать ее выше в корень
  // функция нужна для того чтобы у каждого компонента было свое уникальное хранилище
  data() {
    return {
      id: '',
    }
  },
  methods: {
    reportsByDay: function () {
      // this.reports.
    },

  },

// функция следит на изменениями переменной
  watch: {
    userAttr: function (newVal, oldVal) {
      this.id = newVal.id;
    }
  }
}
</script>

<style>

</style>

<template>
  <v-sheet style=" width: 200px; font-size: 15px" align="center" color="#393a34" >
    <br>
    {{dayName(day.day.getDay())}}
    <br>
    <br>
    <v-row align="center" align-content="center" justify="center">
      <day-number :day="day.day"></day-number>
    </v-row>
    <v-row v-if="role === 'Директор'" align="center" align-content="center" justify="center" >
      <money-per-day :reports="reports" :day="day.day"></money-per-day>
    </v-row>
    <v-row align="center" justify="center" style="" align-content="end">
      <users-by-day :reports="reports" :day="day.day" :role="role"></users-by-day>
    </v-row>
    <br>
  </v-sheet>
</template>




<script>

import UsersByDay from "./usersByDay.vue";
import DayNumber from "./dayNumber.vue";
import MoneyPerDay from "./moneyPerDay.vue";

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
  props: ['day', 'reports', 'role'], // чтобы рабоать с данной переменной и передавать ее выше в корень
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
    dayName: function (day) {
      switch (day) {
        case 1 : return  'Понедельник'
          break;
        case 2 : return  'Вторник'
          break;
        case 3 : return  'Среда'
          break;
        case 4 : return 'Четверг'
          break;
        case 5 : return  'Пятница'
          break;
        case 6 : return  'Суббота'
          break;
        case 0 : return  'Воскресенье'
          break;
      }
    }

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

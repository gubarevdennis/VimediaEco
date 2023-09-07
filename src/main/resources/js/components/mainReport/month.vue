<template >

  <div class="main" >
    <v-sheet color="#393a34">
      <v-card-title align="center" style="color: #FFE075; font-size: 25px">
        {{nowDate()}}
      </v-card-title>
      <v-row justify="center"
             align="center" v-for="w in [1,2,3,4,5]" v-bind:key="w">
        <v-sheet color="#393a34">
          <br>
          <week :reports="reports" :daysPerWeek="daysPerWeek" :w="w"></week>
        </v-sheet>

      </v-row>

      <br>
    </v-sheet>

  </div>

</template>

<script>


Date.prototype.addDays = function(days) {
  var date = new Date(this.valueOf());
  date.setDate(date.getDate() + days);
  return date;
}

import Week from "./week.vue";
export default {
  props: ['users', 'profile', 'role', 'profileId', 'reports'],
  components: {
    Week,
  },

  data() {
    return {
      daysPerMonth: [],
      daysPerWeek: [],
    }
  },
  computed: {},
  // указываем связь данного компонента с полученными от сервара данными
  created: function () {
    // Запускаем функцию заполнения массива дат в выбранном месяце
    var date = new Date()
    this.countDaysPerWeek(new Date(date.getFullYear(), date.getMonth(),1),
        new Date(date.getFullYear(), date.getMonth() + 1, 0))

    var count = 1;
    var week = 1;
    this.daysPerMonth.forEach(d => {
      this.daysPerWeek.push(
          {
            week: week,
            day: d
          }
      )

      count++
      if (count > 7)
      {
        count = 1
        week++
      }
    })

  },
  methods: {
    countDaysPerWeek: function (startDate, stopDate) {
      var currentDate = startDate;
      while (currentDate <= stopDate) {
        this.daysPerMonth.push(new Date(currentDate));
        currentDate = currentDate.addDays(1);
      }
      return this.daysPerMonth;
    },
    nowDate: function () {
      // Выводим текущую дату
      var date = new Date()
      var options = { month: 'long' }; // weekday: 'long',

      return date.toLocaleDateString("ru", options).toUpperCase()
    },
  }
}
</script>


<style>
.main{
  background: #393a34;
}

</style>

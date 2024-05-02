<template >

  <div class="main" >
    <v-sheet color="#393a34">
      <v-row justify="center" align="center">
        <v-col align="start" cols="auto" >
          <v-btn icon="mdi-menu-left" rounded="lg" height="25px"  color="#FFE075"   align="center" @click="monthDown">
          </v-btn>
        </v-col>
        <v-col align="center" cols="auto">
          <v-text align="center" style="color: #FFE075; font-size: 20px">
            {{nowDate()}}
          </v-text>
        </v-col>
        <v-col align="end" cols="auto">
          <v-btn icon="mdi-menu-right"  rounded="lg"   height="25px" color="#FFE075"   align="center" @click="monthUp">
          </v-btn>
        </v-col>
      </v-row>

      <v-row justify="center" v-if=" this.role.split(' ')[0] === 'Руководитель' || this.role === 'Директор'">
        <v-card-title >
          Всего за месяц:
          {{ Math.round(this.reports
            .filter(r => ((
                (Number(r.reportDay.substr(3, 2)))
            ) === Number(nowMonth())))
            // .map(r => r.user ? r.user.salary ? r.hoursOfWorking * r.user.salary/8 : 0 : 0)
            .map(r => r.cost ? parseInt(r.cost) : 0)
            .reduce((partialSum, a) => partialSum + a, 0))
          }} р
        </v-card-title>
      </v-row>
      <v-row justify="center" v-if="facilityCoast !== 0 && this.role === 'Директор'">
        <v-card-title>
          Всего по объекту: {{ facilityCoast }} р
        </v-card-title>
      </v-row>

      <v-row justify="center"
             align="center" v-for="w in [1,2,3,4,5]" v-bind:key="w">
        <v-sheet color="#393a34">
          <br>
          <week :reports="reports" :daysPerWeek="daysPerWeek" :role="role" :w="w"></week>
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
  props: ['users', 'profile', 'role', 'profileId', 'reports', 'updateAllReports', 'facilityCoast'],
  components: {
    Week,
  },

  data() {
    return {
      daysPerMonth: [],
      daysPerWeek: [],
      date: '',
      offSetMonth: 0
    }
  },
  computed: {},
  // указываем связь данного компонента с полученными от сервара данными
  created: function () {

    // Запускаем функцию заполнения массива дат в выбранном месяце
    this.date = new Date()
    var date = this.date
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
      this.daysPerMonth = []
      var currentDate = startDate;
      while (currentDate <= stopDate) {
        this.daysPerMonth.push(new Date(currentDate));
        currentDate = currentDate.addDays(1);
      }
      return this.daysPerMonth;
    },
    nowDate: function () {
      // Выводим текущую дату
      var date = this.date
      var options = { month: 'long' }; // weekday: 'long',

      return date.toLocaleDateString("ru", options).toUpperCase()
    },
    nowMonth: function () {
      // Выводим текущую дату
      var date = this.date
      var options = { month: 'numeric' }; // weekday: 'long',

      return date.toLocaleDateString("ru", options).toUpperCase()
    },
    monthUp: function () {
      var dt = this.date
      this.offSetMonth++
      this.date = new Date(dt.setMonth(dt.getMonth() + 1))
      this.updateAllReports(this.offSetMonth)
      this.updateDays()
    },
    monthDown: function () {
      var dt = this.date
      this.offSetMonth--
      this.date = new Date(dt.setMonth(dt.getMonth() - 1))
      this.updateAllReports(this.offSetMonth)
      this.updateDays()
    },
    updateDays: function () {
      this.daysPerWeek = []
      // Запускаем функцию заполнения массива дат в выбранном месяце
      var date = this.date
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
      console.log(this.daysPerMonth)

    }
  }
}
</script>


<style>
.main{
  background: #393a34;
}

</style>

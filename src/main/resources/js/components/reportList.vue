<template>
  <v-card
      class="bg-surface-variant mb-4"
  >

    <v-card-title align="center">
      Сегодня
      <br>
      {{nowDate()}}
    </v-card-title>

    <v-row
        align="start"
        no-gutters
    >
      <v-col align="start">
        <report-form :reports="reports" :facilities="facilities" :reportAttr="report" :profileId="profileId" />
      </v-col>
    </v-row>
    <v-card-title>
      Отчеты
    </v-card-title>
    <br>
    <v-row>
      <v-select
          variant="outlined"
          @update:modelValue="clicked"
          label="Выберите день"
          :items="['Сегодня','Вчера','Позавчера','Три дня назад','Четыре дня назад','Пять дней назад','Шесть дней назад']"
          :item-value="offsetText"
      ></v-select>
    </v-row>
    <v-card  v-if="isDirector" v-for="facility in facilities" :key="facility.id" color="#DCDCDC">
      <v-card-title v-text="facility.name"></v-card-title>
      <v-card v-for="report in sortedReportsByFacility(facility)" class="pa-2 ma-2" >
        <report-row v-bind:key="report.id"
                    :report="report"
                    :editReport="editReport" :reports="reports"
        />
      </v-card>
    </v-card>
  </v-card>
</template>

<script>
import reportRow from "./reportRow.vue";
import reportForm from "./reportForm.vue";

const url = 'http://sisyphos.vimedia.ru/'

export default {
  props: [ 'users', 'facilities', 'appBoardName', 'profileId'],
  components: {
    reportRow,
    reportForm
  },

  data() {
    return {
      report: null,
      sortedByDay: [],
      offsetAttr: 0,
      offsetText: '',
      isDirector: true,
      facilities: [],
      reports: []
    }
  },
  computed: {
    sortedReportsById() {
      return this.reports.sort((a,b) => -(a.id-b.id)) // сортировка по убыванию (каждый раз когда будет меняться facilities)
    },
    // sortedReportsByFacilities() {
    //   return this.reports.sort((a,b) => {
    //     if (a.facility && b.facility) {
    //       return a.facility.name.localeCompare(b.facility.name); // sort alphabetically
    //     }
    //   }) // сортировка по убыванию (каждый раз когда будет меняться facilities)
    // }
  },
  // указываем связь данного компонента с полученными от сервара данными
  created: function () {
    // if (this.role === 'admin') {
    //   // Получаем отчеты
    //   this.axios.get("http://localhost:9000/api/report").then(result => {
    //     result.data.forEach(report => this.reports.push(report))
    //   })
    // } else {
    //   // Получаем отчеты
    //   this.axios.get("http://localhost:9000/api/report/" + this.profileId).then(result => {
    //     result.data.forEach(report => this.reports.push(report))
    //   })
    // }
    if (this.role === 'admin' || this.role === 'Директор' || this.role === 'HR') {
      this.axios.get(url + "api/user").then(result =>
          result
              .data
              .forEach(u => {
                u.reports.forEach(r => {
                      this.reports.push(r)
                      if (!this.facilities.find((f) => f.id === r.facility.id)) {
                        this.facilities.push(r.facility)
                      }
                    }
                )}))
    } else {
// фильтруем под человека
      this.axios.get(url + "api/user/" + this.profileId).then(result =>
          result
              .data
              .reports.forEach(r => {
                this.reports.push(r)
                if (!this.facilities.find((f) => f.id === r.facility.id)) {
                  this.facilities.push(r.facility)
                }
              }
          )
      )
    }

    // // Получаем объекты
    //  this.axios.get("http://localhost:9000/api/facility").then(result => {
    //        // Вставляем объекты только вслучае если их не было до этого в массиве на фронтенде
    //        return result.data.forEach(facility => {
    //          if (this.facilities.find((f) => f.id === facility.id) === undefined)
    //            return this.facilities.push(facility)
    //        })
    //      }
    //  )
  },
  methods: {
    editReport: function (report) {
      this.report = report
      console.log("Функция editReport " + report.text)
    },
    sortedReportsByFacility(facility) {
      // Выводим текущую дату
      var date = new Date()

      if(this.offsetAttr) {
        date.setDate(date.getDate() + this.offsetAttr)
      }

      // Форматируем текущую дату (добавляем нули, гду нужно и когда нужно)
      var dateForSort = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate())
          +'-'+(date.getMonth()+1 < 10 ? '0' + (date.getMonth()+1) : (date.getMonth()+1))+'-'+date.getFullYear()

      return this.reports.filter((r) => {
        if (r.facility) {
          return  r.facility.id === facility.id && r.reportDay === dateForSort }})   // сортировка по facility

    },
    clicked: function(offsetText) {
      this.offsetText = offsetText
      switch (offsetText) {
        case 'Сегодня': this.offsetAttr=0;
          break;
        case 'Вчера': this.offsetAttr=-1;
          break;
        case 'Позавчера': this.offsetAttr=-2;
          break;
        case 'Три дня назад': this.offsetAttr=-3;
          break;
        case 'Четыре дня назад': this.offsetAttr=-4;
          break;
        case 'Пять дней назад': this.offsetAttr=-5;
          break;
        case 'Шесть дней назад': this.offsetAttr=-6;
          break;
        default: this.offsetAttr=0;
      }
    },
    nowDate: function () {
      // Выводим текущую дату
      var date = new Date()

      // Форматируем текущую дату (добавляем нули, гду нужно и когда нужно)
      return (date.getDate() < 10 ? '0' + date.getDate() : date.getDate())
          + '-' + (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1)) + '-' + date.getFullYear()
    },
  },
}

</script>

<style>

</style>

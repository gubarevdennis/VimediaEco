<template>
  <v-card
      class="bg-surface-variant mb-4"
  >

    <v-card-title align="center">
      Сегодня
      <br>
      <div style="color: #EBB652" >
        {{nowDate()}}
      </div>

    </v-card-title>

    <v-row
        align="start"
        no-gutters
    >
      <v-col align="start">
        <report-form
            :report="report"
            :editSelect="editSelect"
            :reports="reports"
            :facilities="facilities"
            :reportAttr="report"
            :profileId="profileId"
            :editReportStatus="editReportStatus"
        />
      </v-col>
    </v-row>
    <br>

    <br>
    <v-divider></v-divider>
    <br>
    <br>
    <v-card-title style="font-size: 28px; color: #EBB652" align="center">
      Список отчетов
    </v-card-title>
    <br>
    <v-divider></v-divider>

    <v-select
        v-if="!datePickerShow"
        variant="outlined"
        @update:modelValue="clicked"
        label="Выберите день для просмотра"
        :items="['Сегодня','Вчера','Позавчера','Три дня назад','Четыре дня назад','Пять дней назад','Шесть дней назад']"
        :item-value="offsetText"
    ></v-select>

    <br>

    <v-row align="center" justify="center">
      <v-btn rounded="lg" variant="outlined" style="font-size: 20px" height="30"  align="center" @click="datePickerShowFunc">
        <v-text v-if="datePickerShow" style=" font-size: 15px;" >
          Скрыть календарь
        </v-text>
        <v-text v-if="!datePickerShow" style=" font-size: 15px;" >
          Выбрать на календаре
        </v-text>
      </v-btn>
    </v-row>
    <br>
    <br>
    <v-row align="center" justify="center">
      <VDatePicker         v-if="datePickerShow"
                           @update:modelValue="datePickClickFunc"
                           v-model="datePick" mode="date" />
    </v-row>
    <br>
    <br>

    <v-card  v-for="facility in facilities" :key="facility.id" color="#DCDCDC">
      <v-card v-for="report in sortedReportsByFacility(facility)" class=" ma-3 pa-2" >
        <v-card-title v-text="facility.name"></v-card-title>
        <report-row v-bind:key="report.id"
                    :report="report"
                    :editReport="editReport" :editReportSelect="editReportSelect" :reports="reports"
        />
      </v-card>
    </v-card>
  </v-card>
</template>

<script>
import reportRow from "./reportRow.vue";
import reportForm from "./reportForm.vue";

// const url = 'http://localhost:'
// const port = '9000/'
// const url = 'http://reports.vimedia.ru/'
// const port = ''

export default {
  props: [ 'users', 'appBoardName', 'profileId', 'reports', 'url', 'port'],
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
      subFacilities: [],
      reports: [],
      editReportStatus: false,
      datePickerShow: false,
      datePick: ''
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


    if (this.role === 'admin' || this.role === 'Директор' || this.role === 'HR') {
      this.axios.get("/api/user").then(result =>
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
      this.axios.get("/api/user/" + this.profileId).then(result =>
          result
              .data
              .reports.forEach(r => {

                this.reports.push(r)
                if (!this.facilities.find((f) => f.id === r.facility.id)) {
                  this.facilities.push(r.facility)
                  console.log(this.facilities)
                }
              }
          )
      )
    }
  },
  methods: {
    editReport: function (report) {
      this.editReportStatus = true
      this.report = report

      console.log("Функция editReport " + report.text)
    },
    editReportSelect: function () {
      this.editSelect(this.report)
      console.log('editReportSelect')
      console.log(this.report)
    },
    editSelect: function (report) {
        console.log('editSelect')
        console.log(report)
    },
    facilityStatus: function () {

    },
    sortedReportsByFacility(facility) {
      // Выводим текущую дату
      var date = new Date()

      if(this.offsetAttr) {
        date.setDate(date.getDate() + this.offsetAttr)
      }

      if(this.datePick && this.datePickerShow) {
        date.setFullYear(this.datePick.getFullYear())
        date.setMonth(this.datePick.getMonth())
        date.setDate(this.datePick.getDate())
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
      var options = { year: 'numeric', month: 'long', day: 'numeric' }; // weekday: 'long',

      return date.toLocaleDateString("ru", options)

      // Форматируем текущую дату (добавляем нули, гду нужно и когда нужно)
      return (date.getDate() < 10 ? '0' + date.getDate() : date.getDate())
          + '-' + (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1)) + '-' + date.getFullYear()
    },
    datePickerShowFunc: function () {
      var old =this.datePickerShow;
      this.datePickerShow = !old;
    },
    datePickClickFunc: function () {

    }
  },
  watch: {
    reportAttr: function (newVal, oldVal) {
      this.text = newVal.text;
      this.id = newVal.id;
    },
  }
}

</script>

<style>

</style>

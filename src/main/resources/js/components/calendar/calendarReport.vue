<template>
  <v-card>
    <br>
    <v-row justify="center">
      <v-card @click="allWorks" width="200" align="center">Все работы</v-card>
      <v-card @click="redWork" width="200" color="#A0522D" align="center">Черновой монтаж</v-card>
      <v-card @click="greenWork" width="200" color="#FF0000" align="center">Чистовой монтаж</v-card>
      <v-card @click="pinkWork" width="200" color="#F08080" align="center">Шефмонтаж</v-card>
      <v-card @click="blueWork" width="300" color="#9ACD32" align="center">Концептуальное проектирование</v-card>
      <v-card @click="tealWork" width="200" color="#00FF00" align="center">Рабочее проектирование</v-card>
      <v-card @click="yellowWork" width="200" color="#BC8F8F" align="center">Сборка щитов</v-card>
      <v-card @click="greyWork" width="100" color="#FF4500" align="center">ПНР</v-card>
      <v-card @click="grey1Work" width="100" color="#FFA500" align="center">Сервис</v-card>
      <v-card @click="grey2Work" width="200" color="#FF1493" align="center">Авторский надзор</v-card>
      <v-card @click="grey3Work" width="200" color="#BDB76B" align="center">Менеджмент</v-card>
      <v-card @click="grey4Work" width="200" color="#7FFFD4" align="center">Отпуск оплачиваемый</v-card>
      <v-card @click="grey5Work" width="300" color="#7B68EE" align="center">Отпуск по семейным обстоятельствам</v-card>
      <v-card @click="grey6Work" width="300" color="#00FFFF" align="center">Отпуск без сохранения ЗП</v-card>
      <v-card @click="grey7Work" width="100" color="##800080" align="center">Больничный</v-card>
    </v-row>

    <br>
    <br>

    <v-select
        v-if="this.directorOff"
        variant="outlined"
        @update:modelValue="selected"
        label="Укажите сотрудника..."
        :items="userNames"
        :item-value="userName"
    ></v-select>


    <v-row
        v-for="(facility, index) in sortedFacilities"
        justify="center"
        v-bind:facility="facility"
        v-bind:index="index"
        v-bind:key="facility.id"
        justify-sm="0">
      <v-card  v-bind:color="facility.color" >
        <v-col align-self="center" cols="auto">
          <v-card-title align="center"> {{facility.name}}</v-card-title>
        </v-col>
        <v-col cols="auto">
          <calendar-page :profile="profile" :role="role" :profileId="profileId" :attributes="attributeColor(facility.id)" :columns="columns" @did-move="moveCal"></calendar-page>
        </v-col>
        <v-row>
          <v-col v-for="filteredSum in calcSumHoursByMonthAndUser(facility.id)">
            <v-card v-if="this.directorOff" align="center">
              {{ filteredSum.hoursOfWorking }} ч
            </v-card>
          </v-col>
        </v-row>
        <br>
      </v-card>
    </v-row>
  </v-card>
</template>

<script>

import {useScreens} from 'vue-screen-utils'
import CalendarPage from "./calendarPage.vue";


// const url = 'http://localhost:'
// const port = '9000/'
// const url = 'http://reports.vimedia.ru/'
// const port = ''

function setColorByWork(work) {
  switch (work) {
    case 'Черновой монтаж' :
      return '#A0522D'
      break;
    case 'Чистовой монтаж' :
      return '#FF0000'
      break;
    case 'Шефмонтаж' :
      return '#F08080'
      break;
    case 'Концептуальное проектирование' :
      return '#9ACD32'
      break;
    case 'Рабочее проектирование' :
      return '#006400'
      break;
    case 'Сборка щитов' :
      return '#E0FFFF'
      break;
    case 'Расключение шкафов' :
      return '#008B8B'
      break;
    case 'ПНР' :
      return '#FF4500'
      break;
    case 'Сервис' :
      return '#FFA500'
      break;
    case 'Авторский надзор' :
      return '#FF1493'
      break;
    case 'Менеджмент' :
      return '#BDB76B'
      break;
    case 'Отпуск' :
      return '#7FFFD4'
      break;
    case 'Отпуск по семейным обстоятельствам' :
      return '#7B68EE'
      break;
    case 'Отпуск без сохранения ЗП' :
      return '#00008B'
    case 'Больничный' :
      return '#800080'
      break;
    default:
      return '#FFFFFF'
  }
}

function workToNumber(typeOfWork) {
  var typeOfWorkByNumber = 0;
  switch (typeOfWork) {
    case 'Черновой монтаж' : typeOfWorkByNumber = 1;
      break;
    case 'Рабочее проектирование' : typeOfWorkByNumber = 2;
      break;
    case 'Авторский надзор' : typeOfWorkByNumber = 3;
      break;
    case 'Сборка щитов' : typeOfWorkByNumber = 4;
      break;
    case 'ПНР' : typeOfWorkByNumber = 5;
      break;
    case 'Сервис' : typeOfWorkByNumber = 6;
      break;
    case 'Отпуск оплачиваемый' : typeOfWorkByNumber = 7;
      break;
    case 'Больничный' : typeOfWorkByNumber = 8;
      break;
    case 'Отпуск без сохранения ЗП' : typeOfWorkByNumber = 9;
      break;
    case 'Другие работы' : typeOfWorkByNumber = 10;
      break;
    case 'Концептуальное проектирование' : typeOfWorkByNumber = 11;
      break;
    case 'Шефмонтаж' : typeOfWorkByNumber = 12;
      break;
    case 'Чистовой монтаж' : typeOfWorkByNumber = 13;
      break;
    case 'Отпуск по семейным обстоятельствам' : typeOfWorkByNumber = 14;
      break;
    case 'Расключение шкафов' : typeOfWorkByNumber = 15;
      break;
  }
  return typeOfWorkByNumber;
}


// Функция удаления дубликатов
function removeDuplicates(arr) {

  const result = [];
  const duplicatesIndices = [];

  // Перебираем каждый элемент в исходном массиве
  arr.forEach((current, index) => {

    if (duplicatesIndices.includes(index)) return;

    result.push(current);

    // Сравниваем каждый элемент в массиве после текущего
    for (let comparisonIndex = index + 1; comparisonIndex < arr.length; comparisonIndex++) {

      const comparison = arr[comparisonIndex];
      const currentKeys = Object.keys(current);
      const comparisonKeys = Object.keys(comparison);

      // Проверяем длину массивов
      if (currentKeys.length !== comparisonKeys.length) continue;

      // Проверяем значение ключей
      const currentKeysString = currentKeys.sort().join("").toLowerCase();
      const comparisonKeysString = comparisonKeys.sort().join("").toLowerCase();
      if (currentKeysString !== comparisonKeysString) continue;

      // Проверяем индексы ключей
      let valuesEqual = true;
      for (let i = 0; i < currentKeys.length; i++) {
        const key = currentKeys[i];
        if ( current[key] !== comparison[key] ) {
          valuesEqual = false;
          break;
        }
      }
      if (valuesEqual) duplicatesIndices.push(comparisonIndex);

    } // Конец цикла
  });
  return result;
}

export default {
  components: {CalendarPage},
  props: ['facilities', 'profile', 'role', 'profileId'],
  data() {
    return {
      facilities: [],
      sortedFacilities: [],
      sortedReports: [],
      sortedReportsByUser: [],
      reports: [],
      refer: [],
      attributes: [],
      columns: {},
      expanded: {},
      index: [],
      userNames: [],
      userName: '-',
      sumHoursByMonthAndUser: [],
      sumHoursByMonth: [],
      sum: [],
      directorOff: false
    }
  },
  computed: {

  },
  // функция следит на изменениями переменной
  methods: {
    attributeColor: function (facilityId) {
      var date = new Date();
      var year = date.getFullYear();
      var month = date.getMonth();

      var attributesByDay = []


      this.sortedReportsByUser.forEach(r => {
            if (r.facility)
              if (r.facility.id === facilityId) {
                var newDate = r.reportDay

                attributesByDay.push({
                  highlight: {
                    color: setColorByWork(r.typeOfWork),
                    fillMode: 'solid',
                    contentClass: 'italic',
                  },
                  dates: new Date(newDate.substr(6, 4), newDate.substr(3, 2) - 1, newDate.substr(0, 2)),
                })
              }
          }
      )

      return attributesByDay
    },
    allWorks: function () {
      this.sortedFacilities = []
      this.sortedReports = []
      this.sortedReportsByUser = []

      this.reports
          .forEach(r => {
            if ((r.user.name + ' - ' +  r.user.role) === this.userName) {
              this.sortedReportsByUser.push(r)
            }
          })

      if (this.userName === 'Все сотрудники' || this.userName === '-') {
        this.sortedReportsByUser = this.reports
        this.sortWorkHours()
      }

      this.sortedReportsByUser.forEach(r => {
        if (!this.sortedFacilities.find((f) => f.id === r.facility.id)) {
          this.sortedFacilities.push(r.facility)
        }
      })
    },
    redWork: function () {
      this.sortedFacilities = []
      this.sortedReports = []
      this.sortedReportsByUser = []

      this.sortByColor('Черновой монтаж')
      this.sortWorkHoursByWorkTypes('Черновой монтаж')
    },
    greenWork: function () {
      this.sortedFacilities = []
      this.sortedReports = []
      this.sortedReportsByUser = []

      this.sortByColor('Чистовой монтаж')
      this.sortWorkHoursByWorkTypes('Чистовой монтаж')
    },
    pinkWork: function () {
      this.sortedFacilities = []
      this.sortedReports = []
      this.sortedReportsByUser = []

      this.sortByColor('Шефмонтаж')
      this.sortWorkHoursByWorkTypes('Шефмонтаж')
    },
    blueWork: function () {
      this.sortedFacilities = []
      this.sortedReports = []
      this.sortedReportsByUser = []

      this.sortByColor('Концептуальное проектирование')
      this.sortWorkHoursByWorkTypes('Концептуальное проектирование')
    },
    tealWork: function () {
      this.sortedFacilities = []
      this.sortedReports = []
      this.sortedReportsByUser = []

      this.sortByColor('Рабочее проектирование')
      this.sortWorkHoursByWorkTypes('Рабочее проектирование')
    },
    yellowWork: function () {
      this.sortedFacilities = []
      this.sortedReports = []
      this.sortedReportsByUser = []

      this.sortByColor('Сборка щитов')
      this.sortWorkHoursByWorkTypes('Сборка щитов')
    },
    greyWork: function () {
      this.sortedFacilities = []
      this.sortedReports = []
      this.sortedReportsByUser = []

      this.sortByColor('ПНР')
      this.sortWorkHoursByWorkTypes('ПНР')
    },
    grey1Work: function () {
      this.sortedFacilities = []
      this.sortedReports = []
      this.sortedReportsByUser = []

      this.sortByColor('Сервис')
      this.sortWorkHoursByWorkTypes('Сервис')
    },
    grey2Work: function () {
      this.sortedFacilities = []
      this.sortedReports = []
      this.sortedReportsByUser = []

      this.sortByColor('Авторский надзор')
      this.sortWorkHoursByWorkTypes('Авторский надзор')
    },
    grey3Work: function () {
      this.sortedFacilities = []
      this.sortedReports = []
      this.sortedReportsByUser = []

      this.sortByColor('Другие работы')
      this.sortWorkHoursByWorkTypes('Другие работы')
    },
    grey4Work: function () {
      this.sortedFacilities = []
      this.sortedReports = []
      this.sortedReportsByUser = []

      this.sortByColor('Отпуск оплачиваемый')
      this.sortWorkHoursByWorkTypes('Отпуск оплачиваемый')
    },
    grey5Work: function () {
      this.sortedFacilities = []
      this.sortedReports = []
      this.sortedReportsByUser = []

      this.sortByColor('Отпуск по семейным обстоятельствам')
      this.sortWorkHoursByWorkTypes('Отпуск по семейным обстоятельствам')
    },
    grey6Work: function () {
      this.sortedFacilities = []
      this.sortedReports = []
      this.sortedReportsByUser = []

      this.sortByColor('Отпуск без сохранения ЗП')
      this.sortWorkHoursByWorkTypes('Отпуск без сохранения ЗП')
    },
    grey7Work: function () {
      this.sortedFacilities = []
      this.sortedReports = []
      this.sortedReportsByUser = []

      this.sortByColor('Больничный')
      this.sortWorkHoursByWorkTypes('Больничный')
    },
    selected: function (userName) {
      this.userName = userName

      this.sortedReportsByUser = []
      this.sortedFacilities = []
      this.sortedReportsByUser = this.reports
          .filter((r) => r.user.name + ' - ' + r.user.role === userName)

      this.sortedReportsByUser.forEach(r => {
        if (!this.sortedFacilities.find((f) => f.id === r.facility.id)) {
          this.sortedFacilities.push(r.facility)
        }
      })

      if (userName === 'Все сотрудники' || this.userName === '') {
        // Убираем филтр для фильтрации по имени, так как все сотрудники выбраны
        this.sortedFacilities = []
        this.sortedReportsByUser = []
        this.sortedReports = []
        this.sortedFacilities = this.facilities
        this.sortedReportsByUser = this.reports
        this.sortedReports = this.reports
      }

    },
    calcSumHoursByMonthAndUser: function (facilityId) {
      if (this.userName === 'Все сотрудники' || this.userName === '-' )
      {
        return this.sumHoursByMonth.filter(o => o.facilityId === facilityId)
      }


      return this.sumHoursByMonthAndUser.filter(o => o.facilityId === facilityId)
          .filter(o => o.userName + ' - ' +  o.userRole === this.userName)

    },
    moveCal: function () {
      alert("Go!")
    },
    // Функция для сортировки работ по цветам
    sortByColor: function (typeOfWork) {
      this.sortedReports = this.reports
          .filter((r) => r.typeOfWork === typeOfWork)

      this.sortedReportsByUser = this.sortedReports
          .filter((r) => (r.user.name + ' - ' +  r.user.role === this.userName))

      if (this.userName === 'Все сотрудники' || this.userName === '-') {
        this.sortedReportsByUser = this.sortedReports
      }

      this.sortedReportsByUser.forEach(r => {
        if (!this.sortedFacilities.find((f) => f.id === r.facility.id)) {
          this.sortedFacilities.push(r.facility)
        }
      })
    },
    // Для просчета часов наработки по месяцам
    getHoursOfWorkByMonth: function (report) {
      this.sumHoursByMonth = []
      for (let i = 1; i < 4; i++) {
        this.axios.get(`api/facility/sumTime/facility/${report.facility.id}/month/${i}`).then(result => {

              this.sumHoursByMonth.push({
                key: i,
                facilityId: report.facility.id,
                hoursOfWorking: result.data[0],
              })

              // Удаляем дубликаты
              this.sumHoursByMonth = removeDuplicates(this.sumHoursByMonth)
            }
        )
      }
    },
    //Для просчета часов наработки по пользователям и месяцам
    getHoursOfWorkingByUserAndMonth: function (report) {
      this.sumHoursByMonthAndUser = []
      for (let i = 1; i < 4; i++) {

        this.axios.get(`api/facility/sumTime/facility/${report.facility.id}/user/${report.user.id}/month/${i}`).then(result => {

              this.sumHoursByMonthAndUser.push({
                key: i,
                facilityId: report.facility.id,
                hoursOfWorking: result.data[0],
                userName: report.user.name,
                userRole: report.user.role,
              })

              // Удаляем дубликаты
              this.sumHoursByMonthAndUser = removeDuplicates(this.sumHoursByMonthAndUser)
            }
        )
      }
    },
    // Для просчета часов наработки по месяцам и типу работы
    getHoursOfWorkByMonthAndWorkType: function (report, typeOfWorking) {
      this.sumHoursByMonth = []
      for (let i = 1; i < 4; i++) {
        this.axios.get( `api/facility/sumTime/facility/${report.facility.id}/month/${i}/typeOfWork/${typeOfWorking}`).then(result => {

              this.sumHoursByMonth.push({ // надо сделать сортировку потом чтобы вывод был по попорядку
                key: i,
                facilityId: report.facility.id,
                hoursOfWorking: result.data[0],
              })

              // Удаляем дубликаты
              this.sumHoursByMonth = removeDuplicates(this.sumHoursByMonth)
            }
        )
      }
    },
    //Для просчета часов наработки по пользователям, месяцам и типу работы
    getHoursOfWorkingByUserMonthAndWorkType: function (report, typeOfWorking) {
      this.sumHoursByMonthAndUser = []
      for (let i = 1; i < 4; i++) {

        this.axios.get( `api/facility/sumTime/facility/${report.facility.id}/user/${report.user.id}/month/${i}/typeOfWork/${typeOfWorking}`, typeOfWorking).then(result => {

              this.sumHoursByMonthAndUser.push({
                key: i,
                facilityId: report.facility.id,
                hoursOfWorking: result.data[0],
                userName: report.user.name,
                userRole: report.user.role,
              })

              // Удаляем дубликаты
              this.sumHoursByMonthAndUser = removeDuplicates(this.sumHoursByMonthAndUser)
            }
        )
      }
    },
    //Сортировка по времени наработки и типу работы
    sortWorkHoursByWorkTypes: function (typeOfWork) {
      {
        if (this.directorOff) {
          this.axios.get("/report/user/").then(result =>
              result
                  .data
                  .forEach(r => {
                        var typeOfWorkByNumber = workToNumber(typeOfWork)

                        // Функции просчета времени наработки
                        this.getHoursOfWorkByMonthAndWorkType(r, typeOfWorkByNumber)
                        this.getHoursOfWorkingByUserMonthAndWorkType(r, typeOfWorkByNumber)

                      }
                  )
          )
        }
      }
    },
    //Сортировка по времени наработки (для обновления данных)
    sortWorkHours: function () {
      {
        // Обнуляем то, что было до этого
        this.reports = []
        this.facilities = []
        this.sumHoursByMonth = []
        this.sumHoursByMonthAndUser = []

        if (this.directorOff) {
          this.axios.get( "api/report/user/").then(result =>
              result
                  .data
                  .forEach(r => {
                        if (r.user) {
                          this.userNames.push(r.user.name + ' - ' + r.user.role) // имена для select сотрудников
                          this.reports.push(r)
                          if (r.facility)
                            if (!this.facilities.find((f) => f.id === r.facility.id)) {
                              this.facilities.push(r.facility)
                            }

                          // Функции просчета времени наработки
                          this.getHoursOfWorkByMonth(r)
                          this.getHoursOfWorkingByUserAndMonth(r)
                        }
                      }
                  ))

        } else {
// фильтруем под человека
          this.axios.get( "api/report/user/" + this.profileId).then(result =>
              result
                  .data
                  .forEach(r => {
                        this.reports.push(r)
                        if (r.facility)
                          if (!this.facilities.find((f) => f.id === r.facility.id)) {
                            this.facilities.push(r.facility)
                          }
                      }
                  )
          )
        }

        this.sortedFacilities = this.facilities
        this.sortedReports = this.reports
        this.sortedReportsByUser = this.reports
        this.userNames.push('Все сотрудники') // добавялем "Все сотрудники" для select сотрудников
      }
    },
  },
// указываем связь данного компонента с полученными от сервара данными
  created: function () {

    // сразу делаем запрос и сортируем данные
    this.sortWorkHours()

// Для отображения в строку
    var {mapCurrent} = useScreens({
      xs: '0px',
      sm: '640px',
      md: '768px',
      lg: '1024px',
    });
    this.columns = mapCurrent({lg: 3}, 1);
    this.expanded = mapCurrent({lg: false}, true);

  },
}

</script>

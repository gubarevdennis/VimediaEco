<template>
  <v-card>
    <br>
    <v-row justify="center">
      <v-card @click="allWorks" width="100" align="center">Все работы</v-card>
      <v-card @click="redWork" width="100" color="red" align="center">Монтаж</v-card>
      <v-card @click="greenWork" width="100" color="green" align="center">Проект</v-card>
      <v-card @click="pinkWork" width="100" color="pink" align="center">Менеджмент</v-card>
      <v-card @click="blueWork" width="100" color="blue" align="center">Сборка</v-card>
      <v-card @click="tealWork" width="100" color="teal" align="center">ПНР</v-card>
      <v-card @click="yellowWork" width="100" color="yellow" align="center">Сервис</v-card>
      <v-card @click="greyWork" width="100" color="grey" align="center">Остальное</v-card>
    </v-row>

    <br>
    <br>

    <v-select
        v-if="this.role === 'admin' || this.role === 'Директор' || this.role === 'HR'"
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
            <v-card v-if="filteredSum.hoursOfWorking !== null " align="center">
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

function setColorByWork(work) {
  switch (work) {
    case 'Монтаж' : return 'red'
      break;
    case 'Проект' : return 'green'
      break;
    case 'Менеджмент' : return 'pink'
      break;
    case 'Сборка' : return 'aqua'
      break;
    case 'ПНР' : return 'teal'
      break;
    case 'Сервис' : return 'yellow'
      break;
    default: return 'gray'
  }
}

function workToNumber(typeOfWork) {
  var typeOfWorkByNumber = 0;
  switch (typeOfWork) {
    case 'Монтаж' : typeOfWorkByNumber = 1;
      break;
    case 'Проект' : typeOfWorkByNumber = 2;
      break;
    case 'Менеджмент' : typeOfWorkByNumber = 3;
      break;
    case 'Сборка' : typeOfWorkByNumber = 4;
      break;
    case 'ПНР' : typeOfWorkByNumber = 5;
      break;
    case 'Сервис' : typeOfWorkByNumber = 6;
      break;
    case 'Отпуск' : typeOfWorkByNumber = 7;
      break;
    case 'Больничный' : typeOfWorkByNumber = 8;
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
      sum: []
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

      this.sortByColor('Монтаж')
      this.sortWorkHoursByWorkTypes('Монтаж')
    },
    greenWork: function () {
      this.sortedFacilities = []
      this.sortedReports = []
      this.sortedReportsByUser = []

      this.sortByColor('Проект')
      this.sortWorkHoursByWorkTypes('Проект')
    },
    pinkWork: function () {
      this.sortedFacilities = []
      this.sortedReports = []
      this.sortedReportsByUser = []

      this.sortByColor('Менеджмент')
      this.sortWorkHoursByWorkTypes('Менеджмент')
    },
    blueWork: function () {
      this.sortedFacilities = []
      this.sortedReports = []
      this.sortedReportsByUser = []

      this.sortByColor('Сборка')
      this.sortWorkHoursByWorkTypes('Сборка')
    },
    tealWork: function () {
      this.sortedFacilities = []
      this.sortedReports = []
      this.sortedReportsByUser = []

      this.sortByColor('ПНР')
      this.sortWorkHoursByWorkTypes('ПНР')
    },
    yellowWork: function () {
      this.sortedFacilities = []
      this.sortedReports = []
      this.sortedReportsByUser = []

      this.sortByColor('Сервис')
      this.sortWorkHoursByWorkTypes('Сервис')
    },
    greyWork: function () {
      this.sortedFacilities = []
      this.sortedReports = []
      this.sortedReportsByUser = []

      this.sortByColor('Больничный')
      this.sortByColor('Отпуск')
      this.sortWorkHoursByWorkTypes('Больничный')
      this.sortWorkHoursByWorkTypes('Отпуск')
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
        this.axios.get(`http://192.168.100.100:6552/api/facility/sumTime/facility/${report.facility.id}/month/${i}`).then(result => {

              this.sumHoursByMonth.push({
                key: i,
                facilityId: report.facility.id,
                hoursOfWorking: result.data[0],
              })

              // Удаляем дубликаты
              this.sumHoursByMonth = removeDuplicates(this.sumHoursByMonth)
              console.log(this.sumHoursByMonth)
            }
        )
      }
    },
    //Для просчета часов наработки по пользователям и месяцам
    getHoursOfWorkingByUserAndMonth: function (report) {
      this.sumHoursByMonthAndUser = []
      for (let i = 1; i < 4; i++) {

        this.axios.get(`http://192.168.100.100:6552/api/facility/sumTime/facility/${report.facility.id}/user/${report.user.id}/month/${i}`).then(result => {

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
    // Для просчета часов наработки по месяцам
    getHoursOfWorkByMonthAndWorkType: function (report, typeOfWorking) {
      this.sumHoursByMonth = []
      for (let i = 1; i < 4; i++) {
        this.axios.get(`http://192.168.100.100:6552/api/facility/sumTime/facility/${report.facility.id}/month/${i}/typeOfWork/${typeOfWorking}`).then(result => {

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
    getHoursOfWorkingByUserMonthAndWorkType: function (report, typeOfWorking) {
      this.sumHoursByMonthAndUser = []
      for (let i = 1; i < 4; i++) {

        this.axios.get(`http://192.168.100.100:6552/api/facility/sumTime/facility/${report.facility.id}/user/${report.user.id}/month/${i}/typeOfWork/${typeOfWorking}`, typeOfWorking).then(result => {

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
        if (this.role === 'admin' || this.role === 'Директор' || this.role === 'HR') {
          this.axios.get("http://192.168.100.100:6552/api/user").then(result =>
              result
                  .data
                  .forEach(u => {
                    u.reports.forEach(r => {
                          var typeOfWorkByNumber = workToNumber(typeOfWork)

                          // Функции просчета времени наработки
                          this.getHoursOfWorkByMonthAndWorkType(r, typeOfWorkByNumber)
                          this.getHoursOfWorkingByUserMonthAndWorkType(r, typeOfWorkByNumber)

                        }
                    )
                  })
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

        if (this.role === 'admin' || this.role === 'Директор' || this.role === 'HR') {
          this.axios.get("http://192.168.100.100:6552/api/user").then(result =>
              result
                  .data
                  .forEach(u => {
                    this.userNames.push(u.name + ' - ' +  u.role) // имена для select сотрудников
                    u.reports.forEach(r => {
                          this.reports.push(r)
                          if (!this.facilities.find((f) => f.id === r.facility.id)) {
                            this.facilities.push(r.facility)

                          }

                          // Функции просчета времени наработки
                          this.getHoursOfWorkByMonth(r)
                          this.getHoursOfWorkingByUserAndMonth(r)
                        }
                    )}))

        } else {
// фильтруем под человека
          this.axios.get("http://192.168.100.100:6552/api/user/" + this.profileId).then(result =>
              result
                  .data
                  .reports.forEach(r => {
                    if (!this.facilities.find((f) => f.id === r.facility.id)) {
                      this.reports.push(r)
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

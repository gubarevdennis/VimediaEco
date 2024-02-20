<template>
  <v-sheet>
    <v-row>
      <v-col>
        <br>
        <v-autocomplete
            label="Объект"
            variant="outlined"
            @update:modelValue="selectFacility"
            :items="facilityNames"
            :item-value="facilityNameSelected"
        >

        </v-autocomplete>
      </v-col>
      <v-col v-if="subFacilities[1]">
        <br>
        <v-select
            label="Подобъект"
            variant="outlined"
            @update:modelValue="selectSubFacility"
            :items="subFacilities"
            :item-value="subFacilityNameSelected"
        >

        </v-select>
      </v-col>
      <v-col>
        <br>
        <v-autocomplete
            variant="outlined"
            @update:modelValue="selectUser"
            :item-value="userNameSelected"
            label="Сотрудник отдела"
            :items="users"
        >
        </v-autocomplete>
      </v-col>
      <v-col>
        <br>
        <v-select
            variant="outlined"
            @update:modelValue="selectWork"
            :item-value="workNameSelected"
            label="Вид работ"
            :items="['Все работы','Черновой монтаж', 'Чистовой монтаж','Шефмонтаж',  'Сборка щитов',
          'Концептуальное проектирование', 'Рабочее проектирование', 'Расключение шкафов',
          'ПНР', 'Сервис','Авторский надзор', 'Другие работы' , 'Отпуск по семейным обстоятельствам',
          'Отпуск без сохранения ЗП', 'Отпуск оплачиваемый', 'Больничный']"
        >
        </v-select>
      </v-col>
    </v-row>
  </v-sheet>
  <br>
  <br>
  <v-row color="#393a34" justify="center">
    <v-card  width="200" color="#A0522D" align="center">Черновой монтаж</v-card>
    <v-card  width="200" color="#FF0000" align="center">Чистовой монтаж</v-card>
    <v-card  width="200" color="#F08080" align="center">Шефмонтаж</v-card>
    <v-card  width="300" color="#9ACD32" align="center">Концептуальное проектирование</v-card>
    <v-card  width="200" color="#00FF00" align="center">Рабочее проектирование</v-card>
    <v-card  width="200" color="#BC8F8F" align="center">Сборка щитов</v-card>
    <v-card  width="200" color="#008B8B" align="center">Расключение шкафов</v-card>
    <v-card  width="100" color="#FF4500" align="center">ПНР</v-card>
    <v-card  width="100" color="#FFA500" align="center">Сервис</v-card>
    <v-card  width="200" color="#FF1493" align="center">Авторский надзор</v-card>
    <v-card  width="200" color="#BDB76B" align="center">Другие работы</v-card>
    <v-card  width="200" color="#7FFFD4" align="center">Отпуск оплачиваемый</v-card>
    <v-card width="300" color="#7B68EE" align="center">Отпуск по семейным обстоятельствам</v-card>
    <v-card  width="300" color="#00FFFF" align="center">Отпуск без сохранения ЗП</v-card>
    <v-card width="100" color="#800080" align="center">Больничный</v-card>
  </v-row>
  <br>
  <br>
  <month :role="role" :updateAllReports="updateAllReports" :reports="sortedReports" ></month>
</template>

<script>
import Month from "./month.vue";

function departmentNameFromDepDirectorReturn(role) {
  if (role === "Руководитель сборочного цеха") {
    return ["Сборщик"]
  } else if (role === "Руководитель проектного отдела") {
    return ["Проектировщик"]
  } else if (role === "Руководитель отдела ведения проектов") {
    return ["Руководитель проектов", "Монтажник", "Программист", "Прораб", "Проектировщик"]
  } else if (role === "Руководитель проектов") {
    return ["Монтажник", "Программист", "Прораб", "Проектировщик"]
    // } else if (role === "Прораб") {
    //   return ["Монтажник"]
  }
}

export default {
  components: {Month},
  props: ['profile', 'role'],
  data() {
    return {
      reports: [],
      facilities: [],
      facilityNames: [],
      subFacilities: [],
      facilityNameSelected: '',
      subFacilityNameSelected: '',
      workNameSelected: '',
      byFacility: false,
      bySubFacility: false,
      byWork: false,
      sortedReportsByFacility: [],
      sortedReportsBySubFacility: [],
      sortedReportsByWork: [],
      sortedReports: [],
      users: [],
      userNameSelected: '',
      byUser: false,
      sortedReportsByUser: []
    }
  },
  mounted: function () {
    console.log('Запустил mounted')

    // Запрашиваем пользователей
    this.axios.get( "api/user").then(result => {
          result
              .data
              .forEach(u => {
                if ((u.role === departmentNameFromDepDirectorReturn(this.role)[0])
                    || (u.role === departmentNameFromDepDirectorReturn(this.role)[1])
                    || (u.role === departmentNameFromDepDirectorReturn(this.role)[2])
                    || (u.role === departmentNameFromDepDirectorReturn(this.role)[3])
                    || (u.role === departmentNameFromDepDirectorReturn(this.role)[4])) {
                  this.users.push(u.name) // все пользователи
                }
              })
          this.users.unshift('Все сотрудники')
        }
    )
    console.log('Прогрузил user')

    // Запрашиваем отчеты
    this.axios.get( "api/report").then(result => {
          result
              .data
              .forEach(r => {
                if (r.user)
                if ((r.user.role === departmentNameFromDepDirectorReturn(this.role)[0])
                    || (r.user.role === departmentNameFromDepDirectorReturn(this.role)[1])
                    || (r.user.role === departmentNameFromDepDirectorReturn(this.role)[2])
                    || (r.user.role === departmentNameFromDepDirectorReturn(this.role)[3])
                    || (r.user.role === departmentNameFromDepDirectorReturn(this.role)[4])) {
                  this.reports.push(r) // все отчеты
                }

                if (r.facility)
                if (!(this.facilities.find(f => (f.name === r.facility.name)))
                     && ((this.role === 'Руководитель проектов') ? (r.user.name === this.profile) : true)) {
                  if (r.facility) {
                    this.facilities.push({
                      id: r.facility.id,
                      name: r.facility.name,
                      subFacilities: r.facility.subFacilities
                    }) // все по которым отчитывался руководитель
                    this.facilityNames.push(r.facility.name) // все по которым отчитывался руководитель
                  }
                }
              })

          if (this.role === 'Руководитель проектов') {
            this.reports = this.reports.filter(r => (this.facilities.find(f => ((r.facility ? r.facility.name : '') === f.name)))) // сортировка по объектам менеджеров
          }
          this.sortedReports = this.reports
          this.facilityNames.unshift('Все объекты')

        }
    )
    console.log('Прогрузил report')
    // Запрашиваем объекты
    // this.axios.get("api/facility").then(result => {
    //       result
    //           .data
    //           .forEach(f => {
    //             this.facilities.push({
    //               id: f.id,
    //               name: f.name,
    //               subFacilities: f.subFacilities
    //             }) // все отчеты
    //             this.facilityNames.push(f.name) // все отчеты
    //
    //           })
    //       this.facilityNames.unshift('Все объекты')
    //     }
    // )
    //console.log('Прогрузил facility')
  },
  methods: {
    selectFacility: function (facilityNameSelected) {
      console.log('Запустил selectFacility')
      this.byFacility = true
      this.facilityNameSelected = facilityNameSelected

      this.resultFilter()
      this.selectVariantsSubFacilities()
    },
    sortByFacility: function (reports) {
      console.log('Запустил sortByFacility')
      // Если выбраны все объекты
      if (this.facilityNameSelected === 'Все объекты') {
        // Выключаем фильтр по подобъекта когда выбраны Все объекты
        this.bySubFacility = false

        return this.sortedReportsByFacility = reports
      }

      if (this.byFacility) {
        return this.sortedReportsByFacility = reports.filter(r => (r.facility ? r.facility.name : '') === this.facilityNameSelected)}

    },
    selectSubFacility: function (subFacilityNameSelected) {
      console.log('Запустил selectSubFacility')
      this.bySubFacility = true
      this.subFacilityNameSelected = subFacilityNameSelected

      this.resultFilter()
    },
    sortBySubFacility: function (reports) {
      console.log('Запустил sortBySubFacility')
      // Если выбраны все подобъекты
      if (this.bySubFacility && this.subFacilityNameSelected === 'Все подобъекты') {
        return this.sortedReportsBySubFacility = reports
            .filter(r => ((r.facility ? r.facility.name : '') === this.facilityNameSelected))}

      if (this.bySubFacility) {
        return this.sortedReportsBySubFacility = reports
            .filter(r => ((r.facility.name === this.facilityNameSelected)
                && ((r.subFacility ? r.subFacility.name : '') === this.subFacilityNameSelected)))}
    },
    selectUser: function (userNameSelected) {
      console.log('Запустил selectUser')
      this.byUser = true
      this.userNameSelected = userNameSelected

      this.resultFilter()
    },
    sortByUser: function (reports) {
      console.log('Запустил sortByUser')
      // Если выбраны все работы
      if (this.userNameSelected === 'Все сотрудники') {
        return this.sortedReportsByUser = reports}

      if (this.byUser) {
        return this.sortedReportsByUser = reports.filter(r => r.user.name === this.userNameSelected)}

    },
    selectWork: function (workNameSelected) {
      console.log('Запустил selectWork')
      this.byWork = true
      this.workNameSelected = workNameSelected

      this.resultFilter()
    },
    sortByWork: function (reports) {
      console.log('Запустил sortByWork')
      // Если выбраны все работы
      if (this.workNameSelected === 'Все работы') {
        return this.sortedReportsByWork = reports}

      if (this.byWork) {
        return this.sortedReportsByWork = reports.filter(r => r.typeOfWork === this.workNameSelected)}

    },
    resultFilter: function () {
      console.log('Запустил resultFilter')
      var sorted = this.reports

      if (this.byFacility) sorted = this.sortByFacility(this.reports)

      if (this.byUser) sorted = this.sortByUser(sorted)

      if (this.byWork) sorted = this.sortByWork(sorted)

      if (this.byFacility && this.bySubFacility) sorted = this.sortBySubFacility(sorted)

      this.sortedReports = sorted

    },
    selectVariantsSubFacilities: function () {
      console.log('Запустил selectVariantsSubFacilities')
      this.subFacilities = []
      if (this.facilities.find(f => f.name === this.facilityNameSelected))
        this.facilities.find(f => f.name === this.facilityNameSelected)
            .subFacilities.forEach(s => this.subFacilities.push(s.name))
      this.subFacilities.unshift('Все подобъекты')
    },
    updateAllReports: function (month) {
      this.reports = []
      this.facilityNames = []
      this.facilities = []
      console.log('Запустил mounted')
      this.axios.get( "api/report").then(result => {
            result
                .data
                .forEach(r => {

                  if ((r.user.role === departmentNameFromDepDirectorReturn(this.role)[0])
                      || (r.user.role === departmentNameFromDepDirectorReturn(this.role)[1])
                      || (r.user.role === departmentNameFromDepDirectorReturn(this.role)[2])
                      || (r.user.role === departmentNameFromDepDirectorReturn(this.role)[3])
                      || (r.user.role === departmentNameFromDepDirectorReturn(this.role)[4])) {
                    this.reports.push(r) // все отчеты
                  }

                  if (!(this.facilities.find(f => (f.name === r.facility.name)))
                  && ((this.role === 'Руководитель проектов') ? (r.user.name === this.profile) : true)) {
                    this.facilities.push({
                      id: r.facility.id,
                      name: r.facility.name,
                      subFacilities: r.facility.subFacilities
                    }) // все по которым отчитывался руководитель
                    this.facilityNames.push(r.facility.name) // все по которым отчитывался руководитель
                  }
                })
            this.reports = this.reports.filter(r => (this.facilities.find(f => (r.facility.name === f.name)))) // без сортировки
            this.sortedReports = this.reports
            this.facilityNames.unshift('Все объекты')
            this.resultFilter()
          }
      )
      console.log('Прогрузил report')
    }
  }
}

</script>

<style scoped>

</style>

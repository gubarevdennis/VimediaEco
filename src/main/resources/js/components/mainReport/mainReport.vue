<template>
  <v-sheet v-if="this.role === 'Директор' || this.role === 'Диспетчер'">
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
            :items="subFacilities.map(s => s.name)"
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
            label="Сотрудник"
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
          'ПНР', 'Сервис','Авторский надзор', 'Менеджмент' , 'Отпуск по семейным обстоятельствам',
          'Отпуск без сохранения ЗП', 'Отпуск оплачиваемый', 'Больничный']"
        >
        </v-select>
      </v-col>
    </v-row>
  </v-sheet>

  <month :updateAllReports="updateAllReports"
         :reports="sortedReports"
         :facilityCoast="facilityCoast"
         :role="role"
  ></month>
</template>

<script>
import Month from "./month.vue";

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
      sortedReportsByUser: [],
      facilityCoast: 0
    }
  },
  mounted: function () {
    console.log('Запустил mounted')
    // // Запрашиваем отчеты
    // this.axios.get( "api/report").then(result => {
    //       result
    //           .data
    //           .forEach(r => {
    //             this.reports.push(r) // все отчеты
    //           })
    //       this.sortedReports = this.reports // без сортировки
    //     }
    // )

    // Запрашиваем отчеты
    this.axios.get( `api/report/month/0`).then(result => {
          result
              .data
              .forEach(r => {
                this.reports.push(r) // все отчеты
              })
      this.reports = this.reports.filter(r => r.user)
          this.sortedReports = this.reports // без сортировки
        }
    )

    console.log('Прогрузил report')
    // Запрашиваем объекты
    this.axios.get("api/facility").then(result => {
          result
              .data
              .forEach(f => {
                this.facilities.push({
                  id: f.id,
                  name: f.name,
                  subFacilities: f.subFacilities
                }) // все отчеты
                this.facilityNames.push(f.name) // все отчеты

              })
          this.facilityNames.unshift('Все объекты')
        }
    )
    console.log('Прогрузил facility')
    // Запрашиваем пользователей
    this.axios.get( "api/user").then(result => {
          result
              .data
              .forEach(u => {
                this.users.push(u.name) // все пользователи
              })
          this.users.unshift('Все сотрудники')
        }
    )
    console.log('Прогрузил user')
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
        this.facilityCoast = 0 // обнуляем стоимость работ объекта

        return this.sortedReportsByFacility = reports
      }


      // Статистика по конкретному объекту
      var fac = this.facilities.find(f => (f.name === this.facilityNameSelected))
      if (!this.bySubFacility)
        this.axios.get("api/report/facility/" + fac.id).then(result => {
              this.facilityCoast =
                  result
                      .data
                      .map(r => r.user ? r.user.salary ? r.hoursOfWorking * r.user.salary / 8 : 0 : 0)
                      .reduce((partialSum, a) => partialSum + a, 0)
            }
        )

      if (this.byFacility) {
        return this.sortedReportsByFacility = reports.filter(r => ((r.facility ? r.facility.name : '') === this.facilityNameSelected))}

    },
    selectSubFacility: function (subFacilityNameSelected) {
      console.log('Запустил selectSubFacility')
      this.bySubFacility = true
      this.subFacilityNameSelected = subFacilityNameSelected


      this.resultFilter()
    },
    sortBySubFacility: function (reports) {
      console.log('Запустил sortBySubFacility')

      // Статистика по конкретному подобъекту
      var sFac = this.subFacilities.find(f => (f.name === this.subFacilityNameSelected))
      this.axios.get("api/report/subFacility/" + sFac.id).then(result => {
            this.facilityCoast =
                result
                    .data
                    .map(r => r.user ? r.user.salary ? r.hoursOfWorking * r.user.salary / 8 : 0 : 0)
                    .reduce((partialSum, a) => partialSum + a, 0)
          }
      )

      // Если выбраны все подобъекты
      if (this.bySubFacility && this.subFacilityNameSelected === 'Все подобъекты') {
        return this.sortedReportsBySubFacility = reports
            .filter(r => ((r.facility ? r.facility.name : '') === this.facilityNameSelected))
      }

      if (this.bySubFacility) {
        return this.sortedReportsBySubFacility = reports
            .filter(r => ((r.facility.name === this.facilityNameSelected)
                && ((r.subFacility ? r.subFacility.name : '') === this.subFacilityNameSelected)))
      }
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
        return this.sortedReportsByUser = reports
      }

      if (this.byUser) {
        return this.sortedReportsByUser = reports.filter(r => r.user).filter(r => r.user.name === this.userNameSelected)
      }
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
        return this.sortedReportsByWork = reports.filter(r => r.job).filter(r => r.job.type === this.workNameSelected)}

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
            .subFacilities.forEach(s => this.subFacilities.push(s))
      this.subFacilities.unshift({name : 'Все подобъекты'}) // так как в subFacilities объекты а не имена, все подобъекты тоже добавил как фальш подобьект
    },
    updateAllReports: function (month) {
      this.reports = []
      console.log('Запустил mounted')
      // Запрашиваем отчеты
      this.axios.get( `api/report/month/${month}`).then(result => {
            result
                .data
                .forEach(r => {
                  this.reports.push(r) // все отчеты
                })

            this.reports = this.reports.filter(r => r.user)

            this.sortedReports = this.reports // без сортировки
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

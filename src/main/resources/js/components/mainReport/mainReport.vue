<template>
  <v-sheet>
    <v-row>

      <v-col>
        <br>
        <v-select
            label="Объект"
            variant="outlined"
            @update:modelValue="selectFacility"
            :items="facilityNames"
            :item-value="facilityNameSelected"
        >

        </v-select>
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
        <v-select
            variant="outlined"
            @update:modelValue="selectWork"
            :item-value="workNameSelected"
            label="Вид работ"
            :items="['Все работы', 'Монтаж','ПНР','Проект','Менеджмент']"
        >
        </v-select>
      </v-col>
    </v-row>
  </v-sheet>
  <month :reports="sortedReports" ></month>
</template>

<script>
import Month from "./month.vue";

const url = 'http://localhost:'
const port = '9000/'
//const url = 'http://reports.vimedia.ru/'

export default {
  name: "mainReport",
  components: {Month},
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
      sortedReports: []
    }
  },
  created: function () {
    // Запрашиваем отчеты
    this.axios.get(url+ port + "api/report").then(result => {
          result
              .data
              .forEach(r => {
                this.reports.push(r) // все отчеты
              })
          this.sortedReports = this.reports // без сортировки
        }
    )
    // Запрашиваем объекты
    this.axios.get(url+ port + "api/facility").then(result => {
          result
              .data
              .forEach(f => {
                this.facilities.push(f) // все отчеты
                this.facilityNames.push(f.name) // все отчеты

              })
          this.facilityNames.unshift('Все объекты')
        }
    )
  },
  methods: {
    selectFacility: function (facilityNameSelected) {
      this.byFacility = true
      this.facilityNameSelected = facilityNameSelected

      this.resultFilter()
      this.selectVariantsSubFacilities()
    },
    sortByFacility: function (reports) {
      // Если выбраны все объекты
      if (this.facilityNameSelected === 'Все объекты') {
        // Выключаем фильтр по подобъекта когда выбраны Все объекты
        this.bySubFacility = false

        return this.sortedReportsByFacility = reports
      }

      if (this.byFacility) {
        return this.sortedReportsByFacility = reports.filter(r => r.facility.name === this.facilityNameSelected)}

    },
    selectSubFacility: function (subFacilityNameSelected) {
      this.bySubFacility = true
      this.subFacilityNameSelected = subFacilityNameSelected

      this.resultFilter()
    },
    sortBySubFacility: function (reports) {
      // Если выбраны все подобъекты
      if (this.bySubFacility && this.subFacilityNameSelected === 'Все подобъекты') {
        return this.sortedReportsBySubFacility = reports
            .filter(r => (r.facility.name === this.facilityNameSelected))}

      if (this.bySubFacility) {
        return this.sortedReportsBySubFacility = reports
            .filter(r => ((r.facility.name === this.facilityNameSelected)
                && ((r.subFacility ? r.subFacility.name : '') === this.subFacilityNameSelected)))}
    },
    selectWork: function (workNameSelected) {
      this.byWork = true
      this.workNameSelected = workNameSelected

      this.resultFilter()
    },
    sortByWork: function (reports) {
      // Если выбраны все работы
      if (this.workNameSelected === 'Все работы') {
        return this.sortedReportsByWork = reports}

      if (this.byWork) {
        return this.sortedReportsByWork = reports.filter(r => r.typeOfWork === this.workNameSelected)}

    },
    resultFilter: function () {

      if (this.byFacility && !this.byWork) this.sortedReports = this.sortByFacility(this.reports)

      if (this.byWork && !this.byFacility) this.sortedReports = this.sortByWork(this.reports)

      if (this.byFacility && this.bySubFacility && !this.byWork) this.sortedReports = this.sortBySubFacility(this.reports)

      if (this.byFacility && this.byWork)
        this.sortedReports = this.sortByWork(this.sortByFacility(this.reports))

      if (this.byFacility && this.bySubFacility && this.byWork)
        this.sortedReports = this.sortBySubFacility(this.sortByWork(this.sortByFacility(this.reports)))
    },
    selectVariantsSubFacilities: function () {
      this.subFacilities = []
      if (this.facilities.find(f => f.name === this.facilityNameSelected))
        this.facilities.find(f => f.name === this.facilityNameSelected)
            .subFacilities.forEach(s => this.subFacilities.push(s.name))
      this.subFacilities.unshift('Все подобъекты')
    }
  }
}


</script>

<style scoped>

</style>

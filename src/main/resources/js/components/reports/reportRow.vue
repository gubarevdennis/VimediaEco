<template>
  <v-sheet>
    <v-row>
      <v-col>
        <v-card-title class="text-green-accent-4">
          {{report.subFacility ? report.subFacility.name + ' - ' : ''}} {{report.hoursOfWorking}} ч
        </v-card-title>
        <v-card-text>
          {{report.typeOfWork}} - {{report.text}}
        </v-card-text>
      </v-col>
      <v-spacer></v-spacer>
      <v-col cols="auto" align-self="center">
        <v-card-text>
          {{report.reportDay}}
        </v-card-text>
      </v-col>
      <v-col cols="auto" align="end" align-self="center">
        <v-btn @click="edit" icon="mdi-file-edit">  </v-btn>
        <v-btn @click="del" icon="mdi-delete">  </v-btn>
      </v-col>
    </v-row>
  </v-sheet>
</template>

<script>


// const url = 'http://localhost:'
// const port = '9000/'
// const url = 'http://reports.vimedia.ru/'
// const port = ''

export default {
  props: ['report', 'editReport','reports', 'editReportSelect'], // получаем переменную facility
  methods: {
    edit: function () {
      this.editReport(this.report);
      this.editReportSelect(this.report);
    },
    del: function () {
      this.axios.delete(`api/report/${this.report.id}`).then(result => {
        if (result.status === 200) {
          this.reports.splice(this.reports.indexOf(this.report), 1) // удаления объекта из коллекции
        }
      })
    }
},
  computed: {
    facilityNameForReportRow: function () {
      if (this.report.facility) return this.report.facility.text // возвразает не нулевое значение
    },
    userNameForReportRow: function () {
      if (this.report.user) return this.report.user.name // возвращает не нулевое значение
      console.log(this.report)
    }
  },
}
</script>

<style>

</style>

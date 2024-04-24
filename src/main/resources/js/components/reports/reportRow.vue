<template>
  <v-sheet>
    <v-row>
      <v-col>
        <v-card-title class="text-green-accent-4">
          {{report.subFacility ? report.subFacility.name + ' - ' : ''}} {{report.hoursOfWorking}} ч
        </v-card-title>
        <v-card-text>
          {{report.job && report.job.name ? report.job.name : report.typeOfWork}} - {{report.text}}
        </v-card-text>
      </v-col>
      <v-spacer></v-spacer>
      <v-col cols="auto" align-self="center">
        <v-card-text>
          {{report.reportDay}}
        </v-card-text>
      </v-col>

      <v-overlay
          v-model="showReportEdit"
          class="align-center justify-center"
          scroll-strategy="block"
          align="center"
      >
        <v-btn icon="mdi-close" @click="showReportEdit = !showReportEdit" :ripple="false"></v-btn>
        <br>
        <br>
        <report-edit
            :report="report"
            :editShowReportEdit="editShowReportEdit"
            :sortedReports="sortedReports"
            :editReportInReports="editReportInReports">
        </report-edit>
      </v-overlay>

      <v-col cols="auto" align="end" align-self="center">
        <v-btn @click="showReportEdit = !showReportEdit" icon="mdi-file-edit">  </v-btn>
        <v-btn @click="del" icon="mdi-delete">  </v-btn>
      </v-col>
    </v-row>
  </v-sheet>
</template>

<script>

import reportEdit from "./reportEdit.vue";

// const url = 'http://localhost:'
// const port = '9000/'
// const url = 'http://reports.vimedia.ru/'
// const port = ''

export default {
  props: ['report', 'reports', 'sortedReports'],
  components: {
    reportEdit
  },
  data() {
    return {
      showReportEdit: false
    }
  },
  methods: {
    del: function () {
      this.axios.delete(`api/report/${this.report.id}`).then(result => {
        if (result.status === 200) {
          this.reports.splice(this.reports.indexOf(this.report), 1) // удаления объекта из коллекции
        }
      })
    },
    editShowReportEdit: function () {
      this.showReportEdit = false
    },
    editReportInReports: function(data) {
      this.reports.forEach((report, idx) => {
        if (report.id ==this.report.id)
          this.reports.splice(idx, 1, data);
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

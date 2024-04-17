<template>
  <v-card
      height="auto"
      width="auto"
      align="start"
  >
    <template v-slot:title>
      <span class="font-weight-black">{{report.facility.name}}</span>
    </template>

    <v-card-text v-show="isError">
      <r-row>
        <v-col style="color: #ffffff; background-color: #c15151; font-weight: bold; border-radius: 5px">
          Отчетный день в компании Vimedia Group составляет 8 часов
        </v-col>
      </r-row>
    </v-card-text>

    <v-card-text v-show="isErrorFields">
      <r-row>
        <v-col style="color: #ffffff; background-color: #c15151; font-weight: bold; border-radius: 5px">
          Заполните поле работы
        </v-col>
      </r-row>
    </v-card-text>

    <v-card-text>
      <v-row v-if="report.subFacility != null">
        <v-col>
          <div style="font-weight: bold">Подобъект</div>
          <div>{{subFacilityNameAttr}}</div>
        </v-col>
        <v-col>
          <v-select
              label="Подобъект"
              density="compact"
              :items="subFacilities.map(el => el.name)"
              :item-value="subFacilityNameAttr"
              v-model="subFacilityNameAttr"
              @update:modelValue="editSubFacFunc"
          ></v-select>
        </v-col>
      </v-row>

      <v-row>
        <v-col>
          <div style="font-weight: bold">Длительность в часах</div>
          <div>{{hoursOfWorkingAttr}} ч</div>
        </v-col>
        <v-col>
          <v-select
              label="Длительность в часах"
              density="compact"
              :items="['1','2','3','4','5','6','7', '8']"
              :item-value="hoursOfWorkingAttr"
              v-model="hoursOfWorkingAttr"
              @update:modelValue="showSaveBtnFunc"
          ></v-select>
        </v-col>
      </v-row>

      <v-row >
        <v-col>
          <div style="font-weight: bold">Название работы</div>
          <div>{{jobNameAttr}}</div>
        </v-col>
        <v-col>
          <v-select
              label="Название работы"
              density="compact"
              :items="jobs.map(el => el.name)"
              :item-value="jobNameAttr"
              v-model="jobNameAttr"
              @update:modelValue="showSaveBtnFunc"
          ></v-select>
        </v-col>
      </v-row>

      <v-btn color="green" v-show="showSaveBtn" @click="postEditedReport">Сохранить</v-btn>

    </v-card-text>



  </v-card>
</template>

<script>

export default {
  name: "reportEdit",
  props: ['report', 'editShowReportEdit', 'sortedReports', 'editReportInReports'],
  data() {
    return {
      showSaveBtn: false,
      subFacilities: [],
      jobs: [],
      isEditedSubFacility: false,

      hoursOfWorkingAttr: 0,
      subFacilityNameAttr : '',
      jobNameAttr: '',

      isError: false,
      isErrorFields: false
    }
  },
  mounted() {
    console.log(this.report.subFacility)
    console.log('this repoooort')
    console.log(this.report)
    this.checkFullHours()

    this.hoursOfWorkingAttr = this.report.hoursOfWorking
    if(this.report.subFacility)
      this.subFacilityNameAttr = this.report.subFacility.name
    if(this.report.job)
      this.jobNameAttr = this.report.job.name

    this.getSubFacilities()
    this.getJobs(this.isEditedSubFacility)
  },
  methods: {
    showSaveBtnFunc: function () {
      this.showSaveBtn = true
    },
    getSubFacilities: function (){
      this.subFacilities = []
      this.axios.get("/api/facility/" + this.report.facility.id).then(result =>
          result
              .data.subFacilities
              .forEach(r => {
                    this.subFacilities.push(r)
                  }
              )
      )
    },
    getJobs: function (isEditedSubFacility){
      this.jobs = []
      if(this.report.subFacility == null) {
        console.log('get facil jobs')
        this.axios.get("api/job/facility/" + this.report.facility.id).then(result =>
            result
                .data
                .forEach(job => {
                      this.jobs.push(job)
                    }
                )
        )
      } else {

        let findSubFacility

        if(this.isEditedSubFacility)
          findSubFacility = this.subFacilities.find(el => el.name == this.subFacilityNameAttr)
        else
          findSubFacility = this.report.subFacility

        this.axios.get("api/job/subFacility/" + findSubFacility.id).then(result => {
          result
              .data
              .forEach(job => {
                    this.jobs.push(job)
                  }
              )
          if (isEditedSubFacility) {
            if (this.jobs[0] != null) {
              this.jobNameAttr = this.jobs[0].name
            }
            else {
              this.jobNameAttr = ''
            }
          }
          this.isEditedSubFacility = false

        })

      }
    },
    editSubFacFunc: function () {
      this.showSaveBtn = true
      this.isEditedSubFacility = true

      this.getJobs(this.isEditedSubFacility)

    },
    postEditedReport: function () {

      if(!this.checkFullHours(this.hoursOfWorkingAttr))
        this.isError = true
      else
        this.isError = false

      if(!this.jobNameAttr)
        this.isErrorFields = true
      else
        this.isErrorFields = false

      if(!this.isErrorFields && !this.isError) {
        let newSubFacility = this.subFacilities.find(el => el.name == this.subFacilityNameAttr)
        let newJob = this.jobs.find(el => el.name == this.jobNameAttr)
        let newHoursOfWork = this.hoursOfWorkingAttr
        let reportForSend = {};

        console.log('job id')
        console.log(newJob)

        reportForSend = {
          id: this.report.id,
          facility: {id: this.report.facility.id, name: this.report.facility.name},
          subFacility: {name: newSubFacility.name},
          typeOfWork: newJob.name,
          job: {id: parseInt(newJob.id) },
          text: this.report.text,
          hoursOfWorking: newHoursOfWork,
          reportDay: this.report.reportDay
        }


        this.axios.post('api/report/' + this.report.id, reportForSend).then(data => {
          if (data.data === '') {
            this.isError = true
          } else {

            this.isError = false
            this.isErrorFields = false
            this.editShowReportEdit()
            this.editReportInReports(data.data)
          }
        })
      }


    },
    checkFullHours: function (newHours) {
      let currentHoursSum = 0
      this.sortedReports.forEach(el => {
        if(this.report.id != el.id)
          currentHoursSum += parseInt(el.hoursOfWorking)
      })

      if(currentHoursSum + parseInt(newHours) > 8)
        return false
      return true
    }
  }
}
</script>

<style scoped>

</style>

<template>
  <v-col align-content="center" justify="center" style="height: 25px">
    <v-sheet

        align="start"
        rounded="lg"
        style="background-color: dimgray"
        width="180px"
        height="22px"
    >
      <v-sheet
          align="center"
          label
          rounded="lg"
          v-bind:color="this.color"
          v-bind:width="report.hoursOfWorking * 22.5"
      >
        <v-row align="center" justify="center"
               @click = "getCurrentJob"
               style="background-color: rgba(0,0,0,0); width: 200px;  font-size: 15px; vertical-align: center">
          {{report.user ? report.user.name : ''}}
          <v-overlay
              activator="parent"
              location-strategy="connected"
              scroll-strategy="close"
          >
            <v-card
                rounded="lg"
                width="200px"
                v-bind:color="this.color"
            >
              <v-card-text >
                <div style="font-weight: bold;color: #0B0B0B">
                  {{report.facility.name}}
                </div>
                <div style="font-weight: bold;color: #0B0B0B">
                  {{report.subFacility ? report.subFacility.name : ''}}
                </div>
                <div style="color: #0B0B0B;">
                  {{ currentJobName }}
                </div>
                <div style="color: #0B0B0B">
                  {{ report.hoursOfWorking }} ч
                </div>
                <div v-if="report.text !== 'Отчет сформирован без комментариев'">
                  <br>
                  {{report.text}}
                </div>

                <div style="color: #888888; border: 1px solid; padding-left: 8px; background-color: #fff; margin-top: 1rem; border-radius: 5px">Type of work
                  <div style="color: #0B0B0B; ">
                    {{ report.typeOfWork ? report.typeOfWork : '' }}
                  </div>
                </div>

              </v-card-text>

              <v-autocomplete
                  label="Job name"
                  @update:modelValue="selectWorkingType"
                  :model-value="currentJobName"
                  :items="jobs.map(j => j.name)"
                  :item-value= jobType
              ></v-autocomplete>

              <v-row justify="center" align-content="center" style="margin-bottom: 5px">
                <v-btn v-if="showConfirmBtn" color="green"  @click="editJobType" > Сохранить </v-btn>
              </v-row>

            </v-card>
          </v-overlay>
        </v-row>
      </v-sheet>
    </v-sheet>
  </v-col>
</template>

<script>

export default {
  props: ['report'],
  //color: '',
  data() {
    return {
      jobs: [],
      jobType: '',
      showConfirmBtn: false,
      reportForSend: {},
      color: '',
      currentJobName: ''
    }
  },
  mounted: function () {
    this.color = this.report.job ? this.report.job.color : '#fff'
    this.currentJobName = this.report.job ? this.report.job.name : ''

  },
  methods: {
    selectWorkingType: function (jobType) {

      this.jobType = jobType
      this.showConfirmBtn = true
    },
    editJobType: function () {
      var foundJob = this.jobs.find(job => job.name === this.jobType)


      Object.assign(this.reportForSend, this.report)
      this.reportForSend.job = {id: foundJob.id }
      console.log(this.reportForSend);

      this.axios.post("api/report/" + this.report.id, this.reportForSend).then(data => {
        console.log(data)
        this.color = data.data.job ? data.data.job.color : '#ffffff'
        this.currentJobName = data.data.job ? data.data.job.name : ''
        this.showConfirmBtn = false
        //this.report = data.data
      })

    },
    getCurrentJob: function () {
      this.jobs = []
      if (this.report.subFacility) {
        this.axios.get('api/job/subFacility/' + this.report.subFacility.id).then(result => {
          if(result.data)
              result
                  .data
                  .forEach(j => {
                        this.jobs.push(j);
                      }
                  )
            }
        )
      } else {
        this.axios.get('api/job/facility/' + this.report.facility.id).then(result => {
          if(result.data)
              result
                  .data
                  .forEach(j => {
                        this.jobs.push(j)
                      }
                  )
            }
        )
      }
    }
  }
}
</script>

<style scoped>
.ma-2{
  width: 180px;
  border-radius: 1px;

}
</style>

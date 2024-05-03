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
                width="300px"
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
                <div v-show ="isError" style="background-color: #ca4141; padding: 4%; border-radius: 10px">
                  <div style="font-weight: bold; color: #1a1515;">Ошибка. Не больше 8 часов.</div>
                </div>
                <div style="font-weight: bold;color: #0B0B0B">Количество часов: </div>
                <input size="5" style="text-align:center"  type="text" @input="showEditConfirmBtnFunc" v-model="report.hoursOfWorking" />ч
                <div v-if="role == 'Директор'" style="font-weight: bold;color: #0B0B0B">Стоимость:</div>
                <input v-if="role == 'Директор'" size="5" style="text-align:center"  type="text" @input="showEditConfirmBtnFunc" v-model="report.cost" />р
                <br>
                <div style="width: 100%; font-weight: bold;"> Текст отчета:</div>
                <input size="30" style="text-align:center"  type="text" @input="showEditConfirmBtnFunc" v-model="report.text" />
                <div style="color: #888888; border: 1px solid; padding-left: 8px; background-color: #fff; margin-top: 1rem; border-radius: 5px">Type of work
                  <div style="color: #0B0B0B; ">
                    {{ report.typeOfWork ? report.typeOfWork : '' }}
                  </div>
                </div>

              <div v-if="showEditConfirmBtn">
                <br>
                <v-btn v-show="report.job ? (report.job.nds < 1) : true" color="green"  @click="editJobType" > Сохранить </v-btn>
                <v-btn v-show="report.job ? (report.job.nds < 1) : true" color="red"  @click="showEditConfirmBtn = !showEditConfirmBtn" > Отменить </v-btn>
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
                <v-btn v-show="report.job ? (report.job.nds < 1) : true" v-if="showConfirmBtn" color="green"  @click="PostJobType" > Сохранить </v-btn>
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
  props: ['report', 'role'],
  //color: '',
  data() {
    return {
      jobs: [],
      jobType: '',
      showConfirmBtn: false,
      reportForSend: {},
      color: '',
      currentJobName: '',
      isError: false,

      showEditConfirmBtn: false
    }
  },
  mounted: function () {
    this.color = this.report.job ? this.report.job.color : '#fff'
    this.currentJobName = this.report.job ? this.report.job.name : ''

  },
  methods: {
    showEditConfirmBtnFunc: function () {
      this.showEditConfirmBtn = true
    },
    selectWorkingType: function (jobType) {

      this.jobType = jobType
      this.showConfirmBtn = true
    },
    PostJobType: function () {
      var foundJob = this.jobs.find(job => job.name === this.jobType)

      this.reportForSend = {
        id: this.report.id,
        cost: this.report.cost,
        facility: {id: this.report.facility.id, name: this.report.facility.name},
        subFacility: {name: this.report.subFacility ? this.report.subFacility : null},
        typeOfWork: this.report.typeOfWork,
        job: {id: parseInt(foundJob.id) },
        text: this.report.text,
        hoursOfWorking: this.report.hoursOfWorking,
        reportDay: this.report.reportDay
      }

      this.axios.post('api/report/' + this.report.id, this.reportForSend).then(data => {

        this.color = data.data.job ? data.data.job.color : '#ffffff'
        this.currentJobName = data.data.job ? data.data.job.name : ''
        this.showConfirmBtn = false
        //this.report = data.data
      })
    },
    editJobType: function () {
      if (this.report.hoursOfWorking > 8)
          this.isError = true
      else {
        let job_id = this.report.job ? this.report.job.id : null
        if(job_id) {
          this.reportForSend = {
            id: this.report.id,
            cost: this.report.cost,
            facility: {id: this.report.facility.id, name: this.report.facility.name},
            subFacility: {name: this.report.subFacility ? this.report.subFacility : null},
            typeOfWork: this.report.typeOfWork,
            job: {id: job_id},
            text: this.report.text,
            hoursOfWorking: this.report.hoursOfWorking,
            reportDay: this.report.reportDay
          }
        } else {
          this.reportForSend = {
            id: this.report.id,
            cost: this.report.cost,
            facility: {id: this.report.facility.id, name: this.report.facility.name},
            subFacility: {name: this.report.subFacility ? this.report.subFacility : null},
            typeOfWork: this.report.typeOfWork,
            text: this.report.text,
            hoursOfWorking: this.report.hoursOfWorking,
            reportDay: this.report.reportDay
          }
        }
        this.axios.post('api/report/' + this.report.id, this.reportForSend).then(data => {

          this.showEditConfirmBtn = false
          this.isError = false
          //this.report = data.data
        })
      }

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

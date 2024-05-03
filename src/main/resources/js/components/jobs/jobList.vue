<template>
  <html style="max-height: 80vh; min-width: 40vh">
  <v-sheet
      rounded="lg"
      contained
      align="center"
  >
    <v-card-title>
      {{facility.name}} {{subFacility.name}}
    </v-card-title>

    <v-container>
      <v-row>
        <v-col cols="12" sm="4">
          <v-list>
            <v-list-item
            v-for="job in this.jobs"
            :key="job.id"
            @click="selectJob(job)">
              <v-card :color="job.color" class="pa-2">{{job.name}}</v-card>
            </v-list-item>
          </v-list>
          <br>
          <v-btn color="yellow" @click="turnOverlayCopyAndAddNewJob(overlayCopy)" :showConfirmCopyTool="showConfirmCopyTool"
          > Добавить работы</v-btn>
        </v-col>
        <v-col cols="12" sm="8">
          <div v-if="selectedJob">
            <job-row
                :reports="reports.filter(r => r.job).filter(r => r.job.id === selectedJob.id)"
                :job="selectedJob"
                :editJob="editJob"
                :users="users"
                :assignedUsers="assignedUsers"
                :profile="profile" :role="role" :profileId="profileId"
                :updateDeletedJob="updateDeletedJob"
            >
            </job-row>
          </div>
        </v-col>
      </v-row>
    </v-container>

<!--    <v-col >-->
<!--      <v-row justify="center" align="start">-->
<!--          <div v-for="job in this.jobs" :key="job.id">-->
<!--                <job-row-->
<!--                    :reports="reports.filter(r => r.job).filter(r => r.job.id === job.id)"-->
<!--                    :job="job"-->
<!--                    :editJob="editJob"-->
<!--                    :users="users"-->
<!--                    :assignedUsers="assignedUsers"-->
<!--                    :profile="profile" :role="role" :profileId="profileId"-->
<!--                    :updateDeletedFob="updateDeletedFob"-->
<!--                >-->
<!--                </job-row>-->
<!--            <br>-->
<!--            <br>-->
<!--&lt;!&ndash;            <v-row justify="center" align-content="center" style="margin-bottom: 5px">&ndash;&gt;-->
<!--&lt;!&ndash;              <v-btn v-if="showConfirmBtn" color="green"  @click="edit" > Применить </v-btn>&ndash;&gt;-->

<!--&lt;!&ndash;              <v-btn v-if="showConfirmBtn" color="red"  @click="hideConfirmBtnFunc" > Отмена </v-btn>&ndash;&gt;-->
<!--&lt;!&ndash;            </v-row>&ndash;&gt;-->
<!--          </div>-->
<!--      </v-row>-->
<!--    </v-col>-->
<!--    <br>-->
<!--    <v-btn color="yellow" @click="turnOverlayCopyAndAddNewJob(overlayCopy)" :showConfirmCopyTool="showConfirmCopyTool"-->
<!--    > Добавить работы</v-btn>-->

    <br>
    <br>
  </v-sheet>
  </html>
</template>

<script>
import jobRow from "./jobRow.vue";

export default {
  name: "jobList",
  components: {jobRow},
  props: ['profile', 'profileId', 'subFacility', 'facility', 'role', 'tool', 'editTool',
    'toolSets', 'categories', 'deleteTool', 'copyTool', 'closeDescriptionToolByDeleteConfirm',
    'closeDescriptionToolByCopyConfirm', 'users', 'usernames'],
  data() {
    return {
      imageEditButton: false,
      toolSetNameSelected: '',
      toolCategoryNameSelected: '',
      showConfirmBtn: false,
      toolSerialOnOverlay: '',
      toolSetNames: [],
      showConfirmDeleteTool: false,
      showConfirmCopyTool: false,
      overlayDel: false,
      overlayCopy: false,
      toolCategoryNames: [],
      inventoryEvents: [],
      jobs: [],
      assignedUsers: [],
      job: '',
      reports: [],
      userNameSelected: '',

      selectedJob: ''
    }
  },
  mounted() {
    // ['Электроинструмент', 'Абразивный инструмент', 'Измерительный инструмент',
    //   'Слесарно-монтажный инструмент', 'Без категории']
    // console.log(this.toolSetNameSelected)

    if (this.facility) {
      this.axios.get("api/job/facility/" + this.facility.id).then(result => {
          result
              .data
              .forEach(job => {
                    this.jobs.push(job)
                  }
              )
          this.selectedJob = this.jobs ? this.jobs[0] : ''
        }
      )

      this.axios.get("api/report/facility/" + this.facility.id).then(result =>
          result
              .data
              .forEach(r => {
                    this.reports.push(r)
                  }
              )
      )
    } else if (this.subFacility) {
      this.axios.get("api/job/subFacility/" + this.subFacility.id).then(result => {
          result
              .data
              .forEach(job => {
                    this.jobs.push(job)
                  }
              )
          this.selectedJob = this.jobs ? this.jobs[0] : ''
        }
      )

      this.axios.get("api/report/subFacility/" + this.subFacility.id).then(result =>
          result
              .data
              .forEach(r => {
                    this.reports.push(r)
                  }
              )
      )
    }

  },
  methods: {
    addNewJobByFacility: function (facility) {
      this.axios.post("api/job", {name: 'Пустая работа', type: 'Пустая работа',
          budget: 0,
          facility: {id : facility.id}}).then(result => {
      if (result.status === 200) {
        this.jobs.push(result.data)
        this.selectJob(result.data)
      } else {

        }
      })
    },
    addNewJobBySubFacility: function (subFacility) {
      this.axios.post("api/job", {name: 'Пустая работа', type: 'Пустая работа',
        budget: 0,
        subFacility: {id : subFacility.id}}).then(result => {
      if (result.status === 200) {
        this.jobs.push(result.data)
        this.selectJob(result.data)
      } else {

      }
      })
    },
    // assign: function () {
    //   var user = this.users.filter(u => (u.name === this.userNameSelected));
    //
    //   this.axios.put(`api/job/${this.job.id}`, this.job).then(result => {
    //     if (result.status === 200) {
    //       this.editJob(result.data)
    //       this.showConfirmBtn = false
    //     } else {
    //       this.showConfirmBtn = false
    //     }
    //   })
    // },
    userNameSelect: function (userNameSelected) {
      this.userNameSelected = userNameSelected;
    // На всякий случай
    //   <v-autocomplete
    //       density="compact"
    //       label="Выбрать сотрудника"
    //       variant="solo"
    //     @update:model-value="userNameSelect"
    // :items="users.map(u => u.name)"
    // :item-value="userNameSelected"
    // >
    // </v-autocomplete>
    //   <v-col  align="center" cols="auto">
    //     <v-btn  v-if="this.role === 'admin'
    //     || this.role === 'HR'
    //     || this.role === 'Директор'
    //     || this.role === 'Диспетчер' "
    //     @click="assign"> Назначить ответственного за обьект </v-btn>
    // </v-col>
    },
    turnOverlayCopyAndAddNewJob: function (overlayCopy) {
      this.overlayCopy = !overlayCopy;
      if (this.facility) {
        this.addNewJobByFacility(this.facility)
      } else {
        this.addNewJobBySubFacility(this.subFacility)
      }
    },
    hideConfirmBtnFunc: function () {
      this.showConfirmBtn=false
      this.toolSetNameSelected = ''
    },
    showConfirmDeleteToolFunc: function (){
      this.closeDescriptionToolByDeleteConfirm()
      this.overlayDel=false;
    },
    showConfirmCopyToolFunc: function (){
      this.closeDescriptionToolByCopyConfirm()
      this.overlayCopy=false;
    },
    editJob: function (job) {
      // Редактируем инструмент в основных данных
      this.jobs.splice(
          this.jobs.findIndex(j => j.id === job.id),
          1,
          job
      )
      //console.log(this.jobs)
    },
    updateDeletedJob: function (){
      this.jobs = []
      this.axios.get("api/job/facility/" + this.facility.id).then(result =>
          result
              .data
              .forEach(job => {
                    this.jobs.push(job)
                  }
              )
      )
      this.selectedJob = this.jobs ? this.jobs[0] : ''
    },
    selectJob: function (job) {
      this.selectedJob = job
    }
  }
}
</script>

<style scoped>

</style>

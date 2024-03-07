<template>
  <html style="max-height: 80vh; max-width: 100vh">
  <v-sheet
      rounded="lg"
      contained
      align="center"
  >
    <v-card-title>
      {{facility.name}} {{subFacility.name}}
    </v-card-title>
    <v-col >
      <v-row justify="center" align="start">
          <div v-for="job in this.jobs" :key="job.id">
                <job-row
                    :reports="reports.filter(r => r.typeOfWork === job.name)"
                    :job="job"
                    :editJob="editJob"
                    :users="users"
                    :assignedUsers="assignedUsers"
                    :profile="profile" :role="role" :profileId="profileId"
                >
                </job-row>
            <v-row justify="center" align-content="center" style="margin-bottom: 5px">
              <v-btn v-if="showConfirmBtn" color="green"  @click="edit" > Применить </v-btn>

              <v-btn v-if="showConfirmBtn" color="red"  @click="hideConfirmBtnFunc" > Отмена </v-btn>
            </v-row>
          </div>
      </v-row>
    </v-col>
    <br>
    <v-btn color="yellow" @click="turnOverlayCopyAndAddNewJob(overlayCopy)" :showConfirmCopyTool="showConfirmCopyTool"
    > Добавить работы</v-btn>

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
      userNameSelected: ''
    }
  },
  mounted() {
    // ['Электроинструмент', 'Абразивный инструмент', 'Измерительный инструмент',
    //   'Слесарно-монтажный инструмент', 'Без категории']
    // console.log(this.toolSetNameSelected)

    if (this.facility) {
      this.axios.get("api/job/facility/" + this.facility.id).then(result =>
          result
              .data
              .forEach(job => {
                    this.jobs.push(job)
                  }
              )
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
      this.axios.get("api/job/subFacility/" + this.subFacility.id).then(result =>
          result
              .data
              .forEach(job => {
                    this.jobs.push(job)
                  }
              )
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
            this.axios.post("api/job", { facility: {id : facility.id}}).then(result => {
        if (result.status === 200) {
          this.jobs.push(result.data)
        } else {

        }
      })
    },
    addNewJobBySubFacility: function (subFacility) {
      this.axios.post("api/job", { subFacility: {id : subFacility.id}}).then(result => {
        if (result.status === 200) {
          this.jobs.push(result.data)
        } else {

        }
      })
    },
    assign: function () {
      var user = this.users.filter(u => (u.name === this.userNameSelected));

      this.axios.put(`api/job/${this.job.id}`, this.job).then(result => {
        if (result.status === 200) {
          this.editJob(result.data)
          this.showConfirmBtn = false
        } else {
          this.showConfirmBtn = false
        }
      })
    },
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
      console.log(this.jobs)
    },
  }
}
</script>

<style scoped>

</style>

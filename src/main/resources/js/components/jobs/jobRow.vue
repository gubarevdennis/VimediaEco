<template>
  <v-sheet width="320px" border>
    <div style="color: #0B0B0B" align="center" >
      <v-autocomplete
          density="compact"
          label="Название работ"
          variant="solo"
          @update:model-value="jobNameSelect"
          v-model="job.name"
          :items="jobNames"
          :item-value="jobNameSelected"
      >
      </v-autocomplete>
      <div style="font-weight: bold;color: #0B0B0B"> Стоимость работ: &nbsp</div>
      <input size="5" style="text-align:center"  type="text" @change="showConfirmBtnFunc" v-model="job.budget" /> р
      <div style="font-weight: bold;color: #0B0B0B"> Маржинальность: &nbsp</div>
      <input size="1" style="text-align:center"  type="text" @change="showConfirmBtnFunc" v-model="job.marginPercentage" /> %
      <div style="font-weight: bold;color: #0B0B0B"> Бонусная база: &nbsp</div>
      <input size="1" style="text-align:center" type="text" @change="showConfirmBtnFunc" v-model="job.bonus" /> %
      <div style="font-weight: bold;color: #0B0B0B"> Текущий бонус за объект: &nbsp</div>
      {{ calculateAllBonusMoney() }} р
      <div v-if="assignedUsers[0]" style="font-weight: bold;color: #0B0B0B"> Ответственные сотрудники: &nbsp</div>
      <div v-for="(assignedUser, i) in assignedUsers"
           :key="i">
        <div v-if=" assignedUser.role.split(' ')[0] !== 'Руководитель'">
        {{ i+1 }}) {{ assignedUser.name }} -
          {{ calculateAllHours() === 0 ?  0 : Math.round(calculateAllBonusMoney()  * calculateIndividualHours(assignedUser) * 0.8 / calculateAllHours())}} р
        за {{calculateIndividualHours(assignedUser)}} ч из {{calculateAllHours()}} ч
          <v-btn
                  @click="del(assignedUser)" icon="mdi-delete">  </v-btn>
        </div>
        <div v-if=" assignedUser.role.split(' ')[0] === 'Руководитель'">
          {{ i+1 }}) {{ assignedUser.name }} - {{ Math.round(calculateAllBonusMoney() * 0.1) }} р
          за {{calculateIndividualHours(assignedUser)}} ч из {{calculateAllHours()}} ч
          <v-btn
                  @click="del(assignedUser)" icon="mdi-delete">  </v-btn>
        </div>
      </div>
      <v-autocomplete
          density="compact"
          label="Добавить сотрудника"
          variant="solo"
          @update:model-value="userNameSelect"
          :items="users.map(u => u.name)"
          :item-value="userNameSelected"
      >
      </v-autocomplete>
      <div style="margin-top: 5px">
        <v-btn v-if="showConfirmBtn" color="green"  @click="edit" > Применить </v-btn>
        <v-btn v-if="showConfirmBtn" color="red"  @click="hideConfirmBtnFunc" > Отмена </v-btn>
      </div>
    </div>
  </v-sheet>
</template>

<script>
export default {
  name: "jobRow",
  props: ['profile', 'role', 'tool', 'rowInputText' , 'editJob', 'rowInputType', 'job', 'users', 'reports', 'profileId'],
  data() {
    return {
      imageEditButton: false,
      facilityNameSelected: '',
      showConfirmBtn: false,
      toolInfo: '',
      toolEdit: '',
      jobNameSelected:'',
      jobNames: ['Черновой монтаж', 'Чистовой монтаж','Шефмонтаж',  'Сборка щитов',
        'Концептуальное проектирование', 'Рабочее проектирование', 'Расключение шкафов',
        'ПНР', 'Авторский надзор', 'Другие работы' ],
      userNameSelected: '',
      assignedUsers: [],
      bonusMoney: '',
      allHours: 0,
      individualHours: 0,
    }
  },
  mounted() {
    this.toolInfo = this.rowInputText
    console.log(this.rowInputText)

    this.updateAssignedUsers(this.job)

  },
  methods: {
    showConfirmBtnFunc: function () {
      this.showConfirmBtn=true
    },
    jobNameSelect: function (jobNameSelected) {
      this.jobNameSelected = jobNameSelected;
      this.showConfirmBtn=true
    },
    userNameSelect: function (userNameSelected) {
      this.userNameSelected = userNameSelected;
      var user = this.users.filter(u => (u.name === userNameSelected));
      if (this.job.users)
        this.job.users.push({id: user[0].id});
      this.showConfirmBtn = true
    },
    hideConfirmBtnFunc: function () {
      this.showConfirmBtn = false
      this.toolInfo = this.rowInputText
    },
    updateAssignedUsers: function (job) {
      if (job) {
        this.assignedUsers = []
        if (job.users)
          if (job.users[0]) {
            job.users.forEach(u => {
              this.assignedUsers.push(u)
            })
          }
      }
    },
    calculateAllBonusMoney: function () {
      return Math.round(this.job.budget -
          this.reports
              .map(r => r.hoursOfWorking * r.user.salary/8)
              .reduce((partialSum, a) => partialSum + a, 0))
          * (this.job.marginPercentage/100)
          * (this.job.bonus/100);
    },
    edit: function () {
      this.axios.put(`api/job/${this.job.id}`, this.job).then(result => {
        if (result.status === 200) {
          this.editJob(result.data)
          this.showConfirmBtn = false
        } else {
          this.toolInfo = this.rowInputText
          this.showConfirmBtn = false
        }
      })
    },
    calculateAllHours: function () {
      console.log("this.allHours")
      console.log(this.allHours)
      return (
          this.reports
              .filter(r => r.user)
              .filter(r => r.user ? r.user.role.split(' ')[0] !== 'Руководитель' : false)
              .map(r => r.hoursOfWorking)
              .reduce((partialSum, a) => partialSum + a, 0))
    },
    calculateIndividualHours: function (assignedUser) {
      console.log("this.individualHours")
      console.log(this.individualHours)
      return  (
          this.reports
              .filter(r => r.user)
              .filter(r => r.user.id === assignedUser.id)
              .map(r => r.hoursOfWorking)
              .reduce((partialSum, a) => partialSum + a, 0))
    },
    del: function (assignedUser) {
      this.job.users.splice(
          this.job.users.findIndex(u => u.id === assignedUser.id),
          1
      )
      this.showConfirmBtn =true;
    }
  },
  watch: {
    job: function (newVal, oldVal) {
      this.updateAssignedUsers(newVal)
    },
  }
}
</script>

<style scoped>

</style>
















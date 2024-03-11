<template>
  <v-sheet width="320px" border>
    <div style="color: #0B0B0B" align="center" >
      <v-combobox
          density="compact"
          label="Название работ"
          variant="solo"
          @update:model-value="jobNameSelect"
          v-model="job.name"
          :items="jobNames"
          :item-value="jobNameSelected"
      >
      </v-combobox>
      <v-autocomplete
          density="compact"
          label="Виды работ"
          variant="solo"
          @update:model-value="jobNameSelect"
          v-model="job.type"
          :items="jobTypes"
          :item-value="jobTypeSelected"
      >
      </v-autocomplete>

      <v-row align="center" justify="center">
        <v-color-picker hide-canvas hide-inputs v-model="job.color" ></v-color-picker>
      </v-row>
      <br>

      <div style="font-weight: bold;color: #0B0B0B"> Стоимость работ: &nbsp</div>
      <input size="5" style="text-align:center"  type="text" @change="showConfirmBtnFunc" v-model="job.budget" /> р
      <div style="font-weight: bold;color: #0B0B0B"> Маржинальность: &nbsp</div>
      <input size="1" style="text-align:center"  type="text" @change="showConfirmBtnFunc" v-model="job.marginPercentage" /> %
      <div style="font-weight: bold;color: #0B0B0B"> Бонусная база: &nbsp</div>
      <input size="1" style="text-align:center" type="text" @change="showConfirmBtnFunc" v-model="job.bonus" /> %
      <div style="font-weight: bold;color: #0B0B0B"> Текущий бонус за объект: &nbsp</div>
      {{ Math.round(calculateAllBonusMoney()) }} р

      <v-checkbox
          label="Автоматический расчет бонусов"
          :model-value="this.autoBonus = Boolean(job.autoBonus)"
          @update:model-value="manualBonusInvertFunc"
      ></v-checkbox>

      <div v-if="usersResult[0]" style="font-weight: bold;color: #0B0B0B"> Ответственные сотрудники: &nbsp</div>

      <div v-if="autoBonus">
        <div v-for="(assignedUser, i) in usersResult"
             :key="i">
          <div>
            {{ i+1 }}) {{ assignedUser.name }} -
            {{ calculateAllHours() === 0 ?  0 : Math.round(calculateAllBonusMoney()  * calculateIndividualHours(assignedUser) / ( calculateAllHours()))}} р
            за {{calculateIndividualHours(assignedUser)}} ч из {{calculateAllHours()}} ч
            <v-btn
                @click="del(assignedUser, i)" icon="mdi-delete">  </v-btn>
          </div>
        </div>
      </div>

      <div v-if="!autoBonus">
        Всего: {{
          bonusValue
              .map(v => Number(v))
              .reduce((partialSum, a) => partialSum + a, 0)
        }} %
        /
        {{Math.round(calculateAllBonusMoney() * bonusValue
          .map(v => Number(v))
          .reduce((partialSum, a) => partialSum + a, 0)  / (100 ) )}} р

        <div v-for="(assignedUser, i) in usersResult"
             :key="i">
          <div>
            {{ i+1 }}) {{ assignedUser.name }} -
            {{Math.round(calculateAllBonusMoney() * bonusValue[i]  / (100))}} р
            <input size="1" style="text-align:center" type="text" @change="showConfirmBtnFuncUserBonus(i)" v-model="bonusValue[i]" /> %
            <v-btn
                @click="del(assignedUser, i)" icon="mdi-delete">  </v-btn>
          </div>

          <div style="margin-top: 5px">
            <v-btn v-show="showConfirmBtnUserBonus[i]" color="green"  @click="editUserBonus(assignedUser, bonuses.find(b => b.user.id === assignedUser.id), i)" > Применить </v-btn>
            <v-btn v-show="showConfirmBtnUserBonus[i]" color="red"  @click="hideConfirmBtnFuncUserBonus(i)" > Отмена </v-btn>
          </div>
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
      <div v-show="showConfirmBtn" style="margin-top: 5px">
        <v-btn  color="green"  @click="editJob" > Применить </v-btn>
        <v-btn color="red"  @click="hideConfirmBtnFunc" > Отмена </v-btn>
      </div>
    </div>
    <br>
  </v-sheet>
</template>

<script>

// Функция удаления дубликатов
function removeDuplicates(arr) {
  return arr.filter((value, index, self) =>
      index === self.findIndex((t) => (
          t.place === value.place && t.id === value.id
      ))
  )
}


export default {
  name: "jobRow",
  props: ['profile', 'role', 'tool', 'rowInputText' , 'editJob', 'rowInputType', 'job', 'users', 'reports', 'profileId'],
  data() {
    return {
      imageEditButton: false,
      facilityNameSelected: '',
      showConfirmBtn: false,
      showConfirmBtnUserBonus: [],
      toolInfo: '',
      toolEdit: '',
      jobNameSelected:'',
      jobTypeSelected:'',
      jobNames: ['Черновой монтаж', 'Чистовой монтаж','Шефмонтаж',  'Сборка щитов',
        'Концептуальное проектирование', 'Рабочее проектирование', 'Расключение шкафов',
        'ПНР', 'Авторский надзор', 'Менеджмент' ],
      jobTypes: ['Черновой монтаж', 'Чистовой монтаж','Шефмонтаж',  'Сборка щитов',
        'Концептуальное проектирование', 'Рабочее проектирование', 'Расключение шкафов',
        'ПНР', 'Авторский надзор', 'Менеджмент' ],
      userNameSelected: '',
      assignedUsers: [],
      bonusMoney: '',
      allHours: 0,
      individualHours: 0,
      reportCoast: 0,
      autoBonus: false,
      bonuses: [],
      bonus: {},
      bonusValue: [],
      allBonusPercFoWorkers: 0,
      bonusValueToDeleteId: 'nothing',
      usersForBonuses: [],
      usersResult: []
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
    manualBonusInvertFunc: function () {
      this.autoBonus = !this.autoBonus
      this.showConfirmBtn=true

      this.job.autoBonus = this.autoBonus + 0 // присваиваем выбранный бонус
    },
    showConfirmBtnFuncUserBonus: function (i) {
      this.showConfirmBtnUserBonus[i] = true;
    },
    jobNameSelect: function (jobNameSelected) {
      this.jobNameSelected = jobNameSelected;
      this.showConfirmBtn=true
    },
    jobTypeSelect: function (jobTypeSelected) {
      this.jobTypeSelected = jobTypeSelected;
      this.showConfirmBtn = true
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
    },
    hideConfirmBtnFuncUserBonus: function (i) {
      this.showConfirmBtnUserBonus[i] = false
    },
    editUserBonus: function (assignedUser, bonus, i) {
      this.bonus = bonus

      if (bonus ? bonus.value : false) { // если бонус есть уже, то редактируем, нет - создаем новый
        this.bonus.value = this.bonusValue[i]

        this.axios.put(`api/bonus/${this.bonus.id}`, this.bonus).then(result => {
          if (result.status === 200) {

            // this.editBonus(result.data)
            this.showConfirmBtnUserBonus[i] = false
          }
        })
      } else {
        // создаем новый
        this.bonus = {
          value: this.bonusValue[i],
          job: {
            id: this.job.id
          },
          user: {
            id: assignedUser.id
          }
        }

        this.axios.post(`api/bonus`, this.bonus).then(result => {
          if (result.status === 200) {
            this.bonuses.push(result.data)
            // this.bonusValue.push(result.data.value)
            // this.editBonus(result.data)
            this.showConfirmBtnUserBonus[i] = false
          }
        })
      }
    },
    updateAssignedUsers: function (job) {
      if (job) {
        this.assignedUsers = []
        if (job.users) {
          if (job.users[0]) {
            job.users.forEach(u => {
              this.assignedUsers.push(u)
            })
          }

          this.updateBonuses()
        }
      }
    },
    bonusSelectFunc: function(assignedUser) {
      this.bonuses.find(b => b.user.id === assignedUser.id)
    },
    calculateAllBonusMoney: function () {
      this.reportCoast = this.reports
          .filter(r => r.user)
          .filter(r => r.user.salary !== null)
          .map(r => r.hoursOfWorking * r.user.salary/8)
          .reduce((partialSum, a) => partialSum + a, 0)

      console.log("reportCoast")
      console.log(this.reportCoast)

      return Math.round(this.job.budget - this.reportCoast
          )
          * (this.job.marginPercentage/100)
          * (this.job.bonus/100);
    },
    editJob: function () {

      this.job.autoBonus = this.autoBonus + 0 // присваиваем выбранный бонус

      this.axios.put(`api/job/${this.job.id}`, this.job).then(result => {
        if (result.status === 200) {
          this.editJob(result.data)
          this.hideConfirmBtnFunc()

          if (this.bonusValueToDeleteId !== 'nothing') {
            this.bonusValue.splice(
                this.bonusValueToDeleteId,
                1
            )
            this.bonusValueToDeleteId = 'nothing'
          }
          // this.updateBonuses()
        }
      })
    },
    calculateAllHours: function () {
      console.log("this.allHours")
      console.log(this.allHours)
      return (
          this.reports
              .filter(r => r.user) // только те отчеты которые принадлежат хоть какому-то пользователю
              .filter(r => this.usersResult.find(u => (u.id === r.user.id)))  // учитываем только время закрепленных за объектом сотрудников
              // .filter(r => r.user ? r.user.role.split(' ')[0] !== 'Руководитель' : false) // часы руководителей не входят
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
    updateBonuses: function () {
      this.bonuses = []
      this.bonusValue = []
      this.usersForBonuses = []
      this.usersResult = []

      // Запрашиваем бонусы
      this.axios.get( "api/bonus/job/" + this.job.id).then(result => {
            result.data.forEach(t => {
              this.bonuses.push(t)
              this.bonusValue.push(t.value)
              this.usersForBonuses.push(t.user)

            })

            // Создаем результирующий массив в котором все пользователи: и с бонусами и без
            this.usersResult = this.usersForBonuses

            console.log("this.usersForBonuses")
            console.log(this.usersForBonuses)

            this.assignedUsers.forEach(u => {
              if (!this.usersForBonuses.find(u1 => u.id === u1.id))
                this.usersResult.push(u)
            })

            // if (!this.usersForBonuses[0]) {
            //   this.usersForBonuses = this.assignedUsers
            // }
          }
      )
    },
    del: function (assignedUser, i) {
      var bonusToDelete = this.bonuses.find(b => b.user.id === assignedUser.id)

      if (bonusToDelete)
        this.axios.delete(`api/bonus/${bonusToDelete.id}`).then(result => {
          if (result.status === 200) {

          }
        })

      this.bonusValueToDeleteId = i

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
















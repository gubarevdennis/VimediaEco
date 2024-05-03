<template>
  <v-sheet width="320px" border v-bind:color="job.color">
    <div style="color: #0B0B0B" align="center" >
      <v-btn width="100%" v-show="!isColorPicker" @click="showColorPicker">Выбрать цвет</v-btn>
      <v-row v-show="isColorPicker" align="center" justify="center">
        <v-color-picker width="90%" hide-inputs @update:model-value="showConfirmBtnFunc" v-model="job.color" >
        </v-color-picker>
        <v-col>
          <v-btn width="100%" color="rgba(255,255,255,0.5)" @click="showColorPicker">Выбрать</v-btn>
        </v-col>
      </v-row>
      <br>
      <br>

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
          @update:model-value="jobTypeSelect"
          v-model="job.type"
          :items="jobTypes"
          :item-value="jobTypeSelected"
      >
      </v-autocomplete>

      <v-col v-show="errorFields" style="color: #1a1515; background-color: #ca4141; border-radius: 10px; font-weight: bold;">
        <v-text>Ошибка</v-text>
        <br>
        <v-text>Заполните названия и стоимость работы</v-text>
      </v-col>

<!--      <v-alert v-show="errorFields"-->
<!--          type="error"-->
<!--          closable-->
<!--          title="Ошибка"-->
<!--          text="Заполните все поля">-->
<!--      </v-alert>-->

      <div style="font-weight: bold;color: #0B0B0B"> Стоимость работ: &nbsp</div>
      <input id="isOpen_budget" size="5" style="text-align:center" type="text" @input="showConfirmBtnFunc" v-model="job.budget"/> р
      <div style="font-weight: bold;color: #0B0B0B"> Налоги: &nbsp</div>
      <input id="isOpen_taxes" size="5" style="text-align:center"  type="text" @input="showConfirmBtnFunc" v-model="job.taxes" /> р
      <div style="font-weight: bold;color: #0B0B0B"> Возврат: &nbsp</div>
      <input id="isOpen_refund" size="5" style="text-align:center"  type="text" @input="showConfirmBtnFunc" v-model="job.refund" /> р
      <div style="font-weight: bold;color: #0B0B0B"> Расходы: &nbsp</div>
      <input id="isOpen_expenses" size="5" style="text-align:center"  type="text" @input="showConfirmBtnFunc" v-model="job.expenses" /> р
      <div style="font-weight: bold;color: #0B0B0B"> Процент от маржи на бонусы: &nbsp</div>
      <input id="isOpen_bonus" size="5" style="text-align:center" type="text" @input="showConfirmBtnFunc" v-model="job.bonus" /> %
      <div style="font-weight: bold;color: #0B0B0B"> Текущий бонус за объект: &nbsp</div>
      {{ this.calculateAllBonusMoney().toFixed(2) }} р

      <div style="font-weight: bold;color: #0B0B0B"> Себестоимость: </div>
      {{this.reportCoast}} р

      <div style="font-weight: bold;color: #0B0B0B"> Общее количество часов: </div>
      {{this.calculateAllHours()}} ч


<!--      <v-checkbox-->
<!--          density="0"-->
<!--          label="С НДС"-->
<!--          :model-value="this.nds = Boolean(job.nds)"-->
<!--          @update:model-value="manualNDSInvertFunc"-->
<!--      ></v-checkbox>-->
      <v-checkbox
          v-show="isOpenJob(job)"
          density="0"
          label="Автоматический расчет бонусов"
          :model-value="this.autoBonus = Boolean(job.autoBonus)"
          @update:model-value="manualBonusInvertFunc"
      ></v-checkbox>

      <div v-show="showConfirmBtn" style="margin-top: 5px">
        <v-btn v-show="isOpenJob(job)" color="green"  @click="editCurrentJob" > Принять </v-btn>
        <v-btn v-show="isOpenJob(job)" color="red"  @click="hideConfirmBtnFunc" > Отмена </v-btn>
      </div>


      <div class="ma-2 pa-2">
        <v-btn class="pa-2" @click="showUserByReports()">Сотрудники, написавшие отчет</v-btn>
<!--        <v-autocomplete-->
<!--            v-show="showUsersByReport"-->
<!--            density="compact"-->
<!--            label="Сотрудники по отчетам"-->
<!--            variant="solo"-->
<!--            :items="allUsersByReport"-->
<!--        >-->
<!--        </v-autocomplete>-->
        <v-list v-show="showUsersByReport" style="background-color:transparent;">
          <v-list-item
              v-for="user in allUsersByReport">
            <v-list-item-title>{{user.name}} - {{user.hours}} ч</v-list-item-title>
          </v-list-item>
        </v-list>

      </div>

      <div v-if="usersResult[0]" style="font-weight: bold;color: #0B0B0B"> Ответственные сотрудники: &nbsp</div>

      <div v-if="autoBonus">
        <div v-for="(user, i) in usersWithBonus"
             :key="i">
          <div align="start">
            {{ i+1 }}) {{ user.name }} -
            {{ calculateAllHours() === 0 ?  0 : Math.round(this.calculateAllBonusMoney()  * calculateIndividualHours(user) / ( calculateAllHours()))}} р
            за {{calculateIndividualHours(user)}} ч из {{calculateAllHours()}} ч

          </div>
          <v-btn style="font-size: 8pt !important" @click="deleteUserFromJob(user)">Удалить</v-btn>
        </div>
      </div>

      <div v-if="!autoBonus">
        Всего: {{
          usersWithBonus
              .map(v => Number(v.value))
              .reduce((partialSum, a) => partialSum + a, 0)
        }} %
        /
        {{Math.round(calculateAllBonusMoney() * usersWithBonus
          .map(v => Number(v.value))
          .reduce((partialSum, a) => partialSum + a, 0)  / (100 ) )}} р

        <div v-for="(user, i) in usersWithBonus"
            :key="i"
            align="start">
          <div>
            {{ i+1 }}) {{ user.name }} - {{user.value}} % /
            {{Math.round(calculateAllBonusMoney() * user.value / (100))}} р
          </div>
          <v-btn v-show="isOpenJob(job)" style="font-size: 8pt !important" @click="user.isShowEdit = !user.isShowEdit">Изменить</v-btn>
          <v-btn v-show="isOpenJob(job)" color="red" style="font-size: 8pt !important" @click="user.isConfirmDelete = !user.isConfirmDelete">Удалить</v-btn>
          <v-btn v-show="user.isConfirmDelete" color="green" style="font-size: 8pt !important" @click="deleteUserFromJob(user)">Подвердить удаление</v-btn>
          <v-btn v-show="user.isConfirmDelete" color="red" style="font-size: 8pt !important" @click="user.isConfirmDelete = !user.isConfirmDelete">Отмена</v-btn>
          <br>

          <div v-show="user.isShowEdit">
            <div style="font-weight: bold;color: #0B0B0B; background-color: #F9F9F9"> Бонус сотрудника: &nbsp</div>
            <input size="5" style="text-align:center"  type="text" v-model="newUserBonus"/> %
          </div>

          <div v-show="user.isShowEdit" style="margin-top: 5px">
            <v-btn color="green"  @click="editUserBonus(user)" > Применить </v-btn>
            <v-btn color="red"  @click="user.isShowEdit = !user.isShowEdit" > Отмена </v-btn>
          </div>
          <br>

        </div>

      </div>

      <v-autocomplete
          v-show="isOpenJob(job)"
          density="compact"
          label="Добавить сотрудника"
          variant="solo"
          v-model="userNameSelected"
          @update:model-value="userNameSelect"
          :items="users.map(u => u.name)"
          :item-value="userNameSelected"
      >
      </v-autocomplete>

      <div v-show="showAddUserBtn" style="margin-top: 5px">
        <v-btn  color="green"  @click="addUserToJob" > Добавить </v-btn>
        <v-btn color="red"  @click="showAddUserBtn = !showAddUserBtn" > Отмена </v-btn>
      </div>

      <div v-show="showBonusInput">
        <div style="font-weight: bold;color: #0B0B0B; background-color: #F9F9F9"> Бонус сотрудника: &nbsp</div>
        <input size="5" style="text-align:center"  type="text" v-model="newUserBonus" /> %
      </div>

      <div v-show="showBonusInput" style="margin-top: 5px">
        <v-btn  color="green"  @click="addNewBonus" > Применить </v-btn>
        <v-btn color="red"  @click="showBonusInput = !showBonusInput" > Отмена </v-btn>
      </div>
      <br>
      <br>
    </div>

    <div v-if="role == 'Директор' || role == 'Руководитель проектов'" >
      <v-btn @click="closeJob" class="ma-2 pa-2" :color="closeJobColor(job)">{{closeJobText(job)}}</v-btn>
      <v-btn @click="payJob" class="ma-2 pa-2" :color="payJobColor(job)" v-if="role == 'Директор'" >{{payJobText(job)}}</v-btn>
    </div>



    <v-btn @click="showDeleteBtn = !showDeleteBtn" color="red">Удалить работу</v-btn>
    <div v-show="showDeleteBtn" style="margin-top: 5px">
      <v-btn  color="green"  @click="deleteJob" > Удалить </v-btn>
      <v-btn color="red"  @click="showDeleteBtn = !showDeleteBtn" > Отмена </v-btn>
    </div>
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
  props: ['profile', 'role', 'tool', 'rowInputText' , 'editJob', 'rowInputType', 'job', 'users', 'reports', 'profileId', 'updateDeletedJob'],
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
        'ПНР', 'Сервис', 'Авторский надзор', 'Менеджмент' ],
      jobTypes: ['Черновой монтаж', 'Чистовой монтаж','Шефмонтаж',  'Сборка щитов',
        'Концептуальное проектирование', 'Рабочее проектирование', 'Расключение шкафов',
        'ПНР','Сервис', 'Авторский надзор', 'Менеджмент' ],
      userNameSelected: '',
      assignedUsers: [],
      bonusMoney: '',
      allHours: 0,
      individualHours: 0,
      reportCoast: 0,
      autoBonus: false,
      nds: false,
      bonuses: [],
      bonus: {},
      bonusValue: [],
      allBonusPercFoWorkers: 0,
      bonusValueToDeleteId: 'nothing',
      usersForBonuses: [],
      usersResult: [],
      colorNumber: '',
      showDeleteBtn: false,
      showBonusInput: false,
      showAddUserBtn: false,
      usersWithBonus: [],
      newUserBonus: 0,
      showEditUser: false,

      isColorPicker: false,

      errorFields: false,

      allUsersByReport: [],
      showUsersByReport: false
    }
  },
  updated() {

    this.toolInfo = this.rowInputText
    console.log(this.rowInputText)

    this.job.budget = this.job.budget ? this.job.budget : 0
    this.job.taxes = this.job.taxes ? this.job.taxes : 0
    this.job.refund = this.job.refund ? this.job.refund : 0
    this.job.expenses = this.job.expenses ? this.job.expenses : 0
    this.job.bonus = this.job.bonus ? this.job.bonus : 0
    this.bonus = this.bonus ? this.bonus : 0

    this.getUsersWithBonus()

    this.updateAssignedUsers(this.job)
    this.userNameSelected = ''
    this.newUserBonus = 0
  },
  mounted() {
    this.toolInfo = this.rowInputText
    console.log(this.rowInputText)

    this.job.budget = this.job.budget ? this.job.budget : 0
    this.job.taxes = this.job.taxes ? this.job.taxes : 0
    this.job.refund = this.job.refund ? this.job.refund : 0
    this.job.expenses = this.job.expenses ? this.job.expenses : 0
    this.job.bonus = this.job.bonus ? this.job.bonus : 0
    this.bonus = this.bonus ? this.bonus : 0
    //
    // console.log('thiiiissss joooob')
    // console.log(this.job.taxes)
    //
    // this.getUsersWithBonus()
    //
    // this.updateAssignedUsers(this.job)

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
    manualNDSInvertFunc: function () {
      this.nds = !this.nds
      this.showConfirmBtn=true

      this.job.nds = this.nds + 0 //
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
      this.showAddUserBtn = true;
      this.userNameSelected = userNameSelected;
      // var user = this.users.filter(u => (u.name === userNameSelected));
      // if (this.job.users)
      //   this.job.users.push({id: user[0].id});
    },
    hideConfirmBtnFunc: function () {
      this.showConfirmBtn = false
      this.errorFields = false
    },
    hideConfirmBtnFuncUserBonus: function (i) {
      this.showConfirmBtnUserBonus[i] = false
    },
    // editUserBonus: function (assignedUser, bonus, i) {
    //   this.bonus = bonus
    //
    //   if (bonus ? bonus.value : false) { // если бонус есть уже, то редактируем, нет - создаем новый
    //     this.axios.put(`api/bonus/${this.bonus.id}`, this.bonus).then(result => {
    //       if (result.status === 200) {
    //
    //         //this.editBonus(result.data)
    //         this.showConfirmBtnUserBonus[i] = false
    //       }
    //     })
    //   } else {
    //     // создаем новый
    //
    //     this.bonus = {
    //       value: this.bonusValue[i],
    //       job: {
    //         id: this.job.id
    //       },
    //       user: {
    //         id: assignedUser.id
    //       }
    //     }
    //
    //     this.axios.post(`api/bonus`, this.bonus).then(result => {
    //       if (result.status === 200) {
    //         this.bonuses.push(result.data)
    //         // this.bonusValue.push(result.data.value)
    //         // this.editBonus(result.data)
    //         this.showConfirmBtnUserBonus[i] = false
    //       }
    //     })
    //   }
    // },
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
      //console.log("reports")
      //console.log(this.reports)

      this.reportCoast = this.reports
          .filter(r => r.user)
          .filter(r => r.job)
          //.filter(r => r.user.salary !== null)
          //.map(r => r.hoursOfWorking * r.user.salary/8)
          .filter(r => r.cost !== null)
          .map(r => r.hoursOfWorking * r.cost/8)
          .reduce((partialSum, a) => partialSum + a, 0)

      //console.log("reportCoast")
      //console.log(this.reportCoast)

      return Math.round(this.job.budget - this.reportCoast - this.job.taxes
          - this.job.refund - this.job.expenses)
          * (this.job.bonus/100)
    },

    editCurrentJob: function () {

      console.log('job')
      console.log(this.job.name)
      console.log(this.job.type)

      if(this.job.name === null || this.job.type === null || this.job.budget == 0){
        this.errorFields = true
      } else {
        this.errorFields = false
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
            this.updateBonuses()
          }

        })
      }
    },
    deleteJob: function (){
      this.axios.delete( "api/job/" + this.job.id).then(result => {
        if (result.status === 200) {
          console.log('deleted' + result.data.id)
          this.showDeleteBtn = false
        }
      })
      this.updateDeletedJob()
    },
    calculateAllHours: function () {
      console.log("this.allHours")
      console.log(this.allHours)
      return (
          this.reports
              .filter(r => r.user) // только те отчеты которые принадлежат хоть какому-то пользователю
              .filter(r => r.job)
              // .filter(r => this.usersResult.find(u => (u.id === r.user.id)))  // учитываем только время закрепленных за объектом сотрудников
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
              .filter(r => r.job)
              .filter(r => r.user.id === assignedUser.id)
              .map(r => r.hoursOfWorking)
              .reduce((partialSum, a) => partialSum + a, 0))
    },
    getAllUsers: function () {
      this.allUsersByReport = []

      let map = new Map()
      this.reports.filter(r => r.user).filter(r => r.job).forEach(el => {
        if(map.has(el.user.id)) {

          map.get(el.user.id).hours += parseInt(el.hoursOfWorking)
        } else {
          let obj = {}
          obj.name = el.user.name
          obj.hours = parseInt(el.hoursOfWorking)
          map.set(el.user.id, obj)
        }
      })
      map.forEach((value, key, map) => {
        let obj = {}
        obj.name = value.name
        obj.hours = value.hours
        this.allUsersByReport.push(obj)
      })
    },
    showUserByReports: function () {
      this.getAllUsers()
      this.showUsersByReport = !this.showUsersByReport
    },
    updateBonuses: function () {
      this.bonuses = []
      this.bonusValue = []
      this.usersForBonuses = []
      this.usersResult = []

      // Запрашиваем бонусы
      this.axios.get( "api/bonus/job/" + this.job.id).then(result => {
        console.log('start')
        console.log(result.data)
            result.data.forEach(t => {
              this.bonuses.push(t)
              console.log('t.value')
              console.log(t.value)
              this.bonusValue.push(t.value)
              this.usersForBonuses.push(t.user)
            })

            // Создаем результирующий массив в котором все пользователи: и с бонусами и без
            this.usersResult = this.usersForBonuses
            //Object.assign(this.usersResult, this.usersForBonuses)

            console.log("this.usersForBonuses")
            console.log(this.usersForBonuses)
            console.log(this.assignedUsers)

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
    // del: function (assignedUser, i) {
    //   var bonusToDelete = this.bonuses.find(b => b.user.id === assignedUser.id)
    //
    //   if (bonusToDelete)
    //     this.axios.delete(`api/bonus/${bonusToDelete.id}`).then(result => {
    //       if (result.status === 200) {
    //
    //       }
    //     })
    //
    //   this.bonusValueToDeleteId = i
    //
    //   this.job.users.splice(
    //       this.job.users.findIndex(u => u.id === assignedUser.id),
    //       1
    //   )
    //   this.showConfirmBtn =true;
    // },
    showColorPicker: function () {
      this.isColorPicker = !this.isColorPicker
    },
    getJobBudget: function (budget) {
      console.log(budget)
      if(budget === null)
        return 0
      else return budget
    },

    getUsersWithBonus: function () {
      this.usersWithBonus = []

      this.axios.get( "api/bonus/job/" + this.job.id).then(result => {
        result.data.forEach(el => {
          let usr = {}
          usr['id'] = el.user.id
          usr['name'] = el.user.name
          usr['value'] = el.value
          usr['isShowEdit'] = false
          usr['isConfirmDelete'] = false

          this.usersWithBonus.push(usr)
        })
      })

      return this.usersWithBonus
    },
    addUserToJob: function () {
      if(this.job.name === null || this.job.type === null || this.job.budget == 0){
        this.errorFields = true
      } else {

       let user = this.users.filter(u => u.name === this.userNameSelected)
        //this.assignedUsers.push(user[0].id)
        console.log('ALL USERS')
        console.log(this.job)
        if (!this.job.users)
          this.job.users = []
        this.job.users.push(user[0])

        //this.job.users = this.assignedUsers
        this.axios.put(`api/job/${this.job.id}`, this.job).then(result => {
          if (result.status === 200) {
            this.showAddUserBtn = false
            this.showBonusInput = true
            this.errorFields = false
          }

        })
      }

    },
    addNewBonus: function () {
      let usr = this.users.find(el => el.name == this.userNameSelected)

      console.log('POST NEW USER FOR BONUS')
      console.log(usr.name)
      console.log(this.job)

      this.bonus = {
        value: this.newUserBonus,
        job: {
          id: this.job.id
        },
        user: {
          id: usr.id
        }
      }

      this.axios.post(`api/bonus`, this.bonus).then(result => {
        if (result.status === 200) {
          this.bonuses.push(result.data)
          this.getUsersWithBonus()
          this.userNameSelected = ''

          this.showBonusInput = false
        }
      })
    },
    deleteUserFromJob: function (user) {
      let bonusToDelete = this.bonuses.find(b => b.user.id === user.id)

      if (bonusToDelete)
        this.axios.delete(`api/bonus/${bonusToDelete.id}`).then(result => {
          if (result.status === 200) {
            console.log('deleted user ' + user.name)

            this.job.users.splice(
                this.job.users.findIndex(u => u.id === user.id),
                1
            )

            this.getUsersWithBonus()
          }
        })
    },
    editUserBonus: function (user) {
      this.bonus = this.bonuses.find(b => b.user.id === user.id)
      this.bonus.value = this.newUserBonus
      console.log('bonus')
      console.log(this.bonus)
      this.axios.put(`api/bonus/${this.bonus.id}`, this.bonus).then(result => {
        if (result.status === 200) {
          user.isShowEdit = false

          this.getUsersWithBonus()
        }
      })
    },

    isOpenJob: function(job) {
      return job.nds < 1 ? true : false
    },
    closeJobColor: function (job) {
      return job.nds < 1 ? 'amber' : 'grey'
    },
    closeJobText: function (job) {
      return job.nds < 1 ? 'Закрыть работу' : 'Работа закрыта'
    },
    closeJob: function () {
      if (this.job.name === null || this.job.type === null || this.job.budget == 0) {
        this.errorFields = true
      } else {
        if (this.job.nds != 2) {

          if (this.isOpenJob(this.job)) {
            this.job.nds = 1
          } else {
            this.job.nds = 0
          }
          this.errorFields = false

          this.axios.put(`api/job/${this.job.id}`, this.job).then(result => {
            if (result.status === 200) {
              this.editJob(result.data)
              this.job.nds = result.data.nds
              console.log('THE JOB ' + result.data.name + ' IS CLOSED/OPENED')
            }

          })
        }
      }
    },

    isPaidJob: function (job) {
      return job.nds == 2 ? true : false
    },
    payJobColor: function (job) {
      return job.nds < 2 ? 'light-green' : 'grey'
    },
    payJobText: function (job) {
      return job.nds < 2 ? 'Выплатить' : 'Выплачено'
    },
    payJob: function () {
      if (this.job.name === null || this.job.type === null || this.job.budget == 0) {
        this.errorFields = true
      } else {
        if (this.isPaidJob(this.job)) {
          this.job.nds = 1
        } else {
          this.job.nds = 2
        }
        this.errorFields = false

        this.axios.put(`api/job/${this.job.id}`, this.job).then(result => {
          if (result.status === 200) {
            this.editJob(result.data)
            this.job.nds = result.data.nds
            console.log('THE JOB ' + result.data.name + ' IS PAID/NOT PAID')
          }

        })
      }
    }
  },
  watch: {
    job: function (newVal, oldVal) {
      this.updateAssignedUsers(newVal)
    },
    colorNumber: function (colorNumber) {
      this.colorNumber = colorNumber
      this.showConfirmBtn = true

    }
  }
}
</script>

<style scoped>

</style>
















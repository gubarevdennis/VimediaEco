<template>
  <v-card
      class="bg-surface-variant"
  >
    <v-container align="center" style="width: 50em">
      <div>Дата отчетов</div>
      <br>
      <v-row>
        <v-col cols="6">
          <div>Дата начала:</div>
          <br>
          <VDatePicker v-model="startDatePick" mode="date" @update:model-value="hideElements"/>
        </v-col>
        <v-col cols="6">
          <div>Дата конца:</div>
          <br>
          <VDatePicker v-model="endDatePick" mode="date" @update:model-value="hideElements"/>
        </v-col>
        <v-col cols="6">
          <div>Сотрудник:</div>
          <br>
          <v-autocomplete
              clearable
              variant="outlined"
              label="Сотрудник"
              :items="usersByRole.map(user => user.name)"
              :item-value="this.userSelected"
              @update:model-value="updateUserSelected"
              v-model="this.userSelected"
          >
          </v-autocomplete>
        </v-col>
        <v-col cols="6">
          <div>Должность:</div>
          <br>
          <v-autocomplete
              clearable
              variant="outlined"
              label="Должность"
              :items="['Монтажник','Программист','Прораб','Сборщик','Бухгалтер',
                      'Директор','Менеджер', 'Кладовщик', 'Закупщик','HR','Проектировщик',
                      'Диспетчер','Руководитель сборочного цеха','Руководитель проектного отдела',
                      'Руководитель отдела ведения проектов', 'Руководитель проектов']"
              :item-value="this.roleSelected"
              @update:model-value="updateUsersByRole"
              v-model="this.roleSelected"
          >
          </v-autocomplete>
        </v-col>
        <v-col cols="12">
          <div>Размер заработной платы:</div>
          <br>
          <input size="20" style="text-align:center; color: white" @input="hideElements" v-model="newSalary" type="text" />
        </v-col>
      </v-row>
      <br>
      <v-card color="green" v-if="isSuccess" class="ma-2 pa-2">
        <v-card-title>Отчеты изменены!</v-card-title>
      </v-card>
      <v-card color="red" v-if="isError || startDatePick > endDatePick" class="ma-2 pa-2">
        <v-card-title v-if="isError">Заполните все поля!</v-card-title>
        <v-card-title v-if="startDatePick > endDatePick">Дата начала должна быть раньше чем дата конца</v-card-title>
      </v-card>
      <br>
      <v-btn color="green" @click="checkFields">Сохранить</v-btn>
      <br>
      <v-card v-if="showConfirmBtn" width="30em" class="ma-2 pa-2">
        <v-card-title>{{this.userSelected}}</v-card-title>
        <v-card-text>C: {{formattedDate(this.startDatePick)}}   По: {{formattedDate(this.endDatePick)}}</v-card-text>
        <v-card-text>Новая заработная плата: {{this.newSalary}}</v-card-text>
      </v-card>
      <br>
      <v-btn v-if="showConfirmBtn" color="green" @click="postNewSalary">Отправить отчеты</v-btn>
    </v-container>
  </v-card>

</template>

<script>
export default {
  name: "editReportsCost",
  props: [''],
  data() {
    return {
      users: [],
      usersByRole: [],
      roles: [],
      userSelected: '',
      roleSelected: '',
      newSalary: 0,
      startDatePick: '',
      endDatePick: '',

      isError: false,
      showConfirmBtn: false,
      isSuccess: false
    }
  },
  mounted() {
    this.axios.get("api/user").then(result => {
          result.data.forEach(user => {
            this.users.push(user)
            this.usersByRole.push(user)
          })
        }
    )
    const today = new Date();
    this.startDatePick = this.formattedDate(today)
    this.endDatePick = this.formattedDate(today)
  },
  methods: {
    hideElements: function () {
      this.isError = false
      this.showConfirmBtn = false
      this.isSuccess = false
    },
    formattedDate: function (date) {
      return date.getFullYear() + '-' + (date.getMonth() + 1).toString().padStart(2, '0') + '-' + date.getDate().toString().padStart(2, '0');
    },
    updateUserSelected: function (user) {
      this.hideElements()
      this.userSelected = this.users.find(u => u.name === user)
    },
    updateUsersByRole: function (role) {
      this.hideElements()
      if(!role) {
        this.usersByRole = this.users
        this.userSelected = ''
      } else {
        this.roleSelected = role
        this.userSelected = ''
        this.usersByRole = []

        this.users.forEach(user => {
          if (user.role === this.roleSelected)
            this.usersByRole.push(user)
        })
      }
    },
    checkFields: function () {
      if(!this.newSalary || !this.userSelected || !this.startDatePick || !this.endDatePick){
        this.isError = true
        this.showConfirmBtn = false
      } else if (this.startDatePick > this.endDatePick) {
        this.showConfirmBtn = false
      } else {
        this.isError = false
        this.showConfirmBtn = true
      }
    },
    postNewSalary: function () {
      let object = {
        dateFrom: this.formattedDate(this.startDatePick),
        dateTo: this.formattedDate(this.endDatePick),
        salary: this.newSalary
      }

      console.log('this user')
      console.log(this.userSelected)
      let user_id = this.users.find(u => u.name === this.userSelected).id
      console.log('id ' + user_id)
      this.axios.post("api/report/replacecost/user/" + user_id, object).then(result => {
          if (result.status === 200) {
            this.isSuccess = true
          }
        }
      )
      console.log('POST')
      console.log(object)
    }
  }
}
</script>

<style scoped>

</style>

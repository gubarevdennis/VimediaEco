<template>
  <v-sheet>
    <v-row>
      <v-col >
        <v-card-title style="color: #D88E32" primary-title>
          {{user.name}} - {{user.role}}
          <div v-if="this.role === 'Директор'" style="font-size: 15px; color: #0B0B0B">
            Стоимость часа: {{user.salary}} р.
          </div>
          <div v-if="this.role === 'Директор' && user.telegramId" style="font-size: 15px; color: #0B0B0B">
            <v-card color="green" width="150" align="center">
                Телеграмм бот
            </v-card>
          </div>
          <div v-if="this.role === 'Директор' && !user.telegramId" style="font-size: 15px; color: #0B0B0B">
            <v-card color="red" width="150" align="center">
                Телеграмм бот
            </v-card>
          </div>
        </v-card-title>
      </v-col>
      <v-spacer></v-spacer>
      <v-col cols="auto" align="end">
        <v-btn id="Status" :color="userStatusColor" class="ma-2 pa-2" @click="changeUserStatus">{{userStatus}}</v-btn>
        <br>
        <v-btn @click="edit" icon="mdi-file-edit">  </v-btn>
        <v-btn @click="del" icon="mdi-delete">  </v-btn>
      </v-col>
    </v-row>
  </v-sheet>
</template>

<script>


function getIndex(list, id) {
  for (let i = 0; i < list.length; i++) {
    if(list[i].id === id) {
      return i;
    }
  }
  return -1;
}

export default {
  props: ['user', 'editUser','users', 'profile', 'role'], // получаем переменную user
  data(){
    return {
      userStatusColor: 'green',
      userStatus: 'Работает'
    }
  },
  mounted() {
    this.getUserStatusColor(this.user)
  },
  methods: {
    edit: function () {
      this.editUser(this.user);
    },
    del: function () {
      this.axios.delete(`api/user/${this.user.id}`).then(result => {
        if (result.status === 200) {
          this.users.splice(this.users.indexOf(this.user), 1) // удаления объекта из коллекции
        }
      })
    },
    getUserStatusColor: function (user) {
      if (user.status == 0) {
        this.userStatusColor = 'green'
        this.userStatus = 'Работает'
      }
      else {
        this.userStatusColor = 'grey'
        this.userStatus = 'Уволен'
      }
    },
    changeUserStatus: function () {
      console.log('THIS USER')
      console.log(this.user)
      if(this.user.status == 0) this.user.status = 1
      else this.user.status = 0


      let user = {
        id: this.user.id,
        name: this.user.name,
        role: this.user.role,
        salary: this.user.salary,
        password: this.user.password,
        telegramId: this.user.telegramId,
        status: this.user.status
      };

      this.axios.post('api/user/' + this.user.id, user).then(res => {
        let index = getIndex(this.users, res.data.id) // получеам индекс коллекции

        this.users.splice(index, 1, res.data);
        this.getUserStatusColor(res.data)
        // Очищаем поля
      })
    }
  }
}
</script>

<style>

</style>

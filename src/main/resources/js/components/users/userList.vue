<template>
  <v-card
      v-if="this.role === 'admin' || this.role === 'HR' || this.role === 'Директор'"
      class="bg-surface-variant mb-6"
  >
    <v-col>
      <v-card v-for="user in sortedUsers" class="pa-2 ma-2">
        <user-row v-bind:key="user.id"
                      :user="user"
                      :editUser="editUser" :users="users" />
      </v-card>
    </v-col>
    <v-row
        align="start"
        no-gutters
    >
      <v-col align="start">
        <user-form :users="users" :userAttr="user" />
      </v-col>
    </v-row>
  </v-card>
</template>

<script>


const url = 'http://localhost:'
const port = '9000/'
//const url = 'http://reports.vimedia.ru/'

import userForm from "./userForm.vue";
import userRow from "./userRow.vue";
export default {
  props: ['users', 'profile', 'role'],
  components: {
    userForm,
    userRow
  },

  data() {
    return {
      user: null,
    }
  },
  computed: {
    sortedUsers() {
      return this.users.sort((a,b) => -(a.id-b.id)) // сортировка по убыванию (каждый раз когда будет меняться facilities)
    }
  },
  // указываем связь данного компонента с полученными от сервара данными
  created: function () {
    // Вставляем пользователей только вслучае, если их не было до этого в массиве на фронтенде
    this.axios.get(url+ port + "api/user").then(result =>
        result.data.forEach(user => {
          if (this.users.find((u) => u.id === user.id) === undefined)
            return this.users.push(user)
        }))
  },
  methods: {
    editUser: function (user) {
      this.user = user
      console.log("Функция editUser " + user.name)
    }
  }
}

</script>

<style>

</style>

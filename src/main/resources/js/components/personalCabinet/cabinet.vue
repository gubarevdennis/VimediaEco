<template>
  <v-card
      class="bg-surface-variant mb-6"
  >
    <v-row
        align="start"
        no-gutters
    >
      <v-col align="start">
        <user-form :users="users" :userAttr="user" :profile="profile" :role="role" :profileId="profileId" />
      </v-col>
    </v-row>

  </v-card>
</template>

<script>


const url = 'http://localhost:'
const port = '9000/'
//const url = 'http://reports.vimedia.ru/'

import userForm from "./userFormCabinet.vue";
export default {
  props: ['users', 'profile', 'role', 'profileId'],
  components: {
    userForm
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

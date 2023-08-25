<template>
  <v-sheet>
    <v-row>
      <v-col >
        <v-card-title primary-title>
          {{user.name}} - {{user.role}}
        </v-card-title>
      </v-col>
      <v-spacer></v-spacer>
      <v-col cols="auto" align="end">
        <v-btn @click="edit" icon="mdi-file-edit">  </v-btn>
        <v-btn @click="del" icon="mdi-delete">  </v-btn>
      </v-col>
    </v-row>
  </v-sheet>
</template>

<script>

const url = 'http://sisyphos.vimedia.ru/'

export default {
  props: ['user', 'editUser','users'], // получаем переменную user
  methods: {
    edit: function () {
      this.editUser(this.user);
    },
    del: function () {
      this.axios.delete(url + `api/user/${this.user.id}`).then(result => {
        if (result.status === 200) {
          this.users.splice(this.users.indexOf(this.user), 1) // удаления объекта из коллекции
        }
      })
    }
  }
}
</script>

<style>

</style>

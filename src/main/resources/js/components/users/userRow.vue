<template>
  <v-sheet>
    <v-row>
      <v-col >
        <v-card-title style="color: #D88E32" primary-title>
          {{user.name}} - {{user.role}}
          <div v-if="this.role === 'Директор'" style="font-size: 15px; color: #0B0B0B">
            Стоимость часа: {{user.salary}} р.
          </div>
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


export default {
  props: ['user', 'editUser','users', 'profile', 'role'], // получаем переменную user
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
    }
  }
}
</script>

<style>

</style>

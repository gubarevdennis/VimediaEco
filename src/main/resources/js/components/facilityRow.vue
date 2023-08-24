<template>
  <v-sheet>
    <v-row>
      <v-col >
        <v-card-title primary-title>
          {{facility.name}}
        </v-card-title>
      </v-col>
      <v-spacer></v-spacer>
      <v-col cols="auto" align="end">
        <v-btn v-if="this.role === 'admin' || this.role === 'HR' || this.role === 'Директор'" @click="edit" icon="mdi-file-edit">  </v-btn>
        <v-btn v-if="this.role === 'admin' || this.role === 'HR' || this.role === 'Директор'" @click="del" icon="mdi-delete">  </v-btn>
      </v-col>
    </v-row>
  </v-sheet>
</template>

<script>
export default {
  props: ['facility', 'editFacility','facilities', 'role'], // получаем переменную facility
  methods: {
    edit: function () {
      this.editFacility(this.facility);
    },
    del: function () {
      this.axios.delete(`http://192.168.100.100:6552/api/facility/${this.facility.id}`).then(result => {
           if (result.status === 200) {
             this.facilities.splice(this.facilities.indexOf(this.facility), 1) // удаления объекта из коллекции
           }
      })
    }
  }
}
</script>

<style>

</style>

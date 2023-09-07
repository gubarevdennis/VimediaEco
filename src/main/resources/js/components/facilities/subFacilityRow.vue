<template>
  <v-sheet  color="#F9F9F9">
    <v-row >
      <v-col>
        <v-card-text>
          {{subFacilityInner.name}}
        </v-card-text>
      </v-col>
      <v-spacer></v-spacer>
      <v-col cols="auto" align="end">
        <!--  <v-btn v-if="this.role === 'admin' || this.role === 'HR' || this.role === 'Директор'
          || this.role === 'Менеджер' || this.role === 'Руководитель отдела ведения проектов' || this.role === 'Руководитель проектов'"  @click="edit" icon="mdi-file-edit">  </v-btn>
        -->
        <v-btn v-if="this.role === 'admin' || this.role === 'HR' || this.role === 'Директор'" @click="del" icon="mdi-delete">  </v-btn>
      </v-col>
    </v-row>
  </v-sheet>
</template>

<script>



// const url = 'http://localhost:'
// const port = '9000/'
// const url = 'http://reports.vimedia.ru/'
// const port = ''

export default {
  props: ['facility','subFacilityInner', 'editSubFacility','facilities', 'role', 'subFacilities', 'deleteSubFacility'], // получаем переменную facility
  computed: {
  },
  methods: {
    edit: function () {
      this.editSubFacility(this.subFacilityInner);
    },
    del: function () {
      this.axios.delete( `api/subFacility/${this.subFacilityInner.id}`).then(result => {
           if (result.status === 200) {
             this.deleteSubFacility(this.subFacilityInner)
             // удаления объекта из коллекции
           }
      })
    },
    subFacilityForVue: function (facilityId) {
      return this.facilities.find(f => f.id === facilityId).subFacilities
    }
  }
}
</script>

<style>

</style>

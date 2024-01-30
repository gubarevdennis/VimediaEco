<template>
  <v-sheet>
    <v-row>
      <v-col cols="10">
        <v-card-title primary-title>
          {{facility.name}}
        </v-card-title>
      </v-col>
      <v-col cols="10">
      <v-card color="#F9F9F9" height="65px" rounded="xl" v-for="subFacilityInner in subFacilities" :key="subFacilityInner.id" class="pa-2 ma-2" >
        <sub-facility-row
            :editSubFacility="editSubFacility"
            :deleteSubFacility="deleteSubFacility"
            :subFacilityAttr="subFacility"
            :subFacilityInner="subFacilityInner"
            :facility="facility"
            :facilities="facilities"
            :role="role"
        />
      </v-card>
      </v-col>
      <v-spacer></v-spacer>
      <v-col  align="end" cols="auto">
        <v-btn v-if="this.role === 'admin' || this.role === 'Директор' || this.role === 'Диспетчер'
          || this.role === 'Руководитель отдела ведения проектов'"  @click="edit" icon="mdi-plus">  </v-btn>
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

import SubFacilityRow from "./subFacilityRow.vue";

export default {
  data() {
    return {
      subFacilities: []
    }
  },
  components: {SubFacilityRow},
  props: ['facility', 'editFacility','facilities', 'role', 'deleteSubFacility', 'subFacility',
    'url', 'port'], // получаем переменную facility
  computed: {
  },
  created: function () {
    this.facility.subFacilities ? this.facility.subFacilities.forEach( s => this.subFacilities.push(s)) : []
  },
  methods: {
    edit: function () {
      this.editFacility(this.facility);
    },
    del: function () {
      this.axios.delete(`api/facility/${this.facility.id}`).then(result => {
           if (result.status === 200) {
             this.facilities.splice(this.facilities.indexOf(this.facility), 1) // удаления объекта из коллекции
           }
      })
    },
    subFacilityForVue: function (facilityId) {
      return this.facilities.find(f => f.id === facilityId).subFacilities
    },
    editSubFacility: function (subFacility) {
      this.subFacility = subFacility
    },
    deleteSubFacility: function (subFacility) {
      console.log("Удаляю субобъект " + subFacility.name)
      this.subFacilities.splice(this.subFacilities.indexOf(subFacility), 1) // удаления объекта из коллекции
    }
  },
  watch: {
    subFacility: function (newVal, oldVal) {
      if (newVal.facility)
      if (this.facility.name === newVal.facility.name)
      {
        this.subFacilities.push(newVal)
      }

      this.text = newVal.text;
      this.id = newVal.id;
    },
  }
}
</script>

<style>

</style>

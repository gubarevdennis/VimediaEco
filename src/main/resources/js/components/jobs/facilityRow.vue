<template>
  <v-sheet>
  <div v-if="subFacilities[0]">
    <v-row>
      <v-col cols="10">
        <v-card-title primary-title>
          {{facility.name}}
        </v-card-title>
      </v-col>
      <v-col cols="10">
        <v-card v-if="subFacilities" @click="turnOverlayAndSetSubFacilityToJobList(overlay, subFacilityInner)" color="#F9F9F9" height="65px" rounded="xl" v-for="subFacilityInner in subFacilities" :key="subFacilityInner.id" class="pa-2 ma-2" >
          <sub-facility-row
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
      </v-col>
    </v-row>
  </div>
  <v-card v-if="!subFacilities[0]" @click="turnOverlayAndSetFacilityToJobList(overlay, facility)">
    <v-row>
      <v-col cols="10">
        <v-card-title primary-title>
          {{facility.name}}
        </v-card-title>
      </v-col>
      <v-spacer></v-spacer>
    </v-row>
  </v-card>
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
  props: ['facility', 'editFacility','facilities', 'role', 'deleteSubFacility', 'subFacility', 'setSubFacility', 'turnOverlayAndSetFacilityToJobList', 'turnOverlayAndSetSubFacilityToJobList',
    'url', 'port'], // получаем переменную facility
  computed: {
  },
  created: function () {
    this.facility.subFacilities ? this.facility.subFacilities.forEach( s => this.subFacilities.push(s)) : []
  },
  methods: {

  },
  watch: {
    subFacility: function (newVal, oldVal) {
    },
  }
}
</script>

<style>

</style>

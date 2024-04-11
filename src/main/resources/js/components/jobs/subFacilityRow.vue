<template>
  <v-card-title primary-title class="mt-0">
    {{subFacilityInner.name}}
  </v-card-title>

<!--      <v-col cols="auto" align="end">-->
<!--        &lt;!&ndash;  <v-btn v-if="this.role === 'admin' || this.role === 'HR' || this.role === 'Директор'-->
<!--          || this.role === 'Менеджер' || this.role === 'Руководитель отдела ведения проектов' || this.role === 'Руководитель проектов'"  @click="edit" icon="mdi-file-edit">  </v-btn>-->
<!--        &ndash;&gt;-->

<!--      </v-col>-->
  <v-card-text>
    Ответственный:
    <div style="font-weight: bold">
      {{ subFacilityInner.user && subFacilityInner.user.role ? subFacilityInner.user.name + ", " +  subFacilityInner.user.role.toLowerCase(): ''}}
    </div>
  </v-card-text>
</template>

<script>



// const url = 'http://localhost:'
// const port = '9000/'
// const url = 'http://reports.vimedia.ru/'
// const port = ''

import JobList from "./jobList.vue";
export default {
  components: {JobList},
  props: ['facility','subFacilityInner', 'editSubFacility','facilities', 'role', 'subFacilities',
    'deleteSubFacility', 'users', 'userNames'], // получаем переменную facility
  computed: {
  },
  methods: {

  }
}
</script>

<style>

</style>

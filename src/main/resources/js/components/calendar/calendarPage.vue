<template >
  <VCalendar  ref="calendar" expanded
             :columns="columns"
             :attributes="attributes"
             :initial-page-position="2"
  >
  </VCalendar>
  <v-card>
  </v-card>
</template>

<script>
import {ref} from 'vue'

var calendar = ref(null);


export default {

  props: ['facilities', 'columns' , 'attributes', 'profile', 'role', 'sumHoursByMonthAndUser'],
  data() {
    return {
      facilities: [],
      adminFlag: false,
    }
  },
  methods: {
      clicked() {
        alert('click!')
      }
    }

}

</script>

<style>

</style>

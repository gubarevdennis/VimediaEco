<template >
  <v-sheet style="background-color: rgba(66,66,66,1); color: #F9F9F9">
    <v-row align="stretch" justify="center" class="mx-2">
      <v-col >
        <br>
        <v-combobox
            clearable
            variant="outlined"
            label="Название проекта"
            @update:model-value="selectFacility"
            :items="facilities.map(f => f.name)"
            :item-value="this.facilitySelected"
        >
        </v-combobox>
      </v-col>
      <v-col>
        <br>
        <v-autocomplete
            clearable
            variant="outlined"
            label="Ответственный"
            @update:model-value="selectUser"
            :items="users"
            :item-value="this.userSelected"
        >

        </v-autocomplete>
      </v-col>
    </v-row>
  </v-sheet>
</template>

<script>
export default {
  name: "filterFacility",
  props: [
    'facilities',
    'users',
    'updateFacilityMask',
    'updateUserMask'
  ],
  data() {
    return {
      facilitySelected: '',
      userSelected: ''
    }
  },
  mounted() {

  },
  methods: {
    selectFacility: function (facilitySelected) {
      console.log('Запустил selectFacility')

      this.facilitySelected = facilitySelected

      this.updateFacilityMask(facilitySelected)

      //this.resultFilter()
    },
    selectUser: function (userSelected) {
      console.log('Запустил selectUser')
      console.log(userSelected)

      this.userSelected = userSelected

      this.updateUserMask(userSelected)

      //this.resultFilter()
    }
  }
}
</script>

<style scoped>

</style>

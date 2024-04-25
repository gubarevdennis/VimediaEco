<template>
  <v-row align="stretch" justify="center">
    <v-col         cols="10"
                   sm="3">
      <v-autocomplete
          variant="outlined"
          v-model="formData.facility"
          label="Объект"
          @update:modelValue="facilitySelected"
          :items="facilities.map(el => el.name)"
          :item-value="formData.facility"

      ></v-autocomplete>
    </v-col>

    <v-col         cols="10"
                   sm="3"
                   v-if="isSubFacility">
      <v-select
          variant="outlined"
          v-model="formData.subFacility"
          label="Подобъект"
          @update:modelValue="subFacilitySelected"
          :items="subFacilities.map(el => el.name)"
          :item-value="formData.subFacility"

      ></v-select>
    </v-col>

    <v-col         cols="10"
                   sm="3">
      <v-select
          variant="outlined"
          v-model="formData.hoursOfWorking"
          label="Длительность в часах"
          :items="[1,2,3,4,5,6,7,8]"
          :item-value="formData.hoursOfWorking"

      ></v-select>
    </v-col>

    <v-col         cols="10"
                   sm="3"
                   v-if="workingTypes != undefined ? workingTypes.length > 0 : false">
      <v-select
          variant="outlined"
          v-model="formData.workingType"
          label="Название работы"
          @update:modelValue="workingTypeSelected"
          :items="workingTypes.map(el => el.name)"
          :item-value="formData.workingType"

      ></v-select>
    </v-col>

  </v-row>
</template>

<script>
export default {
  name: "reportSelected",
  props: ['formData'],
  data(){
    return {
      facilities: [],
      subFacilities: [],
      isSubFacility: false,
      workingTypes: []
    }
  },
  mounted() {
    this.axios.get("api/facility").then(result => {
          return result.data.forEach(facility => {
            this.facilities.push(facility)
          })
        }
    )
  },
  methods: {
    facilitySelected: function () {
      this.subFacilities = []
      this.workingTypes = []

      this.formData.subFacility = ''
      this.formData.workingType = ''

      this.formData.facility_id = ''
      this.formData.subFacility_id = ''
      this.formData.job_id = ''

      let foundFacility = this.facilities.find(el => el.name == this.formData.facility)

      if(foundFacility) {
        if (foundFacility.subFacilities.length > 0) {
          foundFacility.subFacilities.forEach(el => {
            this.subFacilities.push(el)
          })
          this.isSubFacility = true
        } else {
          this.isSubFacility = false
        }

        if(!this.isSubFacility) {
          this.axios.get("api/job/facility/" + foundFacility.id).then(result => {
                return result.data.forEach(job => {
                  if(job.nds < 1 && job.name)
                    this.workingTypes.push(job)
                })
              }
          )
        }
      }


      console.log('sub')
      console.log(this.workingTypes)
    },
    subFacilitySelected: function () {
      this.workingTypes = []

      this.formData.workingType = ''
      this.formData.subFacility_id = ''
      this.formData.job_id = ''

      let foundSubFacility = this.subFacilities.find(el => el.name == this.formData.subFacility)

      if(foundSubFacility) {
        this.axios.get("api/job/subFacility/" + foundSubFacility.id).then(result => {
              return result.data.forEach(job => {
                if(job.nds < 1 && job.name) {
                  console.log(job.name + ' ' + job.nds)
                  this.workingTypes.push(job)
                }
              })
            }
        )
      }

      console.log('sub')
      console.log(this.workingTypes)
    },
    workingTypeSelected: function () {
      let foundFacility = this.facilities.find(el => el.name == this.formData.facility)
      if (foundFacility)
        this.formData.facility_id = foundFacility.id

      if(this.isSubFacility){
        let foundSubFacility = this.subFacilities.find(el => el.name == this.formData.subFacility)
        if(foundSubFacility){
          this.formData.subFacility_id = foundSubFacility.id
        }
      }

      let foundJob = this.workingTypes.find(el => el.name == this.formData.workingType)
      if(foundJob){
        this.formData.job_id = foundJob.id
      }
    }
  },

}
</script>

<style scoped>

</style>

<template>
  <v-sheet>
    <div>
      <v-row>
        <v-col cols="10">
          <v-card-title style="font-size: 25px; font-weight: lighter" primary-title>
            {{facility.name}}
          </v-card-title>
          <v-card-text v-if="subFacilities ? !subFacilities[0] : false" v-for="job in jobs">
{{calculateAllBonusMoney(job)}}
            <div  v-if=" this.role.split(' ')[0] !== 'Руководитель'">
             <div style="color: #006600; font-weight: bold; font-size: 20px"> {{ calculateIndividualHours(job) !== 0 ?
                 Math.round(calculateAllBonusMoney(job) * 0.8 * calculateIndividualHours(job) / calculateAllHours(job)) : 0 }} р </div>
              <div style="color: red; font-size: 18px"> {{ job.name }} </div>
              за {{calculateIndividualHours(job)}} ч из {{calculateAllHours(job)}} ч
            </div>
            <div style="font-size: 25px" v-if=" this.role.split(' ')[0] === 'Руководитель'">
              <div style="color: #006600; font-weight: bold; font-size: 20px"> {{ Math.round(calculateAllBonusMoney(job) * 0.1) }} р </div>
              <div style="color: red; font-size: 18px"> {{ job.name }} </div>
              за {{calculateIndividualHours(job)}} ч из {{calculateAllHours(job)}} ч
            </div>
          </v-card-text>
        </v-col>
        <v-col cols="10">
          <v-card v-if="subFacilities" color="#F9F9F9" rounded="xl" v-for="subFacility in subFacilities.filter(s => jobs.filter(j => (j.subFacility ? (j.subFacility.id === s.id) : false))[0])" :key="subFacility.id" class="pa-2 ma-2" >
            <div >
              <sub-facility-row
                  :deleteSubFacility="deleteSubFacility"
                  :subFacility="subFacility"
                  :subFacilities="subFacilities"
                  :facility="facility"
                  :facilities="facilities"
                  :role="role"
                  :profile="profile"
                  :profileId="profileId"
                  :jobs="jobs.filter(j => (j.subFacility ? (j.subFacility.id === subFacility.id) : false))"
              />
            </div>
          </v-card>
        </v-col>
        <v-spacer></v-spacer>
        <v-col  align="end" cols="auto">
        </v-col>
      </v-row>
    </div>
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
      subFacilities: [],
      jobs: [],
      reports: [],
      bonusMoney: '',
      allHours: '',
      individualHours: ''
    }
  },
  components: {SubFacilityRow},
  props: ['facility', 'editFacility','facilities', 'role', 'deleteSubFacility', 'setSubFacility', 'jobs',
    'url', 'port', 'profileId' ,'profile'], // получаем переменную facility

  mounted() {
    this.facility.subFacilities ? this.facility.subFacilities.forEach( s => this.subFacilities.push(s)) : []


    this.axios.get("api/report/facility/" + this.facility.id).then(result => {
          result
              .data
              .forEach(r => {
                    this.reports.push(r)
                  }
              )
        }
    )
  },
  methods: {
    calculateAllBonusMoney: function (job) {
      console.log("this.bonusMoney")
      console.log(this.bonusMoney)
      return (job.budget -
          this.reports
              .filter(r => r.user)
              .filter(r => (r.typeOfWork === job.name))
              .map(r => r.hoursOfWorking * r.user.salary/8)
              .reduce((partialSum, a) => partialSum + a, 0))
          * (job.marginPercentage/100)
          * (job.bonus/100);

    },
    calculateAllHours: function (job) {
      console.log("this.allHours")
      console.log(this.allHours)
     return (
          this.reports
              .filter(r => r.user)
              .filter(r => r.user ? r.user.role.split(' ')[0] !== 'Руководитель' : false)
              .filter(r => (r.typeOfWork === job.name))
              .map(r => r.hoursOfWorking)
              .reduce((partialSum, a) => partialSum + a, 0))


    },
    calculateIndividualHours: function (job) {
      console.log("this.individualHours")
      console.log(this.individualHours)
     return  (
          this.reports
              .filter(r => r.user)
              .filter(r => r.user.id === this.profileId)
              .filter(r => (r.typeOfWork === job.name))
              .map(r => r.hoursOfWorking)
              .reduce((partialSum, a) => partialSum + a, 0))



    }
  },
  watch: {
    subFacility: function (newVal, oldVal) {
    },
  }
}
</script>

<style>

</style>

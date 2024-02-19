<template>
  <v-sheet  color="#F9F9F9">
    <v-card-title style="font-size: 25px; font-weight: lighter">
      {{subFacility.name}}
    </v-card-title>
    <v-card-text v-for="job in jobs">

      <div v-if=" this.role.split(' ')[0] !== 'Руководитель'">
        <div style="color: #006600; font-weight: bold; font-size: 20px">  {{calculateIndividualHours(job) !== 0 ? Math.round(calculateAllBonusMoney(job) * 0.8 * calculateIndividualHours(job) / calculateAllHours(job)) : 0 }} р </div>
        <div style="color: red; font-size: 18px"> {{ job.name }} </div>
        за {{calculateIndividualHours(job)}} ч из {{calculateAllHours(job)}} ч
      </div>
      <div v-if=" this.role.split(' ')[0] === 'Руководитель'">
        <div style="color: #006600; font-weight: bold; font-size: 20px"> {{ Math.round(calculateAllBonusMoney(job) * 0.1) }} р </div>
        <div style="color: red; font-size: 18px"> {{ job.name }} </div>
        за {{calculateIndividualHours(job) }} ч из {{calculateAllHours(job)}} ч
      </div>
    </v-card-text>
    <v-spacer></v-spacer>
    <v-col cols="auto" align="end">
      <!--  <v-btn v-if="this.role === 'admin' || this.role === 'HR' || this.role === 'Директор'
        || this.role === 'Менеджер' || this.role === 'Руководитель отдела ведения проектов' || this.role === 'Руководитель проектов'"  @click="edit" icon="mdi-file-edit">  </v-btn>
      -->
    </v-col>
  </v-sheet>
</template>

<script>



// const url = 'http://localhost:'
// const port = '9000/'
// const url = 'http://reports.vimedia.ru/'
// const port = ''


export default {
  props: ['subFacility','facility', 'editSubFacility','facilities', 'role', 'subFacilities', 'deleteSubFacility', 'jobs', 'profileId' ,'profile'], // получаем переменную facility
  data() {
    return {
      reports: [],
      allHours: '',
      individualHours: '',
      bonusMoney:''
    }
  },
  mounted() {
    console.log("recursion!!!")
    this.axios.get("api/report/subFacility/" + this.subFacility.id).then(result => {
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
  }
}
</script>

<style>

</style>

<template>
  <v-sheet v-bind:color="object.facility.color">
    <div>
      <v-row>
        <v-col cols="10">
          <v-card-title style="font-size: 20px; font-weight: lighter" primary-title>
            {{object.facility.name}}
          </v-card-title>
          <v-card-text v-if="object.subFacilities.length == 0" v-for="job in jobs">
            <div v-if="job.nds == 2" style="margin-bottom: 10px">
              <span style="background-color: rgba(147,252,114, 0.4); padding: 5px">Выплачено</span>
            </div>

            <div v-if="job.autoBonus">
              <div>
                <div style="color: #006600; font-weight: bold; font-size: 20px"> {{ calculateIndividualHours(job) !== 0 ?
                    Math.round(calculateAllBonusMoney(job)  * calculateIndividualHours(job) / calculateAllHours(job)) : 0 }} р </div>
                <div style="color: #702323; font-size: 18px"> {{ job.name }} </div>
                за {{calculateIndividualHours(job)}} ч из {{calculateAllHours(job)}} ч
              </div>
            </div>
            <div v-if="!job.autoBonus">
              <div>
                <div style="color: #006600; font-weight: bold; font-size: 20px">
                  {{this.bonuses.find(b => b.job.id === job.id) ?
                    Math.round((this.bonuses.filter(b => b.job).find(b => b.job.id === job.id).value / 100) * calculateAllBonusMoney(job) )
                    : '0'}} р
                </div>
                <div style="color: #702323; font-size: 18px"> {{ job.name }} </div>
                за {{calculateIndividualHours(job)}} ч из {{calculateAllHours(job)}} ч
              </div>
            </div>
          </v-card-text>
        </v-col>
        <v-col cols="10">
          <v-card v-if="object.subFacilities.length > 0" color="#F9F9F9" rounded="xl" v-for="subFacility in object.subFacilities" :key="subFacility.id" class="pa-2 ma-2" style="background-color: rgba(255,255,255, 0.7)">
            <div>
              <sub-facility-row
                  :deleteSubFacility="deleteSubFacility"
                  :subFacility="subFacility"
                  :facility="object.facility"
                  :role="role"
                  :profile="profile"
                  :profileId="profileId"
                  :jobs="jobs.filter(j => (j.subFacility ? (j.subFacility.id === subFacility.id) : false))"
                  :bonuses="bonuses"
                  :isDirector="isDirector"
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
      reports: [],
      bonusMoney: '',
      allHours: '',
      individualHours: '',
      assignedUsers: []
    }
  },
  components: {SubFacilityRow},
  props: ['object', 'editFacility', 'role', 'deleteSubFacility', 'setSubFacility', 'jobs',
    'url', 'port', 'profileId' ,'profile', 'bonuses', 'isDirector'], // получаем переменную facility

  mounted() {
    this.object.subFacilities ? this.object.subFacilities.forEach( s => this.subFacilities.push(s)) : []
    console.log('OBJECT  ' + this.object.facility.name)
    console.log(this.object)
    console.log(this.object.jobs)

    this.axios.get("api/report/facility/" + this.object.facility.id).then(result => {
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
      //console.log("this.bonusMoney" + job.name)
      //console.log(this.bonusMoney)

      this.reportCoast = this.reports
          .filter(r => r.user)
          //.filter(r => r.user.salary !== null)
          .filter(r => r.job)
          .filter(r => (r.job.id === job.id))
          //.map(r => r.hoursOfWorking * r.user.salary/8)
          .filter(r => r.cost !== null)
          .map(r => r.hoursOfWorking * r.cost/8)
          .reduce((partialSum, a) => partialSum + a, 0)

      //console.log("reportCoast" + job.name)
      //console.log(this.reportCoast)

      return (job.budget - this.reportCoast - job.taxes
          - job.refund - job.expenses)
          * (job.bonus/100);

    },
    calculateAllHours: function (job) {
      //console.log("this.allHours" + job.name)
      //console.log(this.allHours)
      return (
          this.reports
              .filter(r => r.user)
              //.filter(r => job.users ? job.users.find(u => (u.id === r.user.id)) : false)  // учитываем только время закрепленных за объектом сотрудников
              // .filter(r => r.user ? r.user.role.split(' ')[0] !== 'Руководитель' : false)
              .filter(r => r.job)
              .filter(r => (r.job.id === job.id))
              .map(r => r.hoursOfWorking)
              .reduce((partialSum, a) => partialSum + a, 0))
    },
    calculateIndividualHours: function (job) {
      //console.log("this.individualHours" + job.name)
      //console.log(this.individualHours )
      return  (
          this.reports
              .filter(r => r.user)
              .filter(r => r.user.id === this.profileId)
              .filter(r => r.job)
              .filter(r => (r.job.id === job.id))
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

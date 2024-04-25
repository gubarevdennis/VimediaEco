<template>
  <v-sheet style="background-color: rgba(66,66,66,1); color: #F9F9F9">
    <v-row align="stretch" justify="center" class="mx-2">
      <v-col >
        <br>
        <br>
        <v-autocomplete
            clearable
            variant="outlined"
            label="Ответственный"
            @update:model-value="selectUser"
            :items="users.map(el => el.name)"
            :item-value="userSelected"
        >
        </v-autocomplete>

      </v-col>
    </v-row>
  </v-sheet>


  <v-col v-if="isSelected">
    <facility-list
    :profile="userSelected.name"
    :profile-id="userSelected.id"
    role="Директор"
    :isDirector="true">
    </facility-list>
  </v-col>
</template>

<script>

import FacilityList from "../individualJob/facilityList.vue";

export default {
  name: "bonusByUser",
  props: ['profile', 'role', 'profileId'],
  components: {FacilityList},
  data(){
    return {
      users: [],
      userSelected: {},
      objects: [],
      facilities: [],
      reports: [],

      isSelected: false,

      bonusesByUser: []
    }
  },
  mounted() {
  },
  created: function () {
    console.log('profile ' + this.profile)
    console.log('prof id ' + this.profileId)
    console.log('role ' + this.role)
    console.log(this.userSelected)

    this.axios.get("api/user").then(result => {
          result.data.forEach(user => this.users.push(user))
        }
    )
  },
  methods: {
    selectUser: function (user) {
      this.userSelected = this.users.find(el => el.name === user)
      this.isSelected = true
      console.log('id user')
      console.log(this.userSelected)
      //this.getBonusByUser(this.userSelected)
    },
    getBonusByUser: function (id) {
      this.bonusesByUser = []
      this.facilities = []
      this.reports = []

      this.axios.get("api/bonus/user/" + id).then(result => {
          result.data.forEach(bonus => {
            this.bonusesByUser.push(bonus)
            if(!this.facilities.find(el => el === bonus.job.facility.name))
              this.facilities.push(bonus.job.facility)
            // const facility = bonus.job.facility
            // let newObj = {}
            // newObj.facility = facility
            //
            // if(bonus.job.subFacility)
            //   newObj.subFacility = bonus.job.subFacility
            //
            // if(newObj != {})
            //   this.objects.push(newObj)
          })
        }
      )

      this.axios.get("api/report/user/" + id).then(result => {
        result.data.forEach(r => {
            this.reports.push(r)
          }
        )}
      )
    },
    getBonusByFacility: function (facility) {
      let result = []
    }
  }
}
</script>

<style scoped>

</style>

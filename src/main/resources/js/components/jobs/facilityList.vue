<template>

  <v-card
      class="bg-surface-variant mb-6"
  >
    <filter-facility
        :facilities="facilities"
        :users="userNames"
        :updateFacilityMask = "updateFacilityMask"
        :updateUserMask="updateUserMask"
        :withUserMask="withUserMask">
    </filter-facility>
    <br>

    <v-col>
      <v-overlay
          v-model="overlay"
          class="align-center justify-center"
          scroll-strategy="block"
          align="center"
      >
        <v-btn icon="mdi-close" @click="overlay = !overlay" :ripple="false"></v-btn>
        <br>
        <br>
        <job-list
            :facility="facility"
            :subFacility="subFacility"
            :profile="profile"
            :role="role"
            :profileId="profileId"
            :overlay="overlay"
            :overlayChange="overlayChange"
            :users="users"
            :userNames="userNames"
        ></job-list>
      </v-overlay>

      <v-div class="d-flex flex-wrap ga-4">
        <v-row >
          <v-col
              v-model="facilitySelected"
              v-for="(facility, i) in searchByFacility"

              :key="i"
              cols="12"
              sm="10" lg="4" md="6">
              <v-card rounded="lg" :profile="profile" :role="role" :profileId="profileId" width="auto" height="100%"

                      v-bind:color="facility.color"

                      class="pa-2 ma-2">

                <facility-row  v-bind:color="facility.color" v-bind:key="facility.id"
                               :facility="facility"
                               :facilities="facilities"
                               :subFacilities="subFacilities"
                               :subFacility="subFacility"
                               :role="role"
                               :turnOverlayAndSetFacilityToJobList="turnOverlayAndSetFacilityToJobList"
                               :turnOverlayAndSetSubFacilityToJobList="turnOverlayAndSetSubFacilityToJobList"
                               :users="users"
                               :userNames="userNames"
                />
              </v-card>

          </v-col>
        </v-row>

      </v-div>


    </v-col>
    <br>
  </v-card>
</template>

<script>
import facilityRow from "./facilityRow.vue";
import jobList from "./jobList.vue";
import FilterFacility from "./filterFacility.vue";


// const url = 'http://localhost:'
// const port = '9000/'
// const url = 'http://reports.vimedia.ru/'
// const port = ''

export default {
  props: ['facilities', 'profile', 'role', 'profileId', 'addSubFacility'],
  components: {
    FilterFacility,
    facilityRow,
    jobList
  },

  data() {
    return {
      facility: null,
      subFacility: null,
      facilities: [],
      color: '',
      subFacilities: [],
      editFacilityStatus: false,
      overlay: false,
      users: [],
      userNames: [],

      facilitySelected: '',
      userSelected: '',
      isByUser: false,
      withUserMask: true
    }
  },
  computed: {
    // sortedFacilities() {
    //   return this.facilities.sort((a,b) => -(a.id-b.id)) // сортировка по убыванию (каждый раз когда будет меняться facilities)
    // },
    searchByFacility() {
      let fac_mask = this.facilitySelected
      let user_mask = this.userSelected
      this.facilities.sort((a,b) => -(a.id-b.id))
      let searchResult = this.facilities.filter(function (item) {
        if (item === null || fac_mask === null ) return true
        return item.name.toLowerCase().includes(fac_mask.toLowerCase())
      })

      if (this.isByUser) {
        searchResult = searchResult.filter(item => {

          if(item.user == null) return false
          if(item.subFacilities.length > 0) {
            let result = false
            item.subFacilities.forEach(el => {
              if (el.user != null && el.user.name === user_mask) result = true
            })
            return result
          }
          return (item.user.name === user_mask)
        })
      }

      return searchResult
    }
  },
  // указываем связь данного компонента с полученными от сервара данными
  mounted: function () {
    console.log(this.role)

    // Добавление объектов
    if (this.role === 'admin' || this.role === 'Директор' || this.role === 'HR' || this.role === 'Диспетчер'
        || this.role === 'Руководитель отдела ведения проектов' || this.role.split(' ')[0] === 'Руководитель') {
      this.axios.get( "api/facility").then(result =>
          result
              .data
              .forEach(fac => {
                    if (!this.facilities.find((f) => f.id === fac.id)) {
                      this.facilities.push(fac)
                      fac.subFacilities.forEach(s => {
                        this.subFacilities.push(s)
                      })
                    }
                  }
              ))
    } else {
// фильтруем под человека
      this.axios.get("api/user/" + this.profileId).then(result =>
          result
              .data
              .reports.forEach(r => {
                if (!this.facilities.find((f) => f.id === r.facility.id)) {
                  this.facilities.push(r.facility)
                }
              }
          )
      )
    }

    // Запрашиваем пользователей
    this.axios.get( "api/user").then(tools => {
          tools.data.forEach(t => {
            this.users.push(t)
            this.userNames.push(t.name)
          })
        }
    )

  },
  methods: {
    turnOverlayAndSetFacilityToJobList: function (overlay, facility) {
      this.facility = facility
      this.subFacility = ''
      this.overlay = !overlay;
    },
    turnOverlayAndSetSubFacilityToJobList(overlay, subFacility) {
      this.subFacility = subFacility
      this.facility = ''
      this.overlay = !overlay;
    },
    overlayChange: function (overlay) {
      this.overlay = overlay;
    },
    updateFacilityMask: function (facilitySelected) {
      this.facilitySelected = facilitySelected
    },
    updateUserMask: function (userSelected) {
      if(userSelected !== null || userSelected !== '') {
        this.isByUser = true
      }
      if(userSelected === null) {
        this.isByUser = false
      }
      this.userSelected = userSelected
    }

  }
}

</script>


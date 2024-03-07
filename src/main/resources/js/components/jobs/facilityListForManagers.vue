<template>
  <v-card
      class="bg-surface-variant mb-6"
  >
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
      <v-card rounded="lg" :profile="profile" :role="role" :profileId="profileId"

              v-bind:color="facility.color"
              v-for="(facility, i) in sortedFacilities"
              :key="i"
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
    <br>
  </v-card>
</template>

<script>
import facilityRow from "./facilityRow.vue";
import jobList from "./jobList.vue";


// const url = 'http://localhost:'
// const port = '9000/'
// const url = 'http://reports.vimedia.ru/'
// const port = ''

export default {
  props: ['facilities', 'profile', 'role', 'profileId', 'addSubFacility'],
  components: {
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
      userNames: []
    }
  },
  computed: {
    sortedFacilities() {
      return this.facilities.sort((a,b) => -(a.id-b.id)) // сортировка по убыванию (каждый раз когда будет меняться facilities)
    },
  },
  // указываем связь данного компонента с полученными от сервара данными
  mounted: function () {
    console.log(this.role)

    // Добавление объектов
    this.axios.get( "api/facility/user/" + this.profileId).then(result =>
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
    }

  }
}

</script>


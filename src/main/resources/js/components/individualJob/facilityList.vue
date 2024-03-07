<template>
  <v-card
      class="bg-surface-variant mb-6"
  >
    <v-col>
      <v-card rounded="lg" :profile="profile" :role="role" :profileId="profileId"
              v-bind:color="facility.color"
              v-for="(facility, i) in sortedFacilities"
              :key="i"
              class="pa-2 ma-2">
        <facility-row  v-bind:color="facility.color"
                       :facility="facility"
                       :facilities="facilities"
                       :role="role"
                       :profile="profile"
                       :profileId="profileId"
                       :jobs="jobs.filter(j => j.facility ?  j.facility.id === facility.id : false)"
                       :bonuses="bonuses"
        />
      </v-card>
    </v-col>
    <br>
  </v-card>
</template>

<script>
import facilityRow from "./facilityRow.vue";


// const url = 'http://localhost:'
// const port = '9000/'
// const url = 'http://reports.vimedia.ru/'
// const port = ''

export default {
  props: [ 'profile', 'role', 'profileId', 'addSubFacility'],
  components: {
    facilityRow
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
      jobs: [],
      bonuses: [],


    }
  },
  computed: {
    sortedFacilities() {
      return this.facilities.sort((a,b) => -(a.id-b.id)) // сортировка по убыванию (каждый раз когда будет меняться facilities)
    },
  },
  // указываем связь данного компонента с полученными от сервара данными
  mounted: function () {
    console.log(this.profile)

    // Получаем объекты в которых текущий пользователь назначен ответственным
    this.axios.get("api/facility/job/user/" + this.profileId).then(result =>
        result
            .data
            .forEach(fac => {
                  if (!this.facilities.find((f) => f.id === fac.id)) {
                    this.facilities.push(fac)
                    fac.subFacilities.forEach(s => {
                      this.subFacilities.push(s)
                    })
                    console.log(this.facilities)
                  }
                }
            )
    )

    // Получаем работы текущего пользователя назначен ответственным
    this.axios.get("api/job/user/" + this.profileId).then(result =>
        result
            .data
            .forEach(j => {
              this.jobs.push(j)

            }))

    // Запрашиваем бонусы
    this.axios.get( "api/bonus/user/" + this.profileId).then(result => {
          result.data.forEach(t => {
            this.bonuses.push(t)
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


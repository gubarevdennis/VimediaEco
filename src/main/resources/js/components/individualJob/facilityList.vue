<template>
  <v-card
      class="bg-surface-variant mb-6"
  >
    <v-col>
      <v-div class="d-flex flex-wrap ga-4">
        <!--    <v-card-->
        <!--        class="bg-surface-variant mb-6"-->
        <!--    >-->
        <v-row>
          <v-col
              v-for="(object, i) in objectsList"
              :key="i"
              cols="12"
              sm="10" lg="4" md="6">
            <v-card rounded="lg" :profile="profile" :role="role" :profileId="profileId"
                    v-bind:color="object.facility.color"
                    width="auto" height="100%"
                    class="pa-2 ma-2">
              <facility-row  v-bind:color="object.facility.color"
                             :object="object"
                             :role="role"
                             :profile="profile"
                             :profileId="profileId"
                             :jobs="object.jobs"
                             :bonuses="bonuses"
                             :isDirector="isDirector"
              />
            </v-card>
          </v-col>
          <br>
        </v-row>
        <!--    </v-card>-->
      </v-div>
    </v-col>
  </v-card>
</template>

<script>
import facilityRow from "./facilityRow.vue";


// const url = 'http://localhost:'
// const port = '9000/'
// const url = 'http://reports.vimedia.ru/'
// const port = ''

// Функция удаления дубликатов
function removeDuplicates(arr) {
  return arr.filter((value, index, self) =>
      index === self.findIndex((t) => (
          t.place === value.place && t.id === value.id
      ))
  )
}

export default {
  props: [ 'profile', 'role', 'profileId', 'isDirector'],
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

      objectsList: []


    }
  },
  computed: {
    sortedFacilities() {
      return this.facilities.sort((a,b) => -(a.id-b.id)) // сортировка по убыванию (каждый раз когда будет меняться facilities)
    },
  },
  created() {
    this.changeUser()
  },
  updated() {
    console.log(this.profile)
    this.changeUser()
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

    changeUser: function () {
      this.objectsList = []
      this.facilities = []
      this.subFacilities = []
      this.bonuses = []
      this.jobs = []

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
                    }
                  }
              )
      )

      // Получаем работы текущего пользователя назначен ответственным
      this.axios.get("api/job/user/" + this.profileId).then(result => {
            result
                .data
                .forEach(j => {
                      this.jobs.push(j)
                    }
                )

            this.jobs = removeDuplicates(this.jobs)
          }
      )


      // Запрашиваем бонусы
      this.axios.get( "api/bonus/user/" + this.profileId).then(result => {
            result.data.forEach(t => {
              this.bonuses.push(t)
            })

            let facilityMap = new Map()
            for(let el of this.bonuses) {
              if(!facilityMap.has(el.job.facility.name)){
                let facObj = {
                  facility: el.job.facility,
                  subFacilities: new Map(),
                  jobs: []
                }
                facilityMap.set(el.job.facility.name, facObj)
              }
              if (el.job.subFacility) {
                if(!facilityMap.get(el.job.facility.name).subFacilities.has(el.job.subFacility.name)){
                  facilityMap.get(el.job.facility.name).subFacilities.set(el.job.subFacility.name, el.job.subFacility)
                }
              }
              facilityMap.get(el.job.facility.name).jobs.push(el.job)

            }

            facilityMap.forEach((value, key, map) => {
              let subFacil = []
              value.subFacilities.forEach((value, key, map) => {
                subFacil.push(value)
              })
              let obj = {
                facility: value.facility,
                subFacilities: subFacil,
                jobs: value.jobs
              }
              this.objectsList.push(obj)
            })
            console.log('ALL JOBS')
            console.log(this.objectsList)
          }
      )
    }

  }
}

</script>


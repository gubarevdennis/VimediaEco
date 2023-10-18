<template>
  <v-card
      class="bg-surface-variant mb-6"
  >

    <v-col>
      <v-card rounded="lg" :profile="profile" :role="role" :profileId="profileId"
              v-bind:color="facility.color"
              v-for="facility in sortedFacilities"
              class="pa-2 ma-2">

        <facility-row  v-bind:color="facility.color" v-bind:key="facility.id"
                      :facility="facility"
                      :editFacility="editFacility" :facilities="facilities" :subFacilities="subFacilities"
                      :subFacility="subFacility"
                      :role="role"
        />
      </v-card>
    </v-col>

    <v-row
        v-if="this.role === 'admin' || this.role === 'Директор' || this.role === 'Диспетчер'
              || this.role === 'Руководитель отдела ведения проектов'"
        align="start"
        no-gutters
    >
      <v-col align="start">
        <facility-form :facilities="facilities" :facilityAttr="facility" :role="role"
                       :addSubFacility="addSubFacility"
                       :editFacilityStatus="editFacilityStatus"
                       :editFacilityStatusFunc="editFacilityStatusFunc"
        />
      </v-col>
    </v-row>
    <br>
  </v-card>
</template>

<script>
import facilityRow from "./facilityRow.vue";
import FacilityForm from "./facilityForm.vue";


// const url = 'http://localhost:'
// const port = '9000/'
// const url = 'http://reports.vimedia.ru/'
// const port = ''

export default {
  props: ['facilities', 'profile', 'role', 'profileId', 'addSubFacility'],
  components: {
    FacilityForm,
    facilityRow
  },

  data() {
    return {
      facility: null,
      subFacility: null,
      facilities: [],
      color: '',
      subFacilities: [],
      editFacilityStatus: false
    }
  },
  computed: {
    sortedFacilities() {
      return this.facilities.sort((a,b) => -(a.id-b.id)) // сортировка по убыванию (каждый раз когда будет меняться facilities)
    },
  },
  // указываем связь данного компонента с полученными от сервара данными
  created: function () {
    console.log(this.role)

    // Добавление объектов
    if (this.role === 'admin' || this.role === 'Директор' || this.role === 'HR' || this.role === 'Диспетчер'
        || this.role === 'Руководитель отдела ведения проектов') {
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

  },
  methods: {
    editFacility: function (facility) {
      this.facility = facility
      this.editFacilityStatus = true
    },
    watchColor: function (color){
      this.color = color;
      return color;
    },
    clickedSubFac: function (){
      return (this.subAttrVisible === true) ?  this.subAttrVisible = false : this.subAttrVisible = true
    },
    addSubFacility: function (subFacility) {
       this.subFacility = subFacility
       this.addSubFacilityForRow(subFacility)

    },
    addSubFacilityForRow: function (subFacility) {
      console.log("Выводим субобъект" + subFacility.id)
    },
    editFacilityStatusFunc: function (editFacilityStatus){
      this.editFacilityStatus = editFacilityStatus
    }
  }
}

</script>


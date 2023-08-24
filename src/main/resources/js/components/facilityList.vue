<template>
  <v-card
      class="bg-surface-variant mb-6"
  >

    <v-col>
      <v-card :profile="profile" :role="role" :profileId="profileId"
              v-bind:color="facility.color"
              v-for="facility in sortedFacilities"
              class="pa-2 ma-2">
        <facility-row v-bind:color="facility.color" v-bind:key="facility.id"
                      :facility="facility"
                      :editFacility="editFacility" :facilities="facilities"
                      :role="role"
        />
      </v-card>
    </v-col>
    <v-row
        v-if="this.role === 'admin' || this.role === 'HR' || this.role === 'Директор'"
        align="start"
        no-gutters
    >
      <v-col align="start">
        <facility-form :facilities="facilities" :facilityAttr="facility" />
      </v-col>
    </v-row>
    <br>
  </v-card>
</template>

<script>
import facilityRow from "./facilityRow.vue";
import FacilityForm from "./facilityForm.vue";
export default {
  props: ['facilities', 'profile', 'role', 'profileId'],
  components: {
    FacilityForm,
    facilityRow
  },

  data() {
    return {
      facility: null,
      facilities: [],
      color: ''
    }
  },
  computed: {
    sortedFacilities() {
      return this.facilities.sort((a,b) => -(a.id-b.id)) // сортировка по убыванию (каждый раз когда будет меняться facilities)
    }
  },
  // указываем связь данного компонента с полученными от сервара данными
  created: function () {
    if (this.role === 'admin' || this.role === 'Директор' || this.role === 'HR') {
      this.axios.get("http://192.168.100.100:6552/api/user").then(result =>
          result
              .data
              .forEach(u => {
                u.reports.forEach(r => {
                      if (!this.facilities.find((f) => f.id === r.facility.id)) {
                        this.facilities.push(r.facility)
                      }
                    }
                )}))
    } else {
// фильтруем под человека
      this.axios.get("http://192.168.100.100:6552/api/user/" + this.profileId).then(result =>
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
    },
    watchColor: function (color){
      this.color = color;
      return color;
    }
  }
}

</script>


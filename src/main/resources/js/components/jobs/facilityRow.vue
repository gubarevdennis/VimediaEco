<template>
  <v-sheet v-bind:color="facility.color">
    <div v-if="subFacilities[0]">
      <v-row>
        <v-col cols="10">
          <v-card-title primary-title>
            {{facility.name}}
          </v-card-title>
        </v-col>
        <v-col cols="10" v-if="subFacilities">
          <div v-for="subFacilityInner in subFacilities" :key="subFacilityInner.id">
            <v-card
                @click="turnOverlayAndSetSubFacilityToJobList(overlay, subFacilityInner)"
                color="#F9F9F9" :height="heightSubFacilityCard" id="subFacilityCard" class=" ma-2" style="background-color: rgba(255,255,255, 0.7)">
              <sub-facility-row
                  :deleteSubFacility="deleteSubFacility"
                  :subFacilityAttr="subFacility"
                  :subFacilityInner="subFacilityInner"
                  :facility="facility"
                  :facilities="facilities"
                  :role="role"
                  :users="users"
                  :userNames="userNames"
              />
            </v-card>
<!--            <v-card-text>-->
<!--              Ответственный:-->
<!--              <div style="font-weight: bold">-->
<!--              {{ subFacilityInner.user && subFacilityInner.user.role ? subFacilityInner.user.name + ", " +  subFacilityInner.user.role.toLowerCase(): ''}}-->
<!--              </div>-->
<!--            </v-card-text>-->
<!--            <br>-->
            <br>
            <v-autocomplete
                density=0
                label="Выбрать ответственного"
                variant="underlined"
                @click="setIndexSubFacilityClicked(subFacilityInner.id)"
                @update:model-value="userNameSelect"
                :items="users.map(u => u.name)"
                :item-value="userNameSelected"
            >
            </v-autocomplete>
            <div v-if="subFacilityIndex === subFacilityInner.id" v-show="showConfirmBtnSubFacility" style="margin-top: 5px">
              <v-btn  color="green"  @click="editSubFacility(subFacilityInner)" > Назначить </v-btn>
              <v-btn color="red"  @click="hideConfirmBtnFunctionSubFacility" > Отмена </v-btn>
            </div>
          </div>
        </v-col>
        <v-spacer></v-spacer>
        <v-col  align="end" cols="auto">
        </v-col>
      </v-row>
    </div>
    <div v-if="!subFacilities[0]" >
      <v-card @click="turnOverlayAndSetFacilityToJobList(overlay, facility)">
        <v-row>
          <v-col cols="10">
            <v-card-title primary-title>
              {{facility.name}}
            </v-card-title>
            <v-card-text>
              Ответственный:
              <div style="font-weight: bold">
              {{ currentFacilityUser && currentFacilityUser.role ? currentFacilityUser.name + ", " +  currentFacilityUser.role.toLowerCase(): ''}}
              </div>
            </v-card-text>
          </v-col>
          <v-spacer></v-spacer>
        </v-row>
      </v-card>
      <br>
      <v-autocomplete
          density=0
          label="Выбрать ответственного"
          variant="underlined"
          @update:model-value="userNameSelect"
          :items="users.map(u => u.name)"
          :item-value="userNameSelected"
      >
      </v-autocomplete>
      <div v-show="showConfirmBtnFacility" style="margin-top: 5px">
        <v-btn  color="green"  @click="editFacility(facility)" > Назначить </v-btn>
        <v-btn color="red"  @click="hideConfirmBtnFunctionFacility" > Отмена </v-btn>
      </div>
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
      userNameSelected: '',
      subFacilities: [],
      showConfirmBtnSubFacility: '',
      showConfirmBtnFacility: '',
      user: '',
      userId: '',

      currentFacilityUser: this.facility.user,

      heightSubFacilityCard: '110px',
      subFacilityIndex: ''
    }
  },
  components: {SubFacilityRow},
  props: ['facility', 'editFacility','facilities', 'role',
    'deleteSubFacility', 'subFacility', 'setSubFacility',
    'turnOverlayAndSetFacilityToJobList', 'turnOverlayAndSetSubFacilityToJobList',
    'users', 'userNames','url', 'port'], // получаем переменную facility
  computed: {
  },
  mounted() {
    this.currentFacilityUser = this.facility.user
  },
  created: function () {
    this.facility.subFacilities ? this.facility.subFacilities.forEach( s => this.subFacilities.push(s)) : []
  },
  methods: {
    userNameSelect: function (userNameSelected) {
      this.userNameSelected = userNameSelected;
      this.user = this.users.find(u => (u ? u.name === this.userNameSelected : false));
      this.userId = this.user.id

      this.showConfirmBtnSubFacility = true

      this.showConfirmBtnFacility = true
    },
    hideConfirmBtnFunctionFacility: function () {
      this.showConfirmBtnFacility = false
    },
    hideConfirmBtnFunctionSubFacility: function () {
      this.showConfirmBtnSubFacility=false
      this.heightSubFacilityCard = '110px'

    },
    editFacility: function (facilityOrSubFacilityObject) {

      var user = this.users.filter(u => (u.name === this.userNameSelected));

      // если объект
      var facility = facilityOrSubFacilityObject;

      console.log(facility)
      console.log(this.user)

      facility.user = {id: this.user.id}

     this.currentFacilityUser = this.user
      console.log('dd' + facility.user.name)
      console.log('ddf' + this.currentFacilityUser.name)

      // Назначаем отвественного сотрудника
      this.axios.put(`api/facility/${facility.id}`, facility).then(result => {
        if (result.status === 200) {
          this.showConfirmBtnFacility = false
          // console.log(result.data)
          // this.currentFacilityUser = result.data.user
        } else {

        }
      })
    },
    editSubFacility: function (facilityOrSubFacilityObject) {

      // если подобьект
      var subFacility = facilityOrSubFacilityObject;

      console.log(subFacility)
      console.log(this.user)

      subFacility.user = {id: this.user.id}

      this.subFacilities.find(el => el.id == subFacility.id).user = this.user

      // Назначаем отвественного сотрудника
      this.axios.put(`api/subFacility/${subFacility.id}`, subFacility).then(result => {
        if (result.status === 200) {
          this.showConfirmBtnSubFacility = false
        } else {

        }
      })
    },

    setIndexSubFacilityClicked: function (index) {
      this.subFacilityIndex = index
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

<template xmlns="http://www.w3.org/1999/html">
  <html xmlns:th="https://www.thymeleaf.org">
  <v-card v-bind:height="((overlay || overlayToGiving) ? 1500 : '')" color="#7d7f7d" rounded="lg"  class="ma-2" >
    <v-row justify="center" align="center">
      <v-col row  wrap align-center height="150px">
        <br>

        <div align="center" >
          <v-sheet rounded="lg" style="background-color: #7d7f7d" width="200px" height="250px">
            <v-sheet rounded="lg" width="200px" height="200px">
              <v-img
                  density="0"
                  v-if="(this.imageEditButton)"
                  v-bind:src=" 'upload/files/' + (tool ? tool.image : '')"
                  class="text-white"
                  height="150px"

              />
            </v-sheet>
          </v-sheet>
        </div>
      </v-col>
      <v-col>
        <div align="center" >
          <v-sheet v-if=" tool ? tool.name : '' " rounded="lg" style="background-color: #7d7f7d" width="300px">
            <v-card-title>
              {{ tool ? tool.name : ''}}
              {{ tool ? tool.article : '' }}
            </v-card-title>
          </v-sheet>
        </div>
      </v-col>
      <v-col >
        <div align="center" >
          <v-sheet v-if=" tool ? tool.article : '' " rounded="lg" style="background-color: #7d7f7d" width="300px">

          </v-sheet>
        </div>
      </v-col>
      <v-col >
        <div align="center" >
          <v-sheet v-if=" tool ? tool.vendor : '' " rounded="lg" style="background-color: #7d7f7d" width="300px">
            {{ tool ? tool.vendor : '' }}
          </v-sheet>
        </div>
      </v-col>
      <v-col row  wrap align-center width="300px">
        <v-sheet  style="background-color: #7d7f7d" >
          <v-col justify="center" align="center">

            <div align="center">
              <v-card-title v-if="!showConfirmText">
                <div>
                  от {{ tool ? tool.user ? tool.user.name : '' : ''}}
                </div>
                <div>
                  из {{ tool ? tool.facility ? tool.facility.name : 'ниоткуда' : ''}}
                </div>
                <br>
                <br>
              </v-card-title>
            </div>
            <v-row justify="center" align="center" >
              <v-card-title style="color: green" v-if="showConfirmText">
                Инструмент принят!
              </v-card-title>
              <v-btn v-if="!showConfirmBtn" size="x-large" color="#EBB652"  align="center" @click="confirm">
                Принять
              </v-btn>
            </v-row>
            <br>
            <br>
            <v-row justify="center" align="center">
              <v-card-title style="color: red" v-if="showCancelText">
                Инструмент отклонен!
              </v-card-title>
              <v-btn v-if="!showConfirmBtn" size="large" color="red" align="center" @click="cancel">
                Отклонить
              </v-btn>
            </v-row>
            <br>
            <br>
            <br>
          </v-col>
          <br>
        </v-sheet>
      </v-col>

    </v-row>
  </v-card>
  </html>
</template>

<script>
import { isReactive, unref, toRaw } from 'vue'
import DecriptionTool from "./decriptionTool.vue";
import GivingTool from "./givingTool.vue";

function getRawData (data) {
  return isReactive(data) ? toRaw(data) : data
}

function toDeepRaw (data) {
  const rawData = getRawData(data)
  for (const key in rawData) {
    const value = rawData[key]
    if (!isObject(value) && !Array.isArray(value)) {
      continue
    }
    rawData[key] = toDeepRaw(value)
  }
  return rawData // much better: structuredClone(rawData)
}

export default {
  components: {DecriptionTool,GivingTool},
  props: ['profile', 'role', 'tool', 'editTool', 'toolSets', 'deleteTool', 'userNames' , 'users', 'profileId'],
  data() {
    return {
      imageEditButton: false,
      facilityNameSelected: '',
      overlay: false,
      facilityNames: [],
      facilitiesArray: [],
      facilities: [],
      showConfirmBtn: false,
      overlayToGiving: false,
      showConfirmText: false,
      showCancelText: false,
      event: '',
      events: []
    }
  },
  mounted: function() {

    // Запрашиваем события которые на перемещение
    this.axios.get( `api/event/user/moving/${this.tool.id}`).then(res => {
          res.data.forEach(f => {
                this.events.push(f)
              }
          )
          console.log("this.events")
          console.log(this.events)
        }
    )

    // Запрашиваем отчеты
    this.axios.get( "api/facility").then(res => {
          res.data.forEach(f => {
                this.facilities.push(f)
              }
          )
          this.facilities.forEach( f => this.facilityNames.push(f.name))
        }
    )

    this.imageEditButton = this.tool ? this.tool.image : ''
    // this.facilityNameSelected = this.toolFacilityNameIfPresent(this.tool)
  },
  methods: {
    showConfirmBtnFunc: function () {
      this.showConfirmBtn=true
    },
    hideConfirmBtnFunc: function () {
      this.showConfirmBtn=false
    },
    imageEditFunc: function () {
      this.imageEditButton = !this.imageEditButton
    },
    confirm: function () {
      var newEvent = {
        name: 'Передан',
      }

      this.axios.put(`api/event/confirmtool/${this.tool.id}`, newEvent).then(result => {
        if (result.status === 200) {
          this.event = newEvent

          var tool = this.tool
          tool.user = {id : this.profileId ? this.profileId : '', name : this.profile ? this.profile : ''}
          this.assignUser(tool)
          console.log("this.events[0].toFacility")
        }
      })
    },
    assignUser: function (tool) {
      this.axios.put(`api/tool/${tool.id}`, tool).then(result => {
        if (result.status === 200) {
          console.log(result.data)

          this.assignFacility(this.events[0].toFacility, tool)

          this.showConfirmBtn = true
          this.showConfirmText = true
        }
      })
    },
    assignFacility: function (facilityInner, toolInner) {
      var facility = this.facilities.find( f => f.name === facilityInner)
      console.log("facility to Assign")
      console.log(facility)
      toolInner.facility = {id : facility ? facility.id : '', name : facility ? facility.name : ''}
      this.axios.put(`api/tool/${toolInner.id}`, toolInner).then(result => {
        if (result.status === 200) {
          console.log('assignFacility ответ ---->>> ')
          console.log(result.data)
        }
      })}
    ,
    cancel: function () {
      var newEvent = {
        name: 'Отклонен',
      }

      this.axios.put(`api/event/confirmtool/${this.tool.id}`, newEvent).then(result => {
        if (result.status === 200) {
          console.log(result.data)
          this.showConfirmBtn = true
          this.showCancelText = true

        }
      })
    },
    closeDescriptionToolByDeleteConfirm: function () {
      this.overlay = false;
    },
  }
}
</script>

<style scoped>

</style>

<template xmlns="http://www.w3.org/1999/html">
  <v-sheet v-bind:height="((overlay || overlayToGiving) ? 1500 : '')" align="center" width="350" height="310" style="background-color: black">
    <v-card fluid v-bind:height="(290)" width="320"  color="#7d7f7d" rounded="lg"  >
      <v-sheet color="#7d7f7d">
        <v-row >
          <v-col offset="0" cols="2" align-self="start">
            <v-card :ripple="false" class="align-center justify-center d-flex flex-column"
                    align="center" height="300"
                    color="red" @click="cancel">
              <v-icon  icon="mdi-cancel" >
              </v-icon>
            </v-card>
          </v-col>
          <v-col  align-self="start" cols="2">
            <v-row justify="center" align="center" style="margin-top: 24px;  margin-bottom: 15px;">
              <v-sheet style="background-color: #7d7f7d; " >
                <v-row justify="center" align="center" >
                  <v-sheet class="text-subtitle-1" color="#b0aeae"  align="center"
                           style="background-color: #a3a2a2;font-weight: bold ; font-size: 14px; margin-left: 160px" width="350">
                    {{ tool.name }}
                  </v-sheet>
                </v-row>
              </v-sheet>
            </v-row>
            <v-sheet width="150" style="background-color: #7d7f7d;" >
              <v-row align-content="start">
                <v-col  row  wrap align-self="start">
                  <v-row style="margin-top: 5px; margin-bottom: 0px;" >
                    <v-sheet rounded="lg" style="background-color: #7d7f7d" width="100px" height="100px">
                      <v-card :ripple="false" rounded="lg" width="100px" height="100px" align="start">
                        <v-img
                            density="0"
                            v-if="(this.imageEditButton)"
                            v-bind:src=" 'upload/files/' + (tool ? tool.image : '')"
                            class="text-white"
                            height="100px"
                        />
                      </v-card>
                    </v-sheet>
                  </v-row>
                </v-col>
                <v-col align="start" style="vertical-align: center" >
                  <v-sheet width="40" style="background-color: #7d7f7d; padding-top: 1%">
                    <v-sheet class=" pa-0 text-subtitle-1" align="start" style="background-color: #7d7f7d;
                    font-size: 12px; font-weight: bold; " width="100px" >
                      <div v-if="tool.model">
                        {{ tool.model }}
                      </div>
                    </v-sheet>
                    <v-sheet class=" pa-0 text-subtitle-1" align="start" style="background-color: #7d7f7d;
                    font-size: 12px; font-weight: bold; " width="100px" >
                      <div v-if="tool.article">
                        {{ tool.article }}
                      </div>
                    </v-sheet>
                    <v-sheet class=" pa-0 text-subtitle-1" align="start" style="background-color: #7d7f7d;
                    font-size: 12px; " width="100px" >
                      <div v-if="tool.vendor">
                        {{ tool.vendor }}
                      </div>
                    </v-sheet>
                    <v-sheet class=" pa-0 text-subtitle-2" align="start" style="background-color: #7d7f7d;
                    font-size: 12px; " width="100px" >
                      <div v-if="tool.serial">
                        {{ tool.serial }}
                      </div>
                    </v-sheet>
                  </v-sheet>
                  <v-sheet v-if="tool.article" align="center"  style="background-color: #7d7f7d;" width="110px;" >
                    <!--          <v-row v-if="!moveStatus" justify="center" align="center" class=" pa-1">-->
                    <!--            <br>-->
                    <!--            с {{ event.eventTimeAndDate }}-->
                    <!--            <br>-->
                    <!--            от {{ event.user ? event.user.name : '' }}-->
                    <!--          </v-row>-->
                  </v-sheet>
                </v-col>
              </v-row>
            </v-sheet>
            <v-row justify="center" align="center">
              <br>
              <v-sheet style="background-color: #a3a2a2; margin-left: 30px"  >
                <v-row justify="center" align="center" >
                  <v-sheet color="#a3a2a2"  align="center" style=" font-size: 18px; font-weight: bold; margin-left: 150px" width="350">
                    <div v-if="tool ? (tool.user ? tool.user.name : '') : ''">
                      от {{ tool ? tool.user ? tool.user.name : '' : ''}}
                    </div>
                  </v-sheet>
                  <v-sheet color="#a3a2a2"  align="center" style=" font-size: 18px; margin-left: 150px" width="320">
                    <div>
                      из {{ tool ? tool.facility ? tool.facility.name : 'ниоткуда' : ''}}
                    </div>
                  </v-sheet>
                </v-row>
              </v-sheet>
              <v-sheet style="background-color: #a3a2a2; margin-left: 30px">
                <v-row justify="center" align="center" >
                  <v-sheet color="#a3a2a2"  align="center" style=" font-size: 18px; font-weight: bold; margin-left: 150px" width="320">
                    <v-card-title style="color: green;" v-if="showConfirmText">
                      Инструмент принят!
                    </v-card-title>
                    <v-card-title style="color: red" v-if="showCancelText">
                      Инструмент отклонен!
                    </v-card-title>
                  </v-sheet>
                </v-row>
              </v-sheet>
            </v-row>
          </v-col>
          <v-col offset="6" cols="2" align-self="end">
            <v-card :ripple="false" class="align-center justify-center d-flex flex-column"
                    align="center" height="300"  style="background-color: #EBB652;"
                    color="E75158" @click="confirm">
              <v-icon  icon="mdi-check" >
              </v-icon>
            </v-card>
          </v-col>
        </v-row>
      </v-sheet>
    </v-card>
  </v-sheet>
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

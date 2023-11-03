<template xmlns="http://www.w3.org/1999/html">
  <v-sheet align="center" width="300" style="background-color: black">

    <v-card  fluid v-bind:height="(178)" width="300" color="#7d7f7d" rounded="lg"  >
      <v-sheet width="300" color="#7d7f7d" >
        <v-row align="center" justify="center">
          <v-col align-self="start">
            <v-sheet width="240" color="#7d7f7d" style="background-color: #7d7f7d;" >
              <v-row >
                <v-col row  wrap >
                  <v-row style="margin-left: 10px; margin-top: 10px; margin-bottom: 10px" >
                    <v-sheet rounded="lg" style="background-color: #7d7f7d" width="100px" height="100px">
                      <v-card rounded="lg" width="100px" height="100px" align="center" @click="overlayFuncInner">
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
                <v-col>
                  <v-sheet width="110" style="background-color: #7d7f7d;">
                    <v-card-title  class=" pa-0" align="start" style="background-color: #7d7f7d; font-size: 15px;
                    font-weight: bold" width="100px" >
                      {{ tool.name }}
                    </v-card-title>
                    <v-sheet class=" pa-0" align="start" style="background-color: #7d7f7d; font-size: 14px;" width="110px" >
                      <div v-if="tool.article">
                        {{ tool.article }}
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
                    <v-row justify="center" align="center" >
                      <v-select
                          style="margin-top: 20px; margin-left:10px; "
                          outlined
                          density="0"
                          class="inline custom-select"
                          label="Объект"
                          variant="outlined"
                          @update:modelValue="selectFacility"
                          v-model="facilityNameSelected"
                          :items="facilityNames"
                          :item-value="facilityNameSelected"
                      >
                      </v-select>
                    </v-row>
                    <v-row justify="center" align="center">
                      <div v-if="showConfirmBtn">
                        <v-btn v-if="showConfirmBtn" width="80" color="green" size="small" @click="edit" >
                          <div style="font-size: 8px">
                            Переместить
                          </div>
                        </v-btn>
                        <v-btn v-if="showConfirmBtn" width="20" color="red" size="small" @click="hideConfirmBtnFunc" >
                          <div style="font-size: 8px">
                            Отмена
                          </div>
                        </v-btn>
                        <br>
                        <br>
                      </div>
                    </v-row>
                  </v-sheet>
                </v-col>
              </v-row>
              <v-row>
                <v-sheet style="background-color: #7d7f7d;" width="300px" >
                  <v-row justify="center" align="center">
                    <v-sheet color="#EBB652"  align="center" style="background-color: #EBB652;" width="300">
                      <div v-if="tool ? (tool.user ? tool.user.name : '') : ''">
                        Прикреплен к: {{ tool ? (tool.user ? tool.user.name : '') : ''}}
                      </div>
                    </v-sheet>
                    <v-sheet color="red"  align="center" style="background-color: #7d7f7d;" width="300">
                      <div v-if="moveStatus">
                        Направлен: {{ (movingEvent[0]) ? movingEvent[0].toUser : ''}}
                      </div>
                    </v-sheet>
                  </v-row>
                </v-sheet>
              </v-row>
            </v-sheet>
          </v-col>
          <v-col>
            <v-card class="align-center justify-center d-flex flex-column"
                    align="center" height="178"  style="background-color: #EBB652;"
                    color="green" @click="overlayToGivingFuncInner">
              <v-icon  icon="mdi-send" >
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
  components: {DecriptionTool, GivingTool},
  props: ['profile', 'role', 'tool', 'editTool', 'toolSets', 'deleteTool', 'userNames' , 'users',
    'profileId', 'toolsForRow', 'overlayToGivingFunc', 'overlayFunc', 'toolFunc', 'facilityNames', 'facilities'],
  data() {
    return {
      imageEditButton: false,
      facilityNameSelected: '',
      overlay: false,
      // facilityNames: [],
      facilitiesArray: [],
      showConfirmBtn: false,
      overlayToGiving: false,
      event: '',
      moveStatus: false,
      movingEvent: ''
    }
  },
  mounted: function() {
    // Запрашиваем события состоявшейся передачи
    this.axios.get( `api/event/user/moved/${this.tool.id}`).then(res => {

          this.event = res.data
        }
    )
    console.log("this.event")
    console.log(this.event)

    // Запрашиваем события которые на перемещение
    this.axios.get( `api/event/user/moving/${this.tool.id}`).then(res => {

          this.movingEvent = res.data
          console.log(res.data)
          if (res.data[0]) this.moveStatus = true
        }
    )
    console.log("this.event")
    console.log(this.event)

    this.imageEditButton = this.tool.image
    this.facilityNameSelected = this.toolFacilityNameIfPresent(this.tool)
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
    selectFacility: function (facilityNameSelected) {
      this.facilityNameSelected = facilityNameSelected
      this.showConfirmBtn = true
    },
    edit: function () {
      var facility = this.facilities.find( f => f.name === this.facilityNameSelected)
      var tool = this.tool
      tool.facility = {id : facility ? facility.id : '', name : facility ? facility.name : ''}
      this.assignFacility(tool)
    },
    assignFacility: function (tool) {
      this.axios.put(`api/tool/${tool.id}`, tool).then(result => {
        if (result.status === 200) {
          console.log(result.data)
          this.facilityNameSelected = result.data.facility ? result.data.facility.name : ''
          this.sendEvent()
          this.showConfirmBtn = false
        }
      })
    },
    sendEvent: function () {
      var newEvent = {
        name: 'Перемещен',
        tool: {id: this.tool.id, name: this.tool.name},
        fromFacility: this.tool.facility.name,
        toFacility: this.facilityNameSelected,
        toUser: this.profile,
      }

      this.axios.post(`api/event`, newEvent).then(result => {
        if (result.status === 200) {
          console.log(result.data)
        }
      })
    },
    toolFacilityNameIfPresent: function (tool) {
      return (tool.facility ? tool.facility.name : '')
    },
    closeDescriptionToolByDeleteConfirm: function () {
      this.overlay = false;
    },

    overlayToGivingFuncInner: function () {
      this.overlayToGiving = !this.overlayToGiving;
      this.overlayToGivingFunc(this.overlayToGivingFunc)
      this.toolFunc(this.tool)
    },
    overlayFuncInner: function () {
      this.overlay = !this.overlay;
      this.overlayFunc(this.overlayToGivingFunc)
      this.toolFunc(this.tool)
    },

  },
}
</script>

<style scoped>

</style>

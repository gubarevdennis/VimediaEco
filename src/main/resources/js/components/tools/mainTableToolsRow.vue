<template xmlns="http://www.w3.org/1999/html">
  <html xmlns:th="https://www.thymeleaf.org">
  <v-card v-bind:height="((overlay || overlayToGiving) ? 1500 : '')" color="#7d7f7d" rounded="lg"  class="ma-2" >
    <v-overlay
        contained
        v-model="overlay"
        class="align-center justify-center"
        scroll-strategy="reposition"
        align="center"
    >

      <v-btn icon="mdi-close" @click="overlay = !overlay"></v-btn>
      <br>
      <br>
      <decription-tool :closeDescriptionToolByDeleteConfirm="closeDescriptionToolByDeleteConfirm"
                       :deleteTool="deleteTool"
                       :toolSets="toolSets"
                       :editTool="editTool"
                       :profile="profile"
                       :role="role"
                       :tool="tool"></decription-tool>

    </v-overlay>
    <v-overlay
        contained
        v-model="overlayToGiving"
        class="align-center justify-center"
        scroll-strategy="reposition"
        align="center"
    >

      <v-btn icon="mdi-close" @click="overlayToGiving = !overlayToGiving"></v-btn>
      <br>
      <br>
      <giving-tool :closeDescriptionToolByDeleteConfirm="closeDescriptionToolByDeleteConfirm"
                   :deleteTool="deleteTool"
                   :facilityNames="facilityNames"
                   :facilities="facilities"
                   :toolSets="toolSets"
                   :editTool="editTool"
                   :profile="profile"
                   :role="role"
                   :userNames="userNames"
                   :users="users"
                   :tool="tool"></giving-tool>

    </v-overlay>
    <v-row justify="center" align="center">
      <v-col row  wrap align-center height="200px">
        <br>
          <v-sheet rounded="lg" style="background-color: #7d7f7d" width="200px" height="150px">
            <v-sheet rounded="lg" width="150px" height="150px">
              <v-img
                  density="0"
                  v-if="(this.imageEditButton)"
                  v-bind:src=" 'upload/files/' + (tool ? tool.image : '')"
                  class="text-white"
                  height="150px"

              />
            </v-sheet>
          </v-sheet>
        <br>
      </v-col>

      <v-col align-self="center">
        <v-sheet style="background-color: #7d7f7d" width="300px" >
          <v-card-title class="text-h6" align="center" width="200px">
            {{ tool.name }}
          </v-card-title>
        </v-sheet>
      </v-col>
      <v-col >
        <v-sheet  align="center" style="background-color: #7d7f7d" width="300px">
          <div v-if="tool.article">
          {{ tool.article }}
          </div>
        </v-sheet>
      </v-col>
      <v-col  >
        <v-sheet class="text-subtitle-1"   align="center" style="background-color: #7d7f7d" width="300px">
          <div v-if="tool.serial">
          {{ tool.serial }}
          </div>
        </v-sheet>
      </v-col>
      <v-col >
        <v-sheet  align="center" style="background-color: #7d7f7d" width="300px">
          <div v-if="tool.vendor">
          {{ tool.vendor }}
          </div>
        </v-sheet>
      </v-col>
      <v-col >
        <v-sheet class="text-subtitle-1" align="center"  style="background-color: #7d7f7d" width="300px">
          <div v-if="tool.quantity">
          {{ tool.quantity }} шт.
          </div>
        </v-sheet>
      </v-col>
      <v-col row  wrap align-center width="300px">
        <v-sheet class="text-subtitle-1" v-if="tool.article" align="center"  style="background-color: #7d7f7d" width="300px">
          <v-row justify="center" align="center">
            <br>
            с {{ event.eventTimeAndDate }}
            <br>
            от {{ event.user ? event.user.name : '' }}
          </v-row>
          <v-row justify="center" align="center">
            <v-select
                label="Объект"
                variant="outlined"
                @update:modelValue="selectFacility"
                v-model="facilityNameSelected"
                :items="facilityNames"
                :item-value="facilityNameSelected"
            >
            </v-select>
          </v-row>
          <div>
            <v-btn v-if="showConfirmBtn" color="green" size="small" @click="edit" > Переместить </v-btn>
            <v-btn v-if="showConfirmBtn" color="red" size="small" @click="hideConfirmBtnFunc" > Отмена </v-btn>
          </div>
        </v-sheet>
      </v-col>
      <v-col row  wrap align-center width="300px">
        <v-sheet  style="background-color: #7d7f7d" width="300px">
          <v-row justify="center" align="center">
            <v-btn v-if="((this.role === 'Кладовщик') || (this.role === 'Директор'))" align="center" @click="overlay = !overlay">
              Подробнее
            </v-btn>
            &nbsp
            &nbsp
            &nbsp
            &nbsp
            <v-btn color="#EBB652" align="center" @click="overlayToGiving = !overlayToGiving">
              Передать
            </v-btn>
            <br>
            <br>
            <br>
          </v-row>
          <br>
          <v-col >
            <v-sheet  align="center" style="background-color: #7d7f7d; color: red" width="300px">
              <div v-if="moveStatus">
                Направлен коллеге {{ (movingEvent[0]) ? movingEvent[0].toUser : ''}}
              </div>
            </v-sheet>
          </v-col>
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
  props: ['profile', 'role', 'tool', 'editTool', 'toolSets', 'deleteTool', 'userNames' , 'users'],
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

    // Запрашиваем отчеты
    this.axios.get( "api/facility").then(res => {
          res.data.forEach(f => {
                this.facilities.push(f)
              }
          )
          this.facilities.forEach( f => this.facilityNames.push(f.name))
        }
    )

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
  }
}
</script>

<style scoped>

</style>

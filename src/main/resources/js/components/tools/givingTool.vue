<template>
  <html style="max-height: 80vh; max-width: 90vh">
  <v-sheet
      rounded="lg"
      contained
      align="center"
  >
    <br>
    <v-col>
      <v-row justify="center" align="center">
        <v-col>
          <form method="POST" enctype="multipart/form-data" action="/upload">
            <input name="toolId" type="hidden" v-bind:value="(tool ? tool.id : '')" />
            <v-card width="260px" >
              <v-img
                  density="0"
                  v-bind:src=" 'upload/files/' + (tool ? tool.image : '')"
                  class="text-white"
                  height="260px"
              />
            </v-card>
          </form>
          <v-sheet style="width: 250px;">
            <div style="font-weight: bold; ">
              {{tool ? tool.name : ''}}
            </div>
            <div>
              {{tool ? tool.article : ''}}
            </div>
          </v-sheet>
        </v-col >
        <v-col  style="font-size: 40px">
          <v-card-text align="start">
            <v-row align="start">
              <div style="font-weight: bold;color: #0B0B0B"> Наименование: &nbsp</div>
              {{ tool ? tool.name : '' }}
            </v-row>
          </v-card-text>
          <v-card-text >
            <v-row>
              <div style="font-weight: bold;color: #0B0B0B"> Модель: &nbsp</div>
              {{ tool.model }}
            </v-row>
          </v-card-text>
          <v-card-text >
            <v-row>
              <div style="font-weight: bold;color: #0B0B0B"> Артикул: &nbsp</div>
              {{ tool.article }}
            </v-row>
          </v-card-text>
          <v-card-text >
            <v-row>
              <div style="font-weight: bold;color: #0B0B0B"> Комплектность: &nbsp</div>
              {{ tool.toolSet ? tool.toolSet.name : 'Не входит в комплект'}}
            </v-row>
          </v-card-text>
          <v-card-text >
            <v-row>
              <div style="font-weight: bold;color: #0B0B0B"> Инвентарный номер: &nbsp</div>
              {{ tool.id }}
            </v-row>
          </v-card-text>
          <v-card-text >
            <v-row>
              <div style="font-weight: bold;color: #0B0B0B"> Серийный номер: &nbsp</div>
              {{ tool.serial }}
            </v-row>
          </v-card-text>
          <v-card-text >
            <v-row>
              <div style="font-weight: bold;color: #0B0B0B"> Производитель: &nbsp</div>
              {{ tool.vendor }}
            </v-row>
          </v-card-text>
          <v-card-text>
            <v-row>
              <div style="font-weight: bold;color: #0B0B0B"> Стоимость: &nbsp</div>
              {{ tool.price }}
            </v-row>
          </v-card-text>
          <v-card-text>
            <v-row>
              <div style="font-weight: bold;color: #0B0B0B"> Поставщик: &nbsp</div>
              {{ tool.provider }}
            </v-row>
          </v-card-text>
          <v-card-text>
            <v-row>
              <div style="font-weight: bold;color: #0B0B0B"> Информация о закупке: &nbsp</div>
              {{ tool.paymentInfo }}
            </v-row>
          </v-card-text>
          <v-card-text>
            <v-row>
              <div style="font-weight: bold;color: #0B0B0B"> Состав: &nbsp</div>
              {{ tool.composition }}
            </v-row>
          </v-card-text>
          <v-card-text>
            <v-row>
              <div style="font-weight: bold;color: #0B0B0B"> Описание: &nbsp</div>
              {{ tool.description }}
            </v-row>
          </v-card-text>
          <v-card-text>
            <v-row>
              <div style="font-weight: bold;color: #0B0B0B"> Комментарий: &nbsp</div>
              <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                    v-bind:rowInputText="tool ? tool.text : ''" v-bind:rowInputType="'text'" ></description-tool-row>
            </v-row>
          </v-card-text>
        </v-col>
      </v-row>
    </v-col>
    <v-divider></v-divider>
    <v-sheet class="text-subtitle-1" align="center" style="max-width: 280px">
      <v-text align="center">
        <br>
        <v-row v-if="!toolSendConfirmField" justify="center" align="center" style="font-weight: bold; color: #0B0B0B">
          Передать
        </v-row>
        <v-row v-if="!toolSendConfirmField" justify="center" align="center" style="font-weight: bold; color: red">
          {{tool ? tool.name : ''}}
        </v-row>
        <v-row v-if="!toolSendConfirmField" justify="center" align="center" style="font-weight: bold; color: red">
          {{tool ? tool.article : ''}}  ?
        </v-row>
      </v-text>
    </v-sheet>
    <br>
    <br>
    <v-row justify="center" justify-sm="center" >
      <v-text v-if="toolSendConfirmField" style="color: green; font-size: 18px;" >
        Интструмент направлен на передачу!
        <br>
        <br>
      </v-text>
    </v-row>
    <v-sheet class="text-subtitle-1" v-if="tool.article" align="center" style="max-width: 280px">
      <v-row v-if="!toolSendConfirmField" justify="center" align="center">
        <v-autocomplete
            label="Кому ?"
            variant="outlined"
            @update:modelValue="selectUser"
            v-model="userNameSelected"
            :items="userNames"
            :item-value="userNameSelected"
        >
        </v-autocomplete>
      </v-row>
      <v-row v-if="!toolSendConfirmField" justify="center" align="center">
        <v-autocomplete
            label="На какой объект ?"
            variant="outlined"
            @update:modelValue="selectFacility"
            v-model="facilityNameSelected"
            :items="facilityNames"
            :item-value="facilityNameSelected"
        >
        </v-autocomplete>
      </v-row>
      <div>
        <v-btn v-if="showConfirmBtn && !toolSendConfirmField" color="#EBB652" size="large" @click="sendEvent" > Передать </v-btn>
        &nbsp
        <v-btn v-if="showConfirmBtn && !toolSendConfirmField" color="red" size="large" @click="hideConfirmBtnFunc" > Отмена </v-btn>
      </div>
      <br>
    </v-sheet>
  </v-sheet>
  </html>
</template>

<script>

import AddDescriptionTool from "./addDescriptionTool.vue";
export default {
  name: "addTool",
  components: {AddDescriptionTool},
  data() {
    return {
      imageEditButton: false,
      userNameSelected: '',
      facilityNameSelected: '',
      overlay: false,
      showConfirmBtn: false,
      toolSendConfirmField: false
    }
  },
  mounted() {
    this.toolSendConfirmField = false

    this.facilityNameSelected = this.toolFacilityNameIfPresent(this.tool)
  },
  methods: {
    showConfirmBtnFunc: function () {
      if ( this.facilityNameSelected !== '' && this.userNameSelected !== '') {
        this.showConfirmBtn = true
      }
    },
    hideConfirmBtnFunc: function () {
      this.showConfirmBtn=false
      this.facilityNameSelected = ''
      this.userNameSelected = ''
    },
    imageEditFunc: function () {
      this.imageEditButton = !this.imageEditButton
    },
    selectUser: function (userNameSelected) {
      this.userNameSelected = userNameSelected
      // Если заполнены оба поля только тогда можно передать инструмент
      if ( this.facilityNameSelected !== '' && this.userNameSelected !== '') {
        this.showConfirmBtn = true
      }
    },
    selectFacility: function (facilityNameSelected) {
      this.facilityNameSelected = facilityNameSelected
      // Если заполнены оба поля только тогда можно передать инструмент
      if ( this.facilityNameSelected !== '' && this.userNameSelected !== '') {
        this.showConfirmBtn = true
      }
    },
    toolFacilityNameIfPresent: function (tool) {
      return (tool.facility ? tool.facility.name : '')
    },
    sendEvent: function () {
      var newEvent = {
        name: 'Направлен на передачу',
        tool: {id: this.tool.id, name: this.tool.name},
        fromFacility: this.tool.facility ? this.tool.facility.name : 'Нет объекта',
        toFacility: this.facilityNameSelected,
        toUser: this.userNameSelected
      }

      this.axios.post(`api/event`, newEvent).then(result => {
        if (result.status === 200) {
          this.toolSendConfirmField = true
          console.log(result.data)
        }
      })
    },
  }
  ,
  props: ['profile', 'role', 'facilities', 'editTool', 'toolSets', 'addTool' , 'tool', 'facilityNames', 'userNames', 'users'],
}
</script>

<style scoped>

</style>

<template>
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
            <v-card width="300px" >
              <v-img
                  density="0"
                  v-bind:src=" 'upload/files/' + (tool ? tool.image : '')"
                  class="text-white"
                  height="300px"
              />
            </v-card>
          </form>
          <v-card-title>
            <div style="font-weight: bold;">
              {{tool ? tool.name : ''}}
            </div>
            <div>
              {{tool ? tool.article : ''}}
            </div>
          </v-card-title>
        </v-col >
        <v-col  style="font-size: 40px">
          <v-card-text >
            <v-row>
              <div style="font-weight: bold;color: #0B0B0B"> Наименование: &nbsp</div>
              {{ tool.name }}
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
              <div style="font-weight: bold;color: #0B0B0B"> Колличество: &nbsp</div>
              {{ tool.quantity }}
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
              <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                    v-bind:rowInputText="tool ? tool.composition : ''" v-bind:rowInputType="'composition'" ></description-tool-row>
            </v-row>
          </v-card-text>
          <v-card-text>
            <v-row>
              <div style="font-weight: bold;color: #0B0B0B"> Описание: &nbsp</div>
              <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                    v-bind:rowInputText="tool ? tool.description : ''" v-bind:rowInputType="'description'" ></description-tool-row>
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
    <v-card-title>
      <br>
      <v-row v-if="!toolSendConfirmField" align="center" justify="center">
        <div     style="font-weight: bold; font-size: 25px">
          Передать &nbsp
        </div>
        <div     style="font-weight: bold; color: red; font-size: 25px">
          {{tool ? tool.name : ''}} {{tool ? tool.article : ''}} &nbsp
        </div>
        <div     style="font-weight: bold; font-size: 25px">
          ?
        </div>
      </v-row>
    </v-card-title>
    <br>
    <br>
    <v-row justify="center" justify-sm="center">
      <v-text v-if="toolSendConfirmField" style="color: green; font-size: 18px;" >
        Интструмент направлен на передачу!
        <br>
        <br>
      </v-text>
    </v-row>
    <v-sheet class="text-subtitle-1" v-if="tool.article" align="center"  width="300px">
      <v-row v-if="!toolSendConfirmField" justify="center" align="center">
        <v-select
            label="Кому ?"
            variant="outlined"
            @update:modelValue="selectUser"
            v-model="userNameSelected"
            :items="userNames"
            :item-value="userNameSelected"
        >
        </v-select>
      </v-row>
      <v-row v-if="!toolSendConfirmField" justify="center" align="center">
        <v-select
            label="На какой объект ?"
            variant="outlined"
            @update:modelValue="selectFacility"
            v-model="facilityNameSelected"
            :items="facilityNames"
            :item-value="facilityNameSelected"
        >
        </v-select>
      </v-row>
      <div>
        <v-btn v-if="showConfirmBtn && !toolSendConfirmField" color="#EBB652" size="large" @click="sendEvent" > Передать </v-btn>
        &nbsp
        &nbsp
        &nbsp
        &nbsp
        <v-btn v-if="showConfirmBtn && !toolSendConfirmField" color="red" size="large" @click="hideConfirmBtnFunc" > Отмена </v-btn>
      </div>
      <br>
    </v-sheet>
  </v-sheet>
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

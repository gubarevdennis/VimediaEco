<template>
  <div v-for="tool in sortedTools">
  <confirm-tool-row
                    :users="users"
                    :userNames="userNames"
                    :role="role"
                    :profileId="profileId"
                    :profile="profile"
                    :toolSets="toolSets"
                    :tool="tool"
  ></confirm-tool-row>
  </div>

</template>

<script>

import ConfirmToolRow from "./confirmToolRow.vue";
export default {
  name: "addTool",
  components: {ConfirmToolRow},
  data() {
    return {
      imageEditButton: false,
      userNameSelected: '',
      facilityNameSelected: '',
      overlay: false,
      showConfirmBtn: false,
      toolSendConfirmField: false,
      toolToConfirm: [],
      sortedTools: []
    }
  },
  mounted() {

   // Запрашиваем инструменты для подтверждения
    this.axios.get( `api/toolforsend/confirm`).then(tools => {
      console.log(tools)
          tools.data.forEach(t =>
              this.toolToConfirm.push(t))
          this.sortedTools = this.sortToolFunc(this.toolToConfirm)

        }
    )

    console.log('toolToConfirm')
    console.log(this.toolToConfirm)

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
      return (tool ? tool.facility ? tool.facility.name : '' : '')
    },
    sortToolFunc: function (tools) {
      return tools.sort((a, b) => {
        if (a.id > b.id) {
          return -1;
        }
        if (a.id < b.id) {
          return 1;
        }
        return 0;
      })
    },
    sendEvent: function () {
      var newEvent = {
        name: 'Передан',
        tool: {id: this.tool.id, name: this.tool.name},
        fromFacility: this.tool ? this.tool.facility ? this.tool.facility.name : 'Нет объекта' : 'Нет объекта',
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
  props: ['profile', 'role', 'facilities', 'editTool', 'toolSets', 'addTool' , 'tool', 'facilityNames', 'userNames', 'users', 'profileId'],
}
</script>

<style scoped>

</style>

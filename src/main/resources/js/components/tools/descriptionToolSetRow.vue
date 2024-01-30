<template>
  <div style="color: #0B0B0B; font-size: 18px" align="center">
    <input style="width: 220px" type="text" @change="showConfirmBtnFunc" v-model="toolInfo" />
    <v-btn align="end" color="red"  @click="del" icon="mdi-delete"> </v-btn>
    <div style="margin-top: 5px">
      <v-btn v-if="showConfirmBtn" color="green"  @click="edit" > Применить </v-btn>
      <v-btn v-if="showConfirmBtn" color="red"  @click="hideConfirmBtnFunc" > Отмена </v-btn>
    </div>
  </div>
</template>

<script>
export default {
  name: "descriptionToolSetRow",
  props: ['profile', 'role', 'toolSet', 'rowInputText' , 'editToolSet', 'rowInputType', 'showConfirmBtnFuncParrent', 'deleteToolSet'],
  data() {
    return {
      imageEditButton: false,
      facilityNameSelected: '',
      showConfirmBtn: false,
      toolInfo: '',
      toolEdit: ''
    }
  },
  mounted() {
    this.toolInfo = this.rowInputText
    console.log(this.rowInputText)
  },
  methods: {
    showConfirmBtnFunc: function () {
      this.showConfirmBtn=true
      this.showConfirmBtnFuncParrent(this.rowInputType, this.toolInfo)
    },
    hideConfirmBtnFunc: function () {
      this.showConfirmBtn=false
      this.toolInfo = this.rowInputText
    },
    edit: function () {
      var toolSet = this.toolSet

      switch (this.rowInputType) {
        case 'name' :
          toolSet.name = this.toolInfo
          break;
        case 'id' :
          toolSet.id = this.toolInfo
          break;
        case 'text' :
          toolSet.text = this.toolInfo
          break;
      }

      this.axios.put(`api/toolset/${toolSet.id}`, toolSet).then(result => {
        if (result.status === 200) {
          this.editToolSet(toolSet)
          this.showConfirmBtn=false
        } else {
          this.toolInfo = this.rowInputText
          this.showConfirmBtn=false
        }
      })
    },
    del: function () {
      this.axios.delete(`api/toolset/${this.toolSet.id}`).then(result => {
        if (result.status === 200) {
          this.showInfoSuccess = true
          this.deleteToolSet(this.toolSet)
        } else {
          this.showInfoBad = true
        }
      })
    }
  },
}
</script>

<style scoped>

</style>

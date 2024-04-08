<template>
  <html style="max-height: 80vh; max-width: 90vh">
  <v-sheet
      rounded="lg"
      contained
      align="center"
  >
    <v-row justify="center" align="center"       style="margin: 10px">
      <v-col  style="font-size: 25px" >
        <v-text style="font-size: 30px; font-weight: bold"> Комплекты </v-text>
        <div>
          <v-card-text class=" pa-3">
            <v-row v-for="(toolSet,index) in toolSets" v-bind:key="toolSet.id">
              <div style="font-weight: bold; font-size: 25px ;color: #0B0B0B"> </div>
              <description-toolSet-row :editToolSet="editToolSet"  :profile="profile" :role="role" :toolSet="toolSet"
                                    v-bind:rowInputText="toolSet ? toolSet.name : ''" v-bind:rowInputType="'name'"
                                       :deleteToolSet="deleteToolSet"
                                    :showConfirmBtnFuncParrent="showConfirmBtnFuncParrent">
              </description-toolSet-row>
            </v-row>
          </v-card-text>
        </div>
        <br>
        Наименование: &nbsp
        <input style="width: 220px" type="text" @change="showConfirmBtnFunc" v-model="toolInfo" />
        <br>
        <v-btn color="#EBB652"  @click="add" > Добавить </v-btn>
      </v-col>
    </v-row>
  </v-sheet>
  </html>
</template>

<script>
import DescriptionToolRow from "./addDescriptionToolRow.vue";
import DescriptionCategoryRow from "./descriptionCategoryRow.vue";
import DescriptionToolSetRow from "./descriptionToolSetRow.vue";

export default {
  components: {DescriptionCategoryRow, DescriptionToolRow, DescriptionToolSetRow},
  props: ['profile', 'role', 'editToolSet', 'toolSets', 'addToolSet', 'editToolSet', 'deleteToolSet'],
  data() {
    return {
      imageEditButton: false,
      toolSetNameSelected: '',
      showConfirmBtn: true,
      toolSerialOnOverlay: '',
      toolSetNames: [],
      rowInputType: '',
      toolSet: {},
      showImageForm: false,
      toolInfo: '',
    }
  },
  mounted() {

    this.toolSetNameSelected = this.toolSetNameIfPresent(this.toolSet)
    // this.toolSerialOnOverlay = this.tool ?  this.tool.serial : ''
    this.toolSets.forEach(t => this.toolSetNames.push(t.name))

    console.log("ToolSetNames -> " + this.toolSetNames)
  },
  methods: {
    imageEditFunc: function () {
      this.imageEditButton = !this.imageEditButton
    },
    selectToolSet: function (toolSetNameSelected) {
      this.toolSetNameSelected = toolSetNameSelected
      var toolSetFound = this.toolSets.find(c => c.name === this.toolSetNameSelected)

      if (toolSetFound)
        this.tool.toolSet = toolSetFound
      this.showConfirmBtn = true
    },
    toolSetNameIfPresent: function (toolSet) {
      return (toolSet ? toolSet.name : '')
    },
    hideConfirmBtnFunc: function () {
      this.showConfirmBtn = false
      this.categoryNameSelected = ''
    },
    edit: function () {

    },
    showConfirmBtnFuncParrent: function () {
      console.log("Комплект - " + this.toolSet.name)

      this.showConfirmBtn = true
    },
    showConfirmBtnFunc: function () {
      this.showConfirmBtnFuncParrent(this.rowInputType, this.toolInfo)

      console.log("rowInputType - " + this.rowInputType)
      console.log("toolInfo - " + this.toolInfo)
      this.showConfirmBtn=true
    },
    add: function () {
      if (this.toolSet) {

        this.toolSet.id = '' // чтобы не заменял последний при многоразовом добавлении
        this.toolSet.name = this.toolInfo.replace(/^\s\s*/, '').replace(/\s\s*$/, '')

        this.axios.post(`api/toolset`, this.toolSet).then(result => {
          if (result.status === 200) {
            console.log(result.data)
            this.toolSet = result.data
            this.addToolSet(result.data)
            this.showConfirmBtn = false
            this.showImageForm = true;
            this.toolInfo = ''
          } else {
            this.toolSet = {}
            this.showConfirmBtn = false
          }
        })
      }
    }
  }
}
</script>

<style scoped>

</style>

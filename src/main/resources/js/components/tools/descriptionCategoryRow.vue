<template>

  <div style="color: #0B0B0B; font-size: 18px" align="center">
    <input style="width: 280px" type="text" @change="showConfirmBtnFunc" v-model="toolInfo" />
    <v-btn align="end" color="red"  @click="del" icon="mdi-delete"> </v-btn>
    <div style="margin-top: 5px">
      <v-btn v-if="showConfirmBtn" color="green"  @click="edit" > Применить </v-btn>
      <v-btn v-if="showConfirmBtn" color="red"  @click="hideConfirmBtnFunc" > Отмена </v-btn>
    </div>
  </div>
</template>

<script>
export default {
  name: "descriptionCategoryRow",
  props: ['profile', 'role', 'category', 'rowInputText' , 'editCategory', 'rowInputType', 'showConfirmBtnFuncParrent', 'deleteCategory'],
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
      var category = this.category

      switch (this.rowInputType) {
        case 'name' :
          category.name = this.toolInfo
          break;
        case 'id' :
          category.id = this.toolInfo
          break;
        case 'text' :
          category.text = this.toolInfo
          break;
      }

      this.axios.put(`api/category/${category.id}`, category).then(result => {
        if (result.status === 200) {
          this.editCategory(category)
          this.showConfirmBtn=false
        } else {
          this.toolInfo = this.rowInputText
          this.showConfirmBtn=false
        }
      })
    },
    del: function () {
      this.axios.delete(`api/category/${this.category.id}`).then(result => {
        if (result.status === 200) {
          this.showInfoSuccess = true
          this.deleteCategory(this.category)
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

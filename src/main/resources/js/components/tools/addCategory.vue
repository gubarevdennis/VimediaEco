<template>
  <html style="max-height: 80vh; max-width: 90vh">
  <v-sheet
      rounded="lg"
      contained
      align="center"
  >
    <v-row justify="center" align="center"       style="margin: 10px">
      <v-col  style="font-size: 25px" >
        <v-text style="font-size: 30px; font-weight: bold"> Категории </v-text>
        <div>
          <v-card-text class=" pa-3">
            <v-row v-for="(category,index) in categories" v-bind:key="category.id">
              <div style="font-weight: bold; font-size: 25px ;color: #0B0B0B"> </div>
              <description-category-row :editCategory="editCategory"  :profile="profile" :role="role" :category="category"
                                    v-bind:rowInputText="category ? category.name : ''" v-bind:rowInputType="'name'"
                                        :deleteCategory="deleteCategory"
                                    :showConfirmBtnFuncParrent="showConfirmBtnFuncParrent">
              </description-category-row>
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

export default {
  components: {DescriptionCategoryRow, DescriptionToolRow},
  props: ['profile', 'role', 'editCategory', 'categories', 'addCategory', 'editCategory', 'addCategory', 'deleteCategory'],
  data() {
    return {
      imageEditButton: false,
      categoryNameSelected: '',
      showConfirmBtn: true,
      toolSerialOnOverlay: '',
      categoryNames: [],
      rowInputType: '',
      category: {},
      showImageForm: false,
      toolInfo: '',
    }
  },
  mounted() {
    this.categoryNameSelected = this.categoryNameIfPresent(this.tool)
    // this.toolSerialOnOverlay = this.tool ?  this.tool.serial : ''
    this.categories.forEach(t => this.categoryNames.push(t.name))
  },
  methods: {
    imageEditFunc: function () {
      this.imageEditButton = !this.imageEditButton
    },
    selectToolSet: function (toolSetNameSelected) {
      this.categoryNameSelected = toolSetNameSelected
      var toolSetFound = this.categories.find(c => c.name === this.categoryNameSelected)

      if (toolSetFound)
        this.tool.toolSet = toolSetFound
      this.showConfirmBtn = true
    },
    categoryNameIfPresent: function (category) {
      return (category ? category.name : '')
    },
    hideConfirmBtnFunc: function () {
      this.showConfirmBtn = false
      this.categoryNameSelected = ''
    },
    edit: function () {

    },
    showConfirmBtnFuncParrent: function () {
      console.log("Категоря - " + this.category.name)

      this.showConfirmBtn = true
    },
    showConfirmBtnFunc: function () {
      this.showConfirmBtnFuncParrent(this.rowInputType, this.toolInfo)

      console.log("rowInputType - " + this.rowInputType)
      console.log("toolInfo - " + this.toolInfo)
      this.showConfirmBtn=true
    },
    add: function () {
      if (this.category) {

        this.category.id = '' // чтобы не заменял последний при многоразовом добавлении
        this.category.name = this.toolInfo.replace(/^\s\s*/, '').replace(/\s\s*$/, '')

        this.axios.post(`api/category`, this.category).then(result => {
          if (result.status === 200) {
            this.category = result.data
            this.addCategory(result.data)
            this.showConfirmBtn = false
            this.showImageForm = true;
            this.toolInfo = ''
          } else {
            this.category = {}
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

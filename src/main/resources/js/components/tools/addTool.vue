<template xmlns="http://www.w3.org/1999/html">

  <v-row justify="center">
    <v-overlay
        scroll-strategy="block"
        class="align-center justify-center"
        v-model="overlayTool"
        align="end"
    >
      <v-btn icon="mdi-close" @click="overlayTool = false"></v-btn>
      <br>
      <br>
      <add-description-tool  :addTool="addTool" :toolSets="toolSets"  :editTool="editTool" :profile="profile" :role="role" :tool="tool"></add-description-tool>
    </v-overlay>
    <v-overlay
        scroll-strategy="block"
        class="align-center justify-center"
        v-model="overlay"
        align="end"
    >
      <v-btn icon="mdi-close" @click="overlay = false"></v-btn>
      <br>
      <br>
      <add-category  :addCategory="addCategory" v-bind:categories="categories"  :editCategory="editCategory" :profile="profile" :role="role" :category="category" :deleteCategory="deleteCategory" ></add-category>
    </v-overlay>
    <v-overlay
        scroll-strategy="block"
        class="align-center justify-center"
        v-model="overlayToolSet"
        align="end"
    >
      <v-btn icon="mdi-close" @click="overlayToolSet = false"></v-btn>
      <br>
      <br>
      <add-toolSet  :addToolSet="addToolSet" v-bind:toolSets="toolSets"   :editToolSet="editToolSet" :profile="profile" :role="role" :category="category" :deleteToolSet="deleteToolSet"></add-toolSet>
    </v-overlay>
    <v-btn
        class="mt-16"
        color="#EBB652"
        @click="overlayTool = !overlayTool"
    >
      Добавить инструмент
    </v-btn>
    &nbsp
    &nbsp
    &nbsp
    &nbsp
    &nbsp
    &nbsp
    <v-btn
        class="mt-16"
        color="#EBB652"
        @click="overlay = !overlay"
    >
      Добавить категорию
    </v-btn>
    &nbsp
    &nbsp
    &nbsp
    &nbsp
    &nbsp
    &nbsp
    <v-btn
        class="mt-16"
        color="#EBB652"
        @click="overlayToolSet = !overlayToolSet"
    >
      Добавить комплект
    </v-btn>
  </v-row>
  <br>
  <br>

</template>

<script>

import AddToolSet from "./addToolSet.vue";
import AddCategory from "./addCategory.vue";
import AddDescriptionTool from "./addDescriptionTool.vue";
export default {
  name: "addTool",
  components: {AddToolSet, AddCategory, AddDescriptionTool},
  data() {
    return {
      imageEditButton: false,
      facilityNameSelected: '',
      overlayToolSet: false,
      overlayTool: false,
      overlay: false,
      tool: '',
      category: ''
    }
  },
  mounted() {
    this.tool = {
      name: '',
      article: '',
      serial: '',
      price: '',
      purchaseDate: '',
      servicePeriod: '',
      inventoryPeriod: '',
      description: '',
    }

    this.category = {
      name: ''
    }

    if (this.tool)
      this.imageEditButton = this.tool.image
    this.facilityNameSelected = this.toolFacilityNameIfPresent(this.tool)
  },
  methods: {
    imageEditFunc: function () {
      this.imageEditButton = !this.imageEditButton
    },
    selectFacility: function (facilityNameSelected) {
      this.facilityNameSelected = facilityNameSelected
    },
    toolFacilityNameIfPresent: function (tool) {
      return (tool.facility ? tool.facility.name : '')
    }
  }
  ,
  props: ['profile', 'role', 'facilities', 'editTool', 'toolSets', 'addTool',  'editCategory', 'categories', 'addCategory', 'editToolSet', 'addToolSet', 'deleteCategory', 'deleteToolSet'],
}
</script>

<style scoped>

</style>

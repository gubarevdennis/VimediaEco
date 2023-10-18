<template xmlns="http://www.w3.org/1999/html">
  <v-row justify="center">
    <v-overlay

        scroll-strategy="reposition"
        class="align-center justify-center"

        v-model="overlay"
        align="end"
    >
      <v-btn icon="mdi-close" @click="overlay = false"></v-btn>
      <br>
      <br>
      <add-description-tool  :addTool="addTool" :toolSets="toolSets"  :editTool="editTool" :profile="profile" :role="role" :tool="tool"></add-description-tool>
    </v-overlay>
    <v-btn
        class="mt-16"
        color="#EBB652"
        @click="overlay = !overlay"
    >
      Добавить инструмент
    </v-btn>
  </v-row>
  <br>
  <br>
</template>

<script>

import AddDescriptionTool from "./addDescriptionTool.vue";
export default {
  name: "addTool",
  components: {AddDescriptionTool},
  data() {
    return {
      imageEditButton: false,
      facilityNameSelected: '',
      overlay: false,
      tool: ''
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
  props: ['profile', 'role', 'facilities', 'editTool', 'toolSets', 'addTool'],
}
</script>

<style scoped>

</style>

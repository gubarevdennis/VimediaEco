<template xmlns="http://www.w3.org/1999/html">
  <v-card align="center" color="black" rounded="lg"  class="ma-2" >
    <br>
    <v-row justify="center" align="center">
      <div v-for="tool in toolsForRow" style="width: 320px; height: 200px">
        <main-table-tools-col :deleteTool="deleteTool"
                              :users="users"
                              :userNames="userNames"
                              :role="role"
                              :profile="profile"
                              :profileId="profileId"
                              :toolSets="toolSets"
                              :editTool="editTool"
                              :row="row"
                              :tool="tool"
                              :toolFunc="toolFunc"
                              :overlayToGivingFunc="overlayToGivingFunc"
                              :overlayFunc="overlayFunc"
                              :facilityNames="facilityNames"
                              :facilities="facilities"
                              :updateFacilityBySorting="updateFacilityBySorting"
        ></main-table-tools-col>
      </div>
    </v-row>

    <br>
  </v-card>
</template>

<script>
import { isReactive, unref, toRaw } from 'vue'
import DecriptionTool from "./decriptionTool.vue";
import GivingTool from "./givingTool.vue";
import MainTableToolsCol from "./mainTableToolsCol.vue";

export default {
  components: {MainTableToolsCol, DecriptionTool, GivingTool},
  props: ['profile', 'role', 'tool', 'editTool',
    'toolSets', 'deleteTool', 'userNames', 'users', 'profileId',
    'row', 'toolsForRow', 'overlayToGivingFunc', 'overlayFunc', 'toolFunc', 'facilityNames', 'facilities' , 'updateFacilityBySorting',
  ],
  data() {
    return {
    }
  },
  mounted: function () {
    // console.log("this.toolsForRow")
    // console.log(this.toolsForRow)
  },
  methods: {
    load() {
      this.tools.push({name: "fsidf"})
      // console.log("Page down!")
      // console.log(this.countLoadingPages)
    },
  }
}
</script>

<style scoped>

</style>

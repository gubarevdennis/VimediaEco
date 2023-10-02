<template xmlns="http://www.w3.org/1999/html">
  <html xmlns:th="https://www.thymeleaf.org">
  <v-card v-bind:height="(overlay ? 1500 : '')" color="#7d7f7d" rounded="lg"  class="ma-2" >
    <v-overlay
        contained
        v-model="overlay"
        class="align-center justify-center"
        scroll-strategy="reposition"
        align="center"
    >

      <v-btn icon="mdi-close" @click="overlay = !overlay"></v-btn>
      <br>
      <br>
      <decription-tool :closeDescriptionToolByDeleteConfirm="closeDescriptionToolByDeleteConfirm"
                       :deleteTool="deleteTool"
                       :toolSets="toolSets"
                       :editTool="editTool"
                       :profile="profile"
                       :role="role"
                       :tool="tool"></decription-tool>

    </v-overlay>
    <v-row justify="center" align="center">
      <v-col row  wrap align-center height="200px">
        <br>
        <div align="center" >
          <v-sheet rounded="lg" style="background-color: #7d7f7d" width="350px" height="150px">
            <v-sheet rounded="lg" style="background-color: dimgray" width="150px" height="150px">
              <v-img
                  density="0"
                  v-if="(this.imageEditButton)"
                  v-bind:src=" 'upload/files/' + (tool ? tool.image : '')"
                  cover
                  class="text-white"
                  height="150px"
              />
            </v-sheet>
          </v-sheet>
        </div>
        <br>
      </v-col>

      <v-col align-self="center">
        <v-sheet style="background-color: #7d7f7d" width="350px" >

          <v-card-title class="text-h6" align="center" width="300px">
            {{ tool.name }}
          </v-card-title>

        </v-sheet>
      </v-col>
      <v-col >
        <v-sheet class="text-subtitle-1" align="center"  v-if="tool.article"  style="background-color: #7d7f7d" width="350px">
          <v-row align="center" justify="center">
            {{ tool.article }}
          </v-row>
        </v-sheet>
      </v-col>
      <v-col  >
        <v-sheet class="text-subtitle-1" v-if="tool.serial"  align="center" style="background-color: #7d7f7d" width="150px">
          {{ tool.serial }}
        </v-sheet>
      </v-col>
      <v-col >
        <v-sheet v-if="tool.vendor" align="center" style="background-color: #7d7f7d" width="200px">
          {{ tool.vendor }}
        </v-sheet>
      </v-col>
      <v-col >
        <v-sheet class="text-subtitle-1" align="center"  v-if="tool.quantity"  style="background-color: #7d7f7d" width="150px">
          <v-row align="center" justify="center">
            {{ tool.quantity }} шт.
          </v-row>
        </v-sheet>
      </v-col>
      <v-col row  wrap align-center width="350px">
        <v-sheet class="text-subtitle-1" v-if="tool.article" align="center"  style="background-color: #7d7f7d" width="350px">
          <v-row justify="center" align="center">
            <br>
            с такого-то числа
            <br>
            от такого-то
          </v-row>
          <v-row justify="center" align="center">
            <v-select

                label="Объект"
                variant="outlined"
                @update:modelValue="selectFacility"
                v-model="facilityNameSelected"
                :items="facilities"
                :item-value="facilityNameSelected"
            >
            </v-select>
          </v-row>
        </v-sheet>
      </v-col>
      <v-col row  wrap align-center width="350px">
        <v-sheet  style="background-color: #7d7f7d" width="350px">
          <v-row justify="center" align="center">
            <v-btn v-if="((this.role === 'Кладовщик') || (this.role === 'Директор'))" align="center" @click="overlay = !overlay">
              Редактировать
            </v-btn>
            &nbsp
            &nbsp
            &nbsp
            &nbsp
            <v-btn color="#EBB652" align="center">
              Передать
            </v-btn>
            <br>
            <br>
            <br>
          </v-row>
          <br>
        </v-sheet>
      </v-col>

    </v-row>
  </v-card>
  </html>
</template>

<script>
import DecriptionTool from "./decriptionTool.vue";
export default {
  components: {DecriptionTool},
  data() {
    return {
      imageEditButton: false,
      facilityNameSelected: '',
      overlay: false
    }
  },
  mounted() {
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
    },
    closeDescriptionToolByDeleteConfirm: function () {
      this.overlay = false;
    },
  }
  ,
  props: ['profile', 'role', 'tool', 'facilities', 'editTool', 'toolSets', 'deleteTool'],
}
</script>

<style scoped>

</style>

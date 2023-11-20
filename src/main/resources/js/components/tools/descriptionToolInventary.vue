<template>
  <html style=" overflow-x: scroll; overflow-y: hidden; ">
  <v-sheet width="600">
    <v-row justify="center" align="center">
      <v-col  v-for="event in inventoryEvents">
        <form  method="POST" enctype="multipart/form-data" action="api/upload/inventory">
          <input name="eventId" type="hidden" v-bind:value="(event ? event.id : '')" />
          <v-sheet width="100px" >
            <v-img
                @click="imageEditFunc"
                density="0"
                v-if="(this.imageEditButton)"
                v-bind:src=" 'upload/files/' + (event ? event.image : '')"
                class="text-white"
                height="100px"
            />
            <br>
            <div v-if="(!(this.imageEditButton))">
              <v-file-input
                  width="100px"
                  density="0"
                  color="black"
                  center-affix="black"
                  hide-details="true"
                  type="file"
                  name="file"
                  accept="image/*"
                  capture="camera"
                  prepend-icon="mdi-camera"
                  variant="solo-inverted"
              />

              <v-row style="vertical-align: center" >
                <v-col  align="end">
                  <v-btn  color="red"  text="Отмена" v-if="(!(this.imageEditButton) && (tool ? tool.invenTa : ''))" @click="imageEditFunc" />
                </v-col>
                <v-col  align="start">
                  <v-btn  text="Загрузить" type="submit"/>
                </v-col>
              </v-row>
              <br>
            </div>
          </v-sheet>
        </form>
      </v-col>
    </v-row>
  </v-sheet>
  </html>
</template>

<script>
import DescriptionToolRow from "./descriptionToolRow.vue";
import ConfirmDeleteTool from "./confirmDeleteTool.vue";
export default {
  name: "decriptionTool",
  components: {ConfirmDeleteTool, DescriptionToolRow},
  props: ['profile', 'role', 'tool', 'editTool', 'toolSets', 'deleteTool', 'closeDescriptionToolByDeleteConfirm',
    'inventoryEvents'],
  data() {
    return {
      imageEditButton: false,
      toolSetNameSelected: '',
      toolCategoryNameSelected: '',
      showConfirmBtn: false,
      toolSerialOnOverlay: '',
      toolSetNames: [],
      showConfirmDeleteTool: false,
      overlayDel: false,
      toolCategoryNames: ''
    }
  },
  mounted() {
    console.log("this.inventoryEvents")
    console.log(this.inventoryEvents)

  },
  methods: {
    imageEditFunc: function () {
      this.imageEditButton = !this.imageEditButton
    },

    edit: function () {
      // Присвоение всех параметров из формы
      var tool = this.tool

      // Находим нужный набор по имени и присваиваем инструмемнту, если нашли
      var toolSetFound = this.toolSets.find(t => t.name === this.toolSetNameSelected)

      if (toolSetFound)
        tool.toolSet = toolSetFound

      // Присваиваем категорию инструмента
      if (this.toolCategoryNameSelected)
        tool.category = this.toolCategoryNameSelected

      this.axios.put(`api/tool/${tool.id}`, tool).then(result => {
        if (result.status === 200) {
          this.editTool(result.data)
          this.showConfirmBtn=false
        } else {
          this.toolSetNameSelected = ''
          this.showConfirmBtn=false
        }
      })
    }
  }
}
</script>

<style scoped>

</style>

<template>
  <html style="max-height: 80vh; max-width: 90vh">
  <v-sheet
      rounded="lg"
      contained
      align="center"
  >
    <v-col>
      <v-row justify="center" align="center">
        <v-col>
          <form method="POST" enctype="multipart/form-data" action="/upload">
            <input name="toolId" type="hidden" v-bind:value="(tool ? tool.id : '')" />
            <v-sheet width="260px" >
              <v-img
                  density="0"
                  v-if="(this.imageEditButton)"
                  v-bind:src=" 'upload/files/' + (tool ? tool.image : '')"
                  class="text-white"
                  height="260px"
              />
            </v-sheet>
          </form>
          <v-sheet style="width: 250px;">
            <div style="font-weight: bold; ">
              {{tool ? tool.name : ''}}
            </div>
            <div>
              {{tool ? tool.article : ''}}
            </div>
          </v-sheet>
        </v-col >
    <v-col  style="font-size: 40px">
      <v-card-text align="start">
        <v-row align="start">
          <div style="font-weight: bold;color: #0B0B0B"> Наименование: &nbsp</div>
          {{ tool ? tool.name : '' }}
        </v-row>
      </v-card-text>
      <v-card-text >
        <v-row>
          <div style="font-weight: bold;color: #0B0B0B"> Артикул: &nbsp</div>
          {{ tool.article }}
        </v-row>
      </v-card-text>
      <v-card-text >
        <v-row>
          <div style="font-weight: bold;color: #0B0B0B"> Комплектность: &nbsp</div>
          {{ tool.toolSet ? tool.toolSet.name : 'Не входит в комплект'}}
        </v-row>
      </v-card-text>
      <v-card-text >
        <v-row>
          <div style="font-weight: bold;color: #0B0B0B"> Колличество: &nbsp</div>
          {{ tool.quantity }}
        </v-row>
      </v-card-text>
      <v-card-text >
        <v-row>
          <div style="font-weight: bold;color: #0B0B0B"> Инвентарный номер: &nbsp</div>
          {{ tool.id }}
        </v-row>
      </v-card-text>
      <v-card-text >
        <v-row>
          <div style="font-weight: bold;color: #0B0B0B"> Серийный номер: &nbsp</div>
          {{ tool.serial }}
        </v-row>
      </v-card-text>
      <v-card-text >
        <v-row>
          <div style="font-weight: bold;color: #0B0B0B"> Производитель: &nbsp</div>
          {{ tool.vendor }}
        </v-row>
      </v-card-text>
      <v-card-text>
        <v-row>
          <div style="font-weight: bold;color: #0B0B0B"> Стоимость: &nbsp</div>
          {{ tool.price }}
        </v-row>
      </v-card-text>
      <v-card-text>
        <v-row>
          <div style="font-weight: bold;color: #0B0B0B"> Поставщик: &nbsp</div>
          {{ tool.provider }}
        </v-row>
      </v-card-text>
      <v-card-text>
        <v-row>
          <div style="font-weight: bold;color: #0B0B0B"> Информация о закупке: &nbsp</div>
          {{ tool.paymentInfo }}
        </v-row>
      </v-card-text>
      <v-card-text>
        <v-row>
          <div style="font-weight: bold;color: #0B0B0B"> Состав: &nbsp</div>
          {{ tool.composition }}
        </v-row>
      </v-card-text>
      <v-card-text>
        <v-row>
          <div style="font-weight: bold;color: #0B0B0B"> Описание: &nbsp</div>
          {{ tool.description }}
        </v-row>
      </v-card-text>
      <v-card-text>
        <v-row>
          <div style="font-weight: bold;color: #0B0B0B"> Комментарий: &nbsp</div>
          {{ tool.text }}
        </v-row>
      </v-card-text>
    </v-col>
      </v-row>
    </v-col>
  </v-sheet>
  </html>
</template>

<script>
import DescriptionToolRow from "./descriptionToolRow.vue";
import ConfirmDeleteTool from "./confirmDeleteTool.vue";
export default {
  name: "decriptionTool",
  components: {ConfirmDeleteTool, DescriptionToolRow},
  props: ['profile', 'role', 'tool', 'editTool', 'toolSets', 'deleteTool', 'closeDescriptionToolByDeleteConfirm'],
  data() {
    return {
      imageEditButton: false,
      toolSetNameSelected: '',
      showConfirmBtn: false,
      toolSerialOnOverlay: '',
      toolSetNames: [],
      showConfirmDeleteTool: false,
      overlayDel: false
    }
  },
  mounted() {
    this.imageEditButton = this.tool ? this.tool.image : ''
    this.toolSetNameSelected = this.toolSetNameIfPresent(this.tool)
    this.toolSerialOnOverlay = this.tool ?  this.tool.serial : ''
    this.toolSets.forEach(t => this.toolSetNames.push(t.name))
    console.log(this.toolSetNameSelected)
  },
  methods: {
    imageEditFunc: function () {
      this.imageEditButton = !this.imageEditButton
    },
    selectToolSet: function (toolSetNameSelected) {
      this.toolSetNameSelected = toolSetNameSelected
      this.showConfirmBtn = true
    },
    toolSetNameIfPresent: function (tool) {
      return (tool ? tool.toolSet ? tool.toolSet.name : '' : '')
    },
    hideConfirmBtnFunc: function () {
      this.showConfirmBtn=false
      this.toolSetNameSelected = ''
    },
    showConfirmDeleteToolFunc: function (){
      this.closeDescriptionToolByDeleteConfirm()
      this.overlayDel=false;
    },
    edit: function () {
      var tool = this.tool

      var toolSetFound = this.toolSets.find(t => t.name === this.toolSetNameSelected)

      if (toolSetFound)
        tool.toolSet = toolSetFound

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

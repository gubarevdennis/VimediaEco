<template>
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
            <v-card color="grey" width="350px" >
              <v-img
                  @click="imageEditFunc"
                  density="0"
                  v-if="(this.imageEditButton)"
                  v-bind:src=" 'upload/files/' + (tool ? tool.image : '')"
                  cover
                  class="text-white"
                  height="350px"
              />
              <div v-if="(!(this.imageEditButton))">
                <br>
                <br>
                <v-file-input
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
                <br>
                <v-row style="vertical-align: center" >
                  <v-col align="end">
                    <v-btn  color="red"  text="Отмена" v-if="(!(this.imageEditButton) && (tool ? tool.image : ''))" @click="imageEditFunc" />
                  </v-col>
                  <v-col align="start">
                    <v-btn text="Загрузить" type="submit"/>
                  </v-col>
                </v-row>
                <br>
                <br>
              </div>
            </v-card>
          </form>
          <v-card-title>
            <div style="font-weight: bold;">
              {{tool ? tool.name : ''}}
            </div>
            <div>
              {{tool ? tool.article : ''}}
            </div>
          </v-card-title>
        </v-col >
        <v-col  style="font-size: 40px">
          <v-select
              label="Комплектность"
              variant="solo"
              @update:modelValue="selectToolSet"
              v-model="toolSetNameSelected"
              :items="toolSetNames"
              :item-value="toolSetNameSelected"
          >
          </v-select>
          <v-btn v-if="showConfirmBtn" color="green" size="small" @click="edit" > Применить </v-btn>
          <v-btn v-if="showConfirmBtn" color="red" size="small" @click="hideConfirmBtnFunc" > Отмена </v-btn>
          <v-card-text >
            <v-row>
              <div style="font-weight: bold;color: #0B0B0B"> Наименование: &nbsp</div>
              <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                    v-bind:rowInputText="tool ? tool.name : ''" v-bind:rowInputType="'name'">

              </description-tool-row>
            </v-row>
          </v-card-text>
          <v-card-text >
            <v-row>
              <div style="font-weight: bold;color: #0B0B0B"> Артикул: &nbsp</div>
              <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                    v-bind:rowInputText="tool ? tool.article : ''" v-bind:rowInputType="'article'">

              </description-tool-row>
            </v-row>
          </v-card-text>
          <v-card-text >
            <v-row>
              <div style="font-weight: bold;color: #0B0B0B"> Колличество: &nbsp</div>
              <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                    v-bind:rowInputText="tool ? tool.quantity : ''" v-bind:rowInputType="'quantity'">

              </description-tool-row>
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
              <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                    v-bind:rowInputText="tool ? tool.serial : ''" v-bind:rowInputType="'serial'">

              </description-tool-row>
            </v-row>
          </v-card-text>
          <v-card-text >
            <v-row>
              <div style="font-weight: bold;color: #0B0B0B"> Производитель: &nbsp</div>
              <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                    v-bind:rowInputText="tool ? tool.vendor : ''" v-bind:rowInputType="'vendor'">

              </description-tool-row>
            </v-row>
          </v-card-text>
          <v-card-text>
            <v-row>
              <div style="font-weight: bold;color: #0B0B0B"> Стоимость: &nbsp</div>
              <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                    v-bind:rowInputText="tool ? tool.price : ''" v-bind:rowInputType="'price'">

              </description-tool-row>
            </v-row>
          </v-card-text>
          <v-card-text>
            <v-row>
              <div style="font-weight: bold;color: #0B0B0B"> Поставщик: &nbsp</div>
              <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                    v-bind:rowInputText="tool ? tool.provider : ''" v-bind:rowInputType="'provider'">

              </description-tool-row>
            </v-row>
          </v-card-text>
          <v-card-text>
            <v-row>
              <div style="font-weight: bold;color: #0B0B0B"> Информация о закупке: &nbsp</div>
              <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                    v-bind:rowInputText="tool ? tool.paymentInfo : ''" v-bind:rowInputType="'paymentInfo'">

              </description-tool-row>
            </v-row>
          </v-card-text>
          <v-card-text>
            <v-row>
              <div style="font-weight: bold;color: #0B0B0B"> Дата покупки: &nbsp</div>
              <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                    v-bind:rowInputText="tool ? tool.purchaseDate : ''" v-bind:rowInputType="'purchaseDate'">

              </description-tool-row>
            </v-row>
          </v-card-text>
          <v-card-text>
            <v-row>
              <div style="font-weight: bold;color: #0B0B0B"> Период обслуживания: &nbsp</div>
              <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                    v-bind:rowInputText="tool ? tool.servicePeriod : ''" v-bind:rowInputType="'servicePeriod'">
              </description-tool-row>
            </v-row>
          </v-card-text>
          <v-card-text>
            <v-row>
              <div style="font-weight: bold;color: #0B0B0B"> Период инвентаризации: &nbsp</div>
              <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                    v-bind:rowInputText="tool ? tool.inventoryPeriod : ''" v-bind:rowInputType="'inventoryPeriod'">

              </description-tool-row>
            </v-row>
          </v-card-text>
          <v-card-text>
            <v-row>
              <div style="font-weight: bold;color: #0B0B0B"> Состав: &nbsp</div>
              <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                    v-bind:rowInputText="tool ? tool.composition : ''" v-bind:rowInputType="'composition'" ></description-tool-row>
            </v-row>
          </v-card-text>
          <v-card-text>
            <v-row>
              <div style="font-weight: bold;color: #0B0B0B"> Описание: &nbsp</div>
              <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                    v-bind:rowInputText="tool ? tool.description : ''" v-bind:rowInputType="'description'" ></description-tool-row>
            </v-row>
          </v-card-text>
          <v-card-text>
            <v-row>
              <div style="font-weight: bold;color: #0B0B0B"> Комментарий: &nbsp</div>
              <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                    v-bind:rowInputText="tool ? tool.text : ''" v-bind:rowInputType="'text'" ></description-tool-row>
            </v-row>
          </v-card-text>
          <v-overlay
              scroll-strategy="reposition"
              class="align-center justify-center"
              v-model="overlayDel"
              align="end"
          >
            <v-btn icon="mdi-close" @click="overlayDel = false"></v-btn>
            <confirm-delete-tool :deleteTool="deleteTool" :showConfirmDeleteToolFunc="showConfirmDeleteToolFunc" :tool="tool" ></confirm-delete-tool>
          </v-overlay>
          <v-btn v-if="(this.role === 'Кладовщик' || this.role === 'Директор')" color="red" @click="overlayDel = !overlayDel" :showConfirmDeleteTool="showConfirmDeleteTool"
          > Удалить инструмент </v-btn>
        </v-col>
      </v-row>
    </v-col>
  </v-sheet>
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

<template>
  <html style="max-height: 100vh; max-width: 90vh">
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
                  @click="imageEditFunc"
                  density="0"
                  v-if="(this.imageEditButton)"
                  v-bind:src=" 'upload/files/' + (tool ? tool.image : '')"
                  class="text-white"
                  height="260px"
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
            </v-sheet>
          </form>
          <v-sheet style="width: 300px; ">
            <div style="font-weight: bold;">
              {{tool ? tool.name : ''}}
            </div>
            <div>
              {{tool ? tool.article : ''}}
            </div>
          </v-sheet>
          <br>
          <v-card style="width: 300px; ">
            <v-text style="font-weight: normal; color: #D88E32; font-size: 15px">
              Обслуживания
            </v-text>
            <br>
            <description-tool-inventary
            >
            </description-tool-inventary>
          </v-card>
          <br>
          <v-card style="width: 300px; ">
            <v-text style="font-weight: normal; color: #D88E32; font-size: 15px">
              Ремонты
            </v-text>
            <br>
            <description-tool-inventary>
            </description-tool-inventary>
          </v-card>
          <br>
          <v-card style="width: 300px; ">
            <v-text style="font-weight: normal; color: #D88E32; font-size: 15px">
              Инвентаризации
            </v-text>
            <br>
            <description-tool-inventary
                :tool="tool"
                :inventoryEvents="inventoryEvents"
            >
            </description-tool-inventary>
          </v-card>
          <br>
        </v-col >
        <v-col  style="font-size: 40px" >

          <v-autocomplete
              density="compact"
              label="Комплектность"
              variant="solo"
              @update:modelValue="selectToolSet"
              v-model="toolSetNameSelected"
              :items="toolSetNames"
              :item-value="toolSetNameSelected"
          >
          </v-autocomplete>
          <v-select
              density="compact"
              label="Категория"
              variant="solo"
              @update:modelValue="selectToolCategory"
              v-model="toolCategoryNameSelected"
              :items="toolCategoryNames"
              :item-value="toolCategoryNameSelected"
          >
          </v-select>
          <v-row justify="center" align-content="center" style="margin-bottom: 5px">
            <v-btn v-if="showConfirmBtn" color="green"  @click="edit" > Применить </v-btn>

            <v-btn v-if="showConfirmBtn" color="red"  @click="hideConfirmBtnFunc" > Отмена </v-btn>
          </v-row>
          <v-card-text>
            <v-row >
              <div style="font-weight: bold;color: #0B0B0B"> Наименование: &nbsp</div>
              <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                    v-bind:rowInputText="tool ? tool.name : ''" v-bind:rowInputType="'name'">
              </description-tool-row>
            </v-row>
          </v-card-text>
          <v-card-text >
            <v-row>
              <div style="font-weight: bold;color: #0B0B0B"> Модель: &nbsp</div>
              <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                    v-bind:rowInputText="tool ? tool.model : ''" v-bind:rowInputType="'model'">
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
              v-model="overlayCopy"
              align="end"
          >
            <v-btn icon="mdi-close" @click="overlayCopy = false"></v-btn>
            <confirm-copy-tool :copyTool="copyTool" :showConfirmCopyToolFunc="showConfirmCopyToolFunc" :tool="tool" ></confirm-copy-tool>
          </v-overlay>
          <v-overlay
              scroll-strategy="reposition"
              class="align-center justify-center"
              v-model="overlayDel"
              align="end"
          >
            <v-btn icon="mdi-close" @click="overlayDel = false"></v-btn>
            <confirm-delete-tool :deleteTool="deleteTool" :showConfirmDeleteToolFunc="showConfirmDeleteToolFunc" :tool="tool" ></confirm-delete-tool>
          </v-overlay>
          <v-btn v-if="(this.role === 'Кладовщик') || (this.role === 'Директор')" color="yellow" @click="overlayCopy = !overlayCopy" :showConfirmCopyTool="showConfirmCopyTool"
          > Копировать </v-btn>
          <v-btn v-if="(this.role === 'Директор')" color="red" @click="overlayDel = !overlayDel" :showConfirmDeleteTool="showConfirmDeleteTool"
          > Удалить инструмент </v-btn>
        </v-col>
      </v-row>
    </v-col>
  </v-sheet>
  </html>
</template>

<script>
import DescriptionToolRow from "./descriptionToolRow.vue";
import DescriptionToolInventary from "./descriptionToolInventary.vue";
import ConfirmDeleteTool from "./confirmDeleteTool.vue";
import ConfirmCopyTool from "./confirmCopyTool.vue";
export default {
  name: "decriptionTool",
  components: {ConfirmDeleteTool, DescriptionToolRow, DescriptionToolInventary, ConfirmCopyTool},
  props: ['profile', 'role', 'tool', 'editTool', 'toolSets', 'categories', 'deleteTool', 'copyTool', 'closeDescriptionToolByDeleteConfirm', 'closeDescriptionToolByCopyConfirm'],
  data() {
    return {
      imageEditButton: false,
      toolSetNameSelected: '',
      toolCategoryNameSelected: '',
      showConfirmBtn: false,
      toolSerialOnOverlay: '',
      toolSetNames: [],
      showConfirmDeleteTool: false,
      showConfirmCopyTool: false,
      overlayDel: false,
      overlayCopy: false,
      toolCategoryNames: [],
      inventoryEvents: []
    }
  },
  mounted() {
    this.imageEditButton = this.tool ? this.tool.image : ''
    this.toolSetNameSelected = this.tool ? this.tool.toolSet ? this.tool.toolSet.name : '' : ''
    this.toolSerialOnOverlay = this.tool ?  this.tool.serial : ''
    this.toolCategoryNameSelected = this.tool ?  this.tool.category ? this.tool.category.name : '' : ''
    this.toolSets.forEach(t => this.toolSetNames.push(t.name)) // добавляем имена инсрументов для выбора
    this.categories.forEach(c => this.toolCategoryNames.push(c.name))
        // ['Электроинструмент', 'Абразивный инструмент', 'Измерительный инструмент',
        //   'Слесарно-монтажный инструмент', 'Без категории']
    // console.log(this.toolSetNameSelected)

    // Подгружаем инвентаризации данного инструмента
    if (this.tool)
      this.axios.get( "api/inventory/tool/" + this.tool.id).then(events => {
            events.data.forEach(e => this.inventoryEvents.push(e))
          }
      )
  },
  methods: {
    imageEditFunc: function () {
      this.imageEditButton = !this.imageEditButton
    },
    selectToolSet: function (toolSetNameSelected) {
      this.toolSetNameSelected = toolSetNameSelected
      this.showConfirmBtn = true
    },
    selectToolCategory: function (toolCategoryNameSelected) {
      this.toolCategoryNameSelected = toolCategoryNameSelected
      this.showConfirmBtn = true
    },
    hideConfirmBtnFunc: function () {
      this.showConfirmBtn=false
      this.toolSetNameSelected = ''
    },
    showConfirmDeleteToolFunc: function (){
      this.closeDescriptionToolByDeleteConfirm()
      this.overlayDel=false;
    },
    showConfirmCopyToolFunc: function (){
      this.closeDescriptionToolByCopyConfirm()
      this.overlayCopy=false;
    },
    edit: function () {
      // Присвоение всех параметров из формы
      var tool = this.tool

      // Находим нужный набор по имени и присваиваем инструмемнту, если нашли
      var toolSetFound = this.toolSets.find(t => t.name === this.toolSetNameSelected)

      if (toolSetFound)
        tool.toolSet = toolSetFound

      // Находим нужную категорию по имени и присваиваем инструмемнту, если нашли
      var categoryFound = this.categories.find(t => t.name === this.toolCategoryNameSelected)

      if (categoryFound)
        tool.category = categoryFound

      // Запрос на редактирование инструмента
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

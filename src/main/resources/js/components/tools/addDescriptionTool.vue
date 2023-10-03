<template>
  <v-sheet
      rounded="lg"
      contained

      align="center"
  >
    <v-row justify="center" align="center">
      <v-col v-if="showImageForm">
        <v-card-title>
          <div style="font-weight: bold;">
            {{tool ? tool.name : ''}}
          </div>
          <div>
            {{tool ? tool.article : ''}}
          </div>
        </v-card-title>
        <form  method="POST" enctype="multipart/form-data" action="/upload">
          <input name="toolId" type="hidden" v-bind:value="(tool ? tool.id : '')" />
          <v-card color="grey" width="350px" >
            <v-img
                @click="imageEditFunc"
                density="0"
                v-if="(this.imageEditButton)"
                v-bind:src=" 'upload/files/' + (tool ? tool.image : '')"
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
        <v-card-text >
          <v-row>
            <div style="font-weight: bold;color: #0B0B0B"> Наименование: &nbsp</div>
            <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                  v-bind:rowInputText="tool ? tool.name : ''" v-bind:rowInputType="'name'"
                                  :showConfirmBtnFuncParrent="showConfirmBtnFuncParrent">

            </description-tool-row>
          </v-row>
        </v-card-text>
        <v-card-text >
          <v-row>
            <div style="font-weight: bold;color: #0B0B0B"> Артикул: &nbsp</div>
            <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                  v-bind:rowInputText="tool ? tool.article : ''" v-bind:rowInputType="'article'"
                                  :showConfirmBtnFuncParrent="showConfirmBtnFuncParrent">

            </description-tool-row>
          </v-row>
        </v-card-text>
        <v-card-text >
          <v-row>
            <div style="font-weight: bold;color: #0B0B0B"> Колличество: &nbsp</div>
            <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                  v-bind:rowInputText="tool ? tool.quantity : ''" v-bind:rowInputType="'quantity'"
                                  :showConfirmBtnFuncParrent="showConfirmBtnFuncParrent">

            </description-tool-row>
          </v-row>
        </v-card-text>
        <v-card-text >
          <v-row>
            <div style="font-weight: bold;color: #0B0B0B"> Серийный номер: &nbsp</div>
            <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                  v-bind:rowInputText="tool ? tool.serial : ''" v-bind:rowInputType="'serial'"
                                  :showConfirmBtnFuncParrent="showConfirmBtnFuncParrent">

            </description-tool-row>
          </v-row>
        </v-card-text>
        <v-card-text >
          <v-row>
            <div style="font-weight: bold;color: #0B0B0B"> Производитель: &nbsp</div>
            <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                  v-bind:rowInputText="tool ? tool.vendor : ''" v-bind:rowInputType="'vendor'"
                                  :showConfirmBtnFuncParrent="showConfirmBtnFuncParrent">

            </description-tool-row>
          </v-row>
        </v-card-text>
        <v-card-text>
          <v-row>
            <div style="font-weight: bold;color: #0B0B0B"> Стоимость: &nbsp</div>
            <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                  v-bind:rowInputText="tool ? tool.price : ''" v-bind:rowInputType="'price'"
                                  :showConfirmBtnFuncParrent="showConfirmBtnFuncParrent">

            </description-tool-row>
          </v-row>
        </v-card-text>
        <v-card-text>
          <v-row>
            <div style="font-weight: bold;color: #0B0B0B"> Поставщик: &nbsp</div>
            <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                  v-bind:rowInputText="tool ? tool.provider : ''" v-bind:rowInputType="'provider'"
                                  :showConfirmBtnFuncParrent="showConfirmBtnFuncParrent">

            </description-tool-row>
          </v-row>
        </v-card-text>
        <v-card-text>
          <v-row>
            <div style="font-weight: bold;color: #0B0B0B"> Информация о закупке: &nbsp</div>
            <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                  v-bind:rowInputText="tool ? tool.paymentInfo : ''" v-bind:rowInputType="'paymentInfo'"
                                  :showConfirmBtnFuncParrent="showConfirmBtnFuncParrent">

            </description-tool-row>
          </v-row>
        </v-card-text>
        <v-card-text>
          <v-row>
            <div style="font-weight: bold;color: #0B0B0B"> Дата покупки: &nbsp</div>
            <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                  v-bind:rowInputText="tool ? tool.purchaseDate : ''" v-bind:rowInputType="'purchaseDate'"
                                  :showConfirmBtnFuncParrent="showConfirmBtnFuncParrent">

            </description-tool-row>
          </v-row>
        </v-card-text>
        <v-card-text>
          <v-row>
            <div style="font-weight: bold;color: #0B0B0B"> Период обслуживания: &nbsp</div>
            <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                  v-bind:rowInputText="tool ? tool.servicePeriod : ''" v-bind:rowInputType="'servicePeriod'"
                                  :showConfirmBtnFuncParrent="showConfirmBtnFuncParrent">
            </description-tool-row>
          </v-row>
        </v-card-text>
        <v-card-text>
          <v-row>
            <div style="font-weight: bold;color: #0B0B0B"> Период инвентаризации: &nbsp</div>
            <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                  v-bind:rowInputText="tool ? tool.inventoryPeriod : ''" v-bind:rowInputType="'inventoryPeriod'"
                                  :showConfirmBtnFuncParrent="showConfirmBtnFuncParrent">

            </description-tool-row>
          </v-row>
        </v-card-text>
        <v-card-text>
          <v-row>
            <div style="font-weight: bold;color: #0B0B0B"> Состав: &nbsp</div>
            <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                  v-bind:rowInputText="tool ? tool.composition : ''" v-bind:rowInputType="'composition'"
                                  :showConfirmBtnFuncParrent="showConfirmBtnFuncParrent"></description-tool-row>
          </v-row>
        </v-card-text>
        <v-card-text>
          <v-row>
            <div style="font-weight: bold;color: #0B0B0B"> Описание: &nbsp</div>
            <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                  v-bind:rowInputText="tool ? tool.description : ''" v-bind:rowInputType="'description'"
                                  :showConfirmBtnFuncParrent="showConfirmBtnFuncParrent"></description-tool-row>
          </v-row>
        </v-card-text>
        <v-card-text>
          <v-row>
            <div style="font-weight: bold;color: #0B0B0B"> Комментарий: &nbsp</div>
            <description-tool-row :editTool="editTool"  :profile="profile" :role="role" :tool="tool"
                                  v-bind:rowInputText="tool ? tool.text : ''" v-bind:rowInputType="'text'"
                                  :showConfirmBtnFuncParrent="showConfirmBtnFuncParrent"></description-tool-row>
          </v-row>
        </v-card-text>
        <v-btn v-if="showConfirmBtn"  color="#EBB652"  @click="add" > Добавить </v-btn>
        <v-btn v-if="showConfirmBtn"   @click="hideConfirmBtnFunc" > Отмена </v-btn>
      </v-col>
    </v-row>
  </v-sheet>
</template>

<script>
import DescriptionToolRow from "./addDescriptionToolRow.vue";
export default {
  components: {DescriptionToolRow},
  props: ['profile', 'role', 'editTool', 'toolSets', 'addTool'],
  data() {
    return {
      imageEditButton: false,
      toolSetNameSelected: '',
      showConfirmBtn: false,
      toolSerialOnOverlay: '',
      toolSetNames: [],
      rowInputType: '',
      tool: {},
      showImageForm: false
    }
  },
  mounted() {
    this.imageEditButton = this.tool ? this.tool.image : ''
    this.toolSetNameSelected = this.toolSetNameIfPresent(this.tool)
    this.toolSerialOnOverlay = this.tool ?  this.tool.serial : ''
    this.toolSets.forEach(t => this.toolSetNames.push(t.name))
  },
  methods: {
    imageEditFunc: function () {
      this.imageEditButton = !this.imageEditButton
    },
    selectToolSet: function (toolSetNameSelected) {
      this.toolSetNameSelected = toolSetNameSelected
      var toolSetFound = this.toolSets.find(t => t.name === this.toolSetNameSelected)

      if (toolSetFound)
        this.tool.toolSet = toolSetFound
      this.showConfirmBtn = true
    },
    toolSetNameIfPresent: function (tool) {
      return (tool ? (tool.toolSet ? tool.toolSet.name : '') : '')
    },
    hideConfirmBtnFunc: function () {
      this.showConfirmBtn = false
      this.toolSetNameSelected = ''
    },
    edit: function () {

    },
    showConfirmBtnFuncParrent: function (rowInputType, toolInfo) {
      switch (rowInputType) {
        case 'quantity' :
          this.tool.quantity = toolInfo
          break;
        case 'article' :
          this.tool.article = toolInfo
          break;
        case 'name' :
          this.tool.name = toolInfo
          break;
        case 'id' :
          this.tool.id = toolInfo
          break;
        case 'vendor' :
          this.tool.vendor = toolInfo
          break;
        case 'serial' :
          this.tool.serial = toolInfo
          break;
        case 'price' :
          this.tool.price = toolInfo
          break;
        case 'provider' :
          this.tool.provider = toolInfo
          break;
        case 'paymentInfo' :
          this.tool.paymentInfo = toolInfo
          break;
        case 'purchaseDate' :
          this.tool.purchaseDate = toolInfo
          break;
        case 'servicePeriod' :
          this.tool.servicePeriod = toolInfo
          break;
        case 'inventoryPeriod' :
          this.tool.inventoryPeriod = toolInfo
          break;
        case 'composition' :
          this.tool.composition = toolInfo
          break;
        case 'description' :
          this.tool.description = toolInfo
          break;
        case 'text' :
          this.tool.text = toolInfo
          break;
      }

      console.log(this.tool)

      this.showConfirmBtn = true
    },
    add: function () {
      if (this.tool)
      this.axios.post(`api/tool`, this.tool).then(result => {
        if (result.status === 200) {
          this.addTool(result.data)
          this.showConfirmBtn=false
          this.showImageForm = true;
          this.tool = result.data
        } else {
          this.tool = {}
          this.showConfirmBtn=false
        }
      })
    }
  }
}
</script>

<style scoped>

</style>

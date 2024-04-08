<template>
  <div style="color: #0B0B0B">
    <input style="width: 220px" type="text" @change="showConfirmBtnFunc" v-model="toolInfo" />
    <div>

    </div>
  </div>
</template>

<script>
export default {
  props: ['profile', 'role', 'tool', 'rowInputText' , 'editTool', 'rowInputType' , 'showConfirmBtnFuncParrent'],
  data() {
    return {
      imageEditButton: false,
      facilityNameSelected: '',
      showConfirmBtn: false,
      toolInfo: '',
      toolEdit: ''
    }
  },
  mounted() {
    this.toolInfo = this.rowInputText
    console.log(this.rowInputText)
  },
  methods: {
    showConfirmBtnFunc: function () {
      this.showConfirmBtnFuncParrent(this.rowInputType, this.toolInfo)
    },
    hideConfirmBtnFunc: function () {
      this.showConfirmBtn=false
      this.toolInfo = this.rowInputText
    },
    edit: function () {
      //   var tool = this.tool
      //
      //   switch (this.rowInputType) {
      //     case 'quantity' :
      //       tool.quantity = this.toolInfo
      //       break;
      //     case 'article' :
      //       tool.article = this.toolInfo
      //       break;
      //     case 'name' :
      //       tool.name = this.toolInfo
      //       break;
      //     case 'id' :
      //       tool.id = this.toolInfo
      //       break;
      //     case 'vendor' :
      //       tool.vendor = this.toolInfo
      //       break;
      //     case 'serial' :
      //       tool.serial = this.toolInfo
      //       break;
      //     case 'price' :
      //       tool.price = this.toolInfo
      //       break;
      //     case 'provider' :
      //       tool.provider = this.toolInfo
      //       break;
      //     case 'paymentInfo' :
      //       tool.paymentInfo = this.toolInfo
      //       break;
      //     case 'purchaseDate' :
      //       tool.purchaseDate = this.toolInfo
      //       break;
      //     case 'servicePeriod' :
      //       tool.servicePeriod = this.toolInfo
      //       break;
      //     case 'inventoryPeriod' :
      //       tool.inventoryPeriod = this.toolInfo
      //       break;
      //     case 'composition' :
      //       tool.composition = this.toolInfo
      //       break;
      //     case 'description' :
      //       tool.description = this.toolInfo
      //       break;
      //     case 'text' :
      //       tool.text = this.toolInfo
      //       break;
      //   }
      //
      //   this.axios.put(`api/tool/${tool.id}`, tool).then(result => {
      //     if (result.status === 200) {
      //       this.editTool(tool)
      //       this.showConfirmBtn=false
      //     } else {
      //       this.toolInfo = this.rowInputText
      //       this.showConfirmBtn=false
      //     }
      //   })
    }
  },
}
</script>

<style scoped>

</style>

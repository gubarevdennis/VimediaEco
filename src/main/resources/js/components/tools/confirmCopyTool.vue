<template>
  <v-col>
    <v-card width="300px" align="center">
      <div align="center" style="font-weight: bold">
        Вы уверены, что хотите скопировать {{tool.name}} {{tool.article}} ?
      </div>

      Колличество:
      <input style="width: 50px" type="text" v-model="quantityToCopy" />
      <div align="center">
        <v-btn color="green"  @click="confirmCopy" > Копировать </v-btn>
        &nbsp
        &nbsp
        &nbsp
        &nbsp
        <v-btn  @click="cancel" > Отмена </v-btn>
      </div>
    </v-card>
  </v-col>
</template>

<script>
export default {
  name: "confirmCopyTool",
  props: ['tool', 'showConfirmCopyToolFunc', 'copyTool'],
  data() {
    return {
      showInfoSuccess: false,
      showInfoBad: false,
      quantityToCopy: 1
    }
  },
  mounted() {
    this.showInfoSuccess= false
    this.showInfoBad= false
  },
  methods: {
    confirmCopy: function () {
      // Ограничение колличетсва снизу
      if (this.quantityToCopy < 1) { this.quantityToCopy = 1; }

      // Ограничение колличетсва сверху
      if (this.quantityToCopy > 20) { this.quantityToCopy = 20; }

      for (let i = 0; i < this.quantityToCopy; i++) {

        if (this.tool) {
          // Обнуляем id и сериал
          this.tool.id = null;
          this.tool.serial = null;

          this.axios.post(`api/tool`, this.tool).then(result => {
            if (result.status === 200) {
              this.showInfoSuccess = true
              this.copyTool(result.data)
//              this.tool = result.data
            } else {
              this.showInfoBad = true
            }
          })
        }
      }
      this.showConfirmCopyToolFunc() // зыкрыть попап подтверждения
    },
    cancel: function () {
      this.showConfirmCopyToolFunc() // зыкрыть попап подтверждения
    }
  }
}
</script>

<style scoped>

</style>

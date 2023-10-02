<template>
  <v-col>
    <v-card width="300px">
      <div align="center">
        Вы уверены, что хотите удалить {{tool.name}} {{tool.article}} ?
      </div>
      <div align="center">
      <v-btn color="red"  @click="confirmDelete" > Удалить </v-btn>
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
  name: "confirmDeleteTool",
  props: ['tool', 'showConfirmDeleteToolFunc', 'deleteTool'],
  data() {
    return {
      showInfoSuccess: false,
      showInfoBad: false,
    }
  },
  mounted() {
    this.showInfoSuccess= false
    this.showInfoBad= false
  },
  methods: {
    confirmDelete: function () {
      this.axios.delete(`api/tool/${this.tool.id}`).then(result => {
        if (result.status === 200) {
          this.showInfoSuccess = true
          this.deleteTool(this.tool)
        } else {
          this.showInfoBad= true
        }
      })
      this.showConfirmDeleteToolFunc() // зыкрыть попап подтверждения
    },
    cancel: function () {
      this.showConfirmDeleteToolFunc() // зыкрыть попап подтверждения
    }
  }
}
</script>

<style scoped>

</style>

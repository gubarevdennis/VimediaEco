<template>
  <v-card
      class="ma-2"
      label
      rounded="lg"
      v-bind:color="setColorByWork(report.typeOfWork)"
  >
    {{report.user.name}}
  </v-card>
</template>

<script>

export default {
  name: "userByHour",
  props: ['report'],
  color: '',

  methods: {
    setColorByWork: function (work) {
      switch (work) {
        case 'Монтаж' :
          return 'red'
          break;
        case 'Проект' :
          return 'green'
          break;
        case 'Менеджмент' :
          return 'pink'
          break;
        case 'Сборка' :
          return 'aqua'
          break;
        case 'ПНР' :
          return 'teal'
          break;
        case 'Сервис' :
          return 'yellow'
          break;
        default:
          return 'gray'
      }
    }
  }
}
</script>

<style scoped>
.ma-2{
  width: 180px;
  border-radius: 1px;

}
</style>

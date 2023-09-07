<template>

  <v-card align="center"
          class="ma-2"
          label
  >
    <v-card-text style="font-size: 26px">
      {{day.getDate()}}
    </v-card-text>
  </v-card>

</template>

<script>
export default {
  props: ['day'],
}
</script>

<style scoped>
.ma-2{
  width: 180px;

  background: #D9D9D9;
}
</style>

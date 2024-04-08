<template>

  <v-card align="center"
          class="ma-2"
          label
  >
    <div style="font-size: 18px; vertical-align: center">
      {{day.getDate()}}
    </div>
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

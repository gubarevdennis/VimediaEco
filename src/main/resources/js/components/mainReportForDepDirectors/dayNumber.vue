<template>

  <v-card align="center"
          class="wewewe"

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
.wewewe{
  width: 180px;
  height: 30px;
  background: #D9D9D9;
}
</style>

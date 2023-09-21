<template>
  <v-col align-content="center" justify="center" style="height: 25px">
    <v-sheet

        align="start"
        rounded="lg"
        style="background-color: dimgray"
        width="180px"
        height="22px"
    >
      <v-sheet
          align="center"
          label
          rounded="lg"
          v-bind:color="setColorByWork(report.typeOfWork)"
          v-bind:width="report.hoursOfWorking * 22.5"
      >
        <v-row align="center" justify="center"
               style="background-color: rgba(0,0,0,0); width: 200px;  font-size: 15px; vertical-align: center">
          {{report.user.name}}
          <v-overlay
              activator="parent"
              location-strategy="connected"
              scroll-strategy="close"
          >
            <v-card
                rounded="lg"
                width="200px"
                v-bind:color="setColorByWork(report.typeOfWork)"
            >
              <v-card-text >
                <div style="font-weight: bold;color: #0B0B0B">
                  {{report.facility.name}}
                </div>
                <div style="font-weight: bold;color: #0B0B0B">
                  {{report.subFacility ? report.subFacility.name : ''}}
                </div>
                <div style="color: #0B0B0B">
                  {{ report.hoursOfWorking }} ч
                </div>
                <div v-if="report.text !== 'Отчет сформирован без комментариев'">
                  <br>
                  {{report.text}}
                </div>
              </v-card-text>
            </v-card>
          </v-overlay>
        </v-row>
      </v-sheet>
    </v-sheet>
  </v-col>
</template>

<script>

export default {
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

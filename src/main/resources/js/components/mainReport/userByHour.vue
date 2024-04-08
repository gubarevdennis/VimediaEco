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
          {{report.user ? report.user.name : ''}}
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
                <div style="color: #0B0B0B;">
                  {{ report.job ? report.job.name : ''}}
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
      if (this.report.job)
        return this.report.job.color
      // switch (work) {
      //   case 'Черновой монтаж' :
      //     return '#A0522D'
      //     break;
      //   case 'Чистовой монтаж' :
      //     return '#FF0000'
      //     break;
      //   case 'Шефмонтаж' :
      //     return '#F08080'
      //     break;
      //   case 'Концептуальное проектирование' :
      //     return '#9ACD32'
      //     break;
      //   case 'Рабочее проектирование' :
      //     return '#00FF00'
      //     break;
      //   case 'Сборка щитов' :
      //     return '#BC8F8F'
      //     break;
      //   case 'Расключение шкафов' :
      //     return '#008B8B'
      //     break;
      //   case 'ПНР' :
      //     return '#FF4500'
      //     break;
      //   case 'Сервис' :
      //     return '#FFA500'
      //     break;
      //   case 'Авторский надзор' :
      //     return '#FF1493'
      //     break;
      //   case 'Другие работы' :
      //     return '#BDB76B'
      //     break;
      //   case 'Отпуск оплачиваемый' :
      //     return '#7FFFD4'
      //     break;
      //   case 'Отпуск по семейным обстоятельствам' :
      //     return '#7B68EE'
      //     break;
      //   case 'Отпуск без сохранения ЗП' :
      //     return '#00FFFF'
      //   case 'Больничный' :
      //     return '#800080'
      //     break;
      //   default:
      //     return '#FFFFFF'
      // }
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

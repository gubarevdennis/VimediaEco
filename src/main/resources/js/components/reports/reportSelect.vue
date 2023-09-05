<template>
  <v-container>

    <v-row align="stretch" justify="center">

      <v-col         cols="10"
                     sm="3">
        <v-select
            variant="outlined"
            @update:modelValue="clicked1"
            label="Объект"
            :items="facilityNames"
            :item-value="facilityName"

        ></v-select>

      </v-col>

      <v-col   v-if="subAttrVisible"      cols="10"
               sm="2">
        <v-select
            variant="outlined"
            @update:modelValue="clickedSubFac"
            label="Подобъект"
            :items="filteredSubFacilityNames"
            :item-value="subFacilityName"

        ></v-select>
      </v-col>

      <v-col         cols="10"
                     sm="3">
        <v-select
            variant="outlined"
            @update:modelValue="clicked2"
            label="Длительность в часах"
            :items="workingTimes"
            :item-value="workingTime"

        ></v-select>
      </v-col>

      <v-col         cols="10"
                     sm="3">
        <v-select justify="center"
                  variant="outlined"
                  @update:modelValue="clicked3"
                  label="Вид работ"
                  :items="workingTypes"
                  :item-value="workingType"

        ></v-select>
      </v-col>

    </v-row>

  </v-container>
</template>

<script>


const url = 'http://localhost:'
const port = '9000/'
//const url = 'http://reports.vimedia.ru/'

// Функция для определения id объекта по имени
function getIdByName(facilities, name) {
  for (let i = 0; i < facilities.length; i++) {
    if (facilities[i].name === name) {
      return facilities[i].id
    }
  }
  return -1;
}

// Функция для определения индекса элементов коллекции
function getIndex(list, id) {
  for (let i = 0; i < list.length; i++) {
    if(list[i].id === id) {
      return list.length-id;
    }
  }
  return -1;
}
export default {
  props: ['reports', 'reportAttr','facilities',
    'facilityId', 'workingTime', 'workingType','facilityName', 'subFacilityName',
    'clickSel1', 'clickSel2', 'clickSel3', 'clickSelSubFac',
  ], // чтобы рабоать с данной переменной и передавать ее выше в корень
  // функция нужна для того чтобы у каждого компонента было свое уникальное хранилище
  data() {
    return {
      facilityNameArray: [],
      facilityName: '',
      workingTimeArray: [],
      workingTime: '',
      workingTypeArray: [],
      workingType: '',
      facilityId: '',
      text: '',
      id: '',
      subAttrVisible: false,
      subFacilityNameArray: [],
      subFacilityName: '',
      filteredSubFacilityNames: []
    }
  },
  methods: {
    clicked1: function(facilityName) {
      this.filteredSubFacilityNames = [] // обнуление перед выбором следующего объекта
      this.clickSel1(facilityName)
      this.facilityName = facilityName

      if (this.facilities.find(f => f.name === facilityName).subFacilities[0]) {this.subAttrVisible = true} else {this.subAttrVisible = false} // показ и скрытия поля для выбор подъобъекта
       this.facilities.find(f => f.name === facilityName).subFacilities.forEach(s => this.filteredSubFacilityNames.push(s.name)) // фильтрация подъобъектов

    },
    clicked2: function(workingTime) {
      this.clickSel2(workingTime)
      this.workingTime = workingTime
    },
    clicked3: function(workingType) {
      this.clickSel3(workingType)
      this.workingType = workingType
    },
    clickedSubFac: function (subFacilityName){
      this.clickSelSubFac(subFacilityName)
      this.subFacilityName = subFacilityName
    },
  },

// Выводит массив имен объектов
  computed: {
    facilityNames: function () {
      this.facilityNameArray = []
      this.facilities.forEach(facility => this.facilityNameArray.push(facility.name))
      return this.facilityNameArray
    },
    subFacilityNames: function () {
      this.subFacilityNameArray = []
      this.facilities.forEach(facility => facility.subFacilities.forEach(subFac => this.subFacilityNameArray.push(subFac.name)))
      return this.subFacilityNameArray
    },
    workingTimes: function () {
      this.workingTimeArray = [1,2,3,4,5,6,7,8]
      return this.workingTimeArray
    },
    workingTypes: function () {
      this.workingTypeArray = ["Проект","Менеджмент","Монтаж","Сборка","ПНР","Сервис","Отпуск","Больничный"]
      return this.workingTypeArray
    },
  },
// функция следит на изменениями переменной
  watch: {
    reportAttr: function (newVal, oldVal) {
      this.text = newVal.text;
      this.id = newVal.id;
    }
  }
}
</script>


<style scoped>

</style>

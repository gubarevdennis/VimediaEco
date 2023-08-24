<template>
  <v-container>
  <v-row>
    <v-col         cols="12"
                   sm="4">
    <v-select
        variant="outlined"
        @update:modelValue="clicked1"
        label="Объект"
        :items="facilityNames"
        :item-value="facilityName"
    ></v-select>
    </v-col>

    <v-col         cols="12"
                   sm="4">
    <v-select
        variant="outlined"
        @update:modelValue="clicked2"
        label="Длительность работ"
        :items="workingTimes"
        :item-value="workingTime"
    ></v-select>
    </v-col>

    <v-col         cols="12"
                   sm="4">
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
    'facilityId', 'workingTime', 'workingType','facilityName',
    'clickSel1', 'clickSel2', 'clickSel3',
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
      id: ''
    }
  },
  methods: {
    clicked1: function(facilityName) {
      this.clickSel1(facilityName)
      this.facilityName = facilityName
    },
    clicked2: function(workingTime) {
      this.clickSel2(workingTime)
      this.workingTime = workingTime
    },
    clicked3: function(workingType) {
      this.clickSel3(workingType)
      this.workingType = workingType
    },
  },

// Выводит массив имен объектов
  computed: {
    facilityNames: function () {
      this.facilityNameArray = []
      this.facilities.forEach(facility => this.facilityNameArray.push(facility.name))
      return this.facilityNameArray
    },
    workingTimes: function () {
      this.workingTimeArray = [1,2,3,4,5,6,7,8]
      return this.workingTimeArray
    },
    workingTypes: function () {
      this.workingTypeArray = ["Проект","Менеджмент","Монтаж","Сборка","ПНР","Сервис","Отпуск","Больничный"]
      return this.workingTypeArray
    }
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

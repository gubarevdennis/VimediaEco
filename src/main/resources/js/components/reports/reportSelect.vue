<template>
  <v-container>

    <v-row align="stretch" justify="center">

      <v-col         cols="10"
                     sm="3">
        <v-autocomplete
            variant="outlined"
            v-model="this.facilityName"
            @update:modelValue="clicked1"
            label="Объект"
            :items="facilityNames"
            :item-value="facilityName"

        ></v-autocomplete>

      </v-col>

      <v-col   v-if="subAttrVisible"      cols="10"
               sm="2">
        <v-select
            v-model="this.subFacilityName"
            variant="outlined"
            @update:modelValue="clickedSubFac"
            label="Подобъект"
            :items="filteredSubFacilities.map(s => s.name)"
            :item-value="subFacilityName"
            hide-no-data="true"
        ></v-select>
      </v-col>

      <v-col         cols="10"
                     sm="3">
        <v-select
            v-model="this.workingTime"
            variant="outlined"
            @update:modelValue="clicked2"
            label="Длительность в часах"
            :items="workingTimes"
            :item-value="workingTime"

        ></v-select>
      </v-col>

      <v-col         cols="10"
                     sm="3">
        <v-select
            justify="center"
            v-model="this.workingType"
            variant="outlined"
            @update:modelValue="clicked3"
            label="Название работы"
            :items="workingTypes"
            :item-value="workingType"
            hide-no-data="true"
        ></v-select>
      </v-col>

    </v-row>

  </v-container>
</template>

<script>


// const url = 'http://localhost:'
// const port = '9000/'
// const url = 'http://reports.vimedia.ru/'
// const port = ''

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
    'clickSel1', 'clickSel2', 'clickSel3', 'clickSelSubFac', 'url', 'port', 'editSelect' ,'clearFormAttr', 'refreshFormAttr',
    'editFormFunction'
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
      facility: '',
      subFacility: '',
      text: '',
      id: '',
      subAttrVisible: false,
      subFacilityNameArray: [],
      subFacilityName: '',
      filteredSubFacilities: [],
      clickSubFac: false,
      clickFac: false,
      job: '',
      jobs: []
    }
  },
  methods: {
    clicked1: function (facilityName) {
      this.filteredSubFacilities = [] // обнуление перед выбором следующего объекта
      this.clickSel1(facilityName)
      this.facilityName = facilityName

      this.facility = this.facilities.find(f => f.name === facilityName)
      this.clickFac = true;
      this.clickSubFac = false;
      this.subAttrVisible = false;

      if (this.facilities.find(f => f.name === facilityName).subFacilities[0]) {
        this.subAttrVisible = true
        this.facilities.find(f => f.name === facilityName).subFacilities.forEach(s => this.filteredSubFacilities.push(s)) // фильтрация подъобъектов
      } else {
        this.subAttrVisible = false
      } // показ и скрытия поля для выбор подъобъекта

      this.editSelect()
    },
    clicked2: function (workingTime) {
      this.clickSel2(workingTime)
      this.workingTime = workingTime

      this.editSelect()
    },
    clicked3: function (workingType) {
      this.workingType = workingType
      this.job = this.jobs.find(j => (j.name === workingType))

      console.log("this.workingTypeArray")
      console.log(this.workingTypeArray)

      console.log("this.job")
      console.log(this.job)

      this.clickSel3(this.job)

      this.editSelect()
    },
    clickedSubFac: function (subFacilityName) {
      this.clickSelSubFac(subFacilityName)
      this.subFacilityName = subFacilityName

      this.clickSubFac = true;

      this.subFacility = this.filteredSubFacilities.find(f => f.name === subFacilityName);
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
      this.workingTypeArray = [];
      this.jobs = [];

      if (this.subAttrVisible) {
        if (this.clickSubFac)
          this.axios.get('api/job/subFacility/' + this.subFacility.id).then(result =>
              result
                  .data
                  .forEach(j => {
                        if (j.name)
                          this.jobs.push(j)
                        this.workingTypeArray.push(j.name)
                      }
                  )
          )
      } else {
        if (this.clickFac)
          this.axios.get('api/job/facility/' + this.facility.id).then(result =>
              result
                  .data
                  .forEach(j => {
                        this.jobs.push(j)
                        if (j.name)
                          this.workingTypeArray.push(j.name)
                      }
                  )
          )
      }
      // this.workingTypeArray = ['Черновой монтаж', 'Чистовой монтаж','Шефмонтаж',  'Сборка щитов',
      //   'Концептуальное проектирование', 'Рабочее проектирование', 'Расключение шкафов',
      //   'ПНР', 'Сервис','Авторский надзор', 'Менеджмент' , 'Отпуск по семейным обстоятельствам',
      //   'Отпуск без сохранения ЗП', 'Отпуск оплачиваемый', 'Больничный']

      return this.workingTypeArray
    },
  },
// функция следит на изменениями переменной
  watch: {
    clearFormAttr: function (newVal, oldVal) {
      this.workingType = ''
      this.workingTime = ''
      this.subFacilityName = ''
      this.facilityName = ''
      this.text = ''
      this.id = ''

      this.refreshFormAttr()
    },
    reportAttr: function (newVal, oldVal) {
      this.workingType = newVal.typeOfWork;
      this.workingTime = newVal.hoursOfWorking;
      this.subFacilityName = newVal.subFacility ?  newVal.subFacility.name : '';
      if (newVal.subFacility) {this.subAttrVisible = true} else {this.subAttrVisible = false}// показываем дополнительную строку при наличии подобъекта
      this.facilityName = newVal.facility ? newVal.facility.name : '';
      this.text = newVal.text;
      this.id = newVal.id;

      this.editFormFunction(newVal)

    }
  }
}
</script>


<style scoped>

</style>

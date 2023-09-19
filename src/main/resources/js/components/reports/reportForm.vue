<template>
  <v-container align="center">

    <v-row >
      <report-select v-for="form in reportForms" v-bind:key="form" @click="clickOnSelect(form)"
                     :reports="reports" :reportAttr="reportAttr" :facilities="facilities"
                     :subFacilityName="subFacilityName"
                     :workingType="workingType"
                     :workingTime="workingTime"
                     :facilityId="facilityId"
                     :clickSel1="clickSel1"
                     :clickSel2="clickSel2"
                     :clickSel3="clickSel3"
                     :clickSelSubFac="clickSelSubFac"


      ></report-select>
    </v-row>
    <v-btn width="180" align="center" @click="addReportForm">
      +
    </v-btn>

    <br>
    <br>


    <v-select
        v-if="!datePickerShow"
        variant="outlined"
        @update:modelValue="clicked"
        label="Сегодня"
        :items="['Сегодня','Вчера','Позавчера','Три дня назад','Четыре дня назад','Пять дней назад','Шесть дней назад']"
        :item-value="offsetText"
    >
    </v-select>


    <v-btn rounded="lg" variant="outlined"  style="font-size: 20px" height="30"  align="center" @click="datePickerShowFunc">
      <v-text v-if="datePickerShow" style=" font-size: 15px;" >
        Скрыть календарь
      </v-text>
      <v-text v-if="!datePickerShow" style=" font-size: 15px;" >
        Выбрать на календаре
      </v-text>
    </v-btn>

    <br>
    <br>
    <VDatePicker         v-if="datePickerShow"
                         v-model="datePick" mode="date" />


    <v-row justify="center">
      <v-text v-if="errorFields" style="color: red; text-align: center" >
        Отчет не отправлен. Укажите все необходимые поля для отправки отчета!
        <br>
        Отчетный день в компании Vimedia Group составляет 8 часов
      </v-text>
    </v-row>
    <br>
    <br>
    <v-row justify="center" justify-sm="center">
      <v-text v-if="reportSendConfirmField" style="color: green; font-size: 18px;" >
        Отчет отправлен. Спасибо за работу!
      </v-text>
    </v-row>


    <br>
    <br>

    <v-row align="center" justify="center" >
      <v-text-field
          placeholder="Комментарий к отчету..."
          variant="solo-filled"
          v-model="text">
      </v-text-field>
    </v-row>

    <br>
    <br>

    <v-row justify="center" >
      <!--// v-model для того чтобы с input пробросить в data в поле text -->
      <v-btn color="#EBB652"  style="font-size: 20px" height="50" width="260" @click="save">
        Отправить отчет
      </v-btn>
    </v-row>
  </v-container>
</template>

<script>


// Функция для определения id объекта по имени
import ReportSelect from "./reportSelect.vue";

// const url = 'http://localhost:'
// const port = '9000/'
// const url = 'http://reports.vimedia.ru/'
// const port = ''

function getIdByName(facilities, name) {
  for (let i = 0; i < facilities.length; i++) {
    if (facilities[i].name === name) {
      return facilities[i].id
    }
  }
  return -1;
}

// Функция для определения индекса элементов коллекции
function getIndexForPost(list, id) {
  for (let i = 0; i < list.length; i++) {
    if(list[i].id === id) {
      return i;
    }
  }
  return -1;
}

// Функция для определения индекса элементов коллекции
function getIndex(list, id) {
  for (let i = 0; i < list.length; i++) {
    if(list[i].formId === id) {
      return id;
    }
  }
  return -1;
}
export default {
  components: {ReportSelect},
  props: ['reports', 'reportAttr', 'facilities', 'subFacilities',
    'editReportStatus', 'addFacilityFromForm', 'url', 'port'], // чтобы рабоать с данной переменной и передавать ее выше в корень
  // функция нужна для того чтобы у каждого компонента было свое уникальное хранилище
  data() {
    return {
      facilityName: {},
      workingTime: {},
      workingType: {},
      facilityNames: [],
      workingTimes: [],
      workingTypes: [],
      facilityId: '',
      text: '',
      id: '',
      reportForms: [1],
      countReportForms: 1,
      reportsForSend: [], // массив отчетов для записи в базу данных
      form: 1,
      errorFields: false,
      sumHoursPerDay: 0,
      reportDay: '',
      offsetText: '',
      subFacilityName: '',
      subFacilityNames: [],
      reportSendConfirmField: false,
      datePick: '',
      datePickerShow: false
    }
  },
  // указываем связь данного компонента с полученными от сервара данными
  created: function () {
    this.axios.get("api/facility").then(result => {
          // Вставляем объекты только вслучае если их не было до этого в массиве на фронтенде
          return result.data.forEach(facility => {
            if (this.facilities.find((f) => f.id === facility.id) === undefined)
              return this.facilities.push(facility)
          })
        }
    )
  },
  methods: {
    save: function () {
      this.defaultFacilityName = ''
      // Обнуляем счетчик времени наработки в день
      this.sumHoursPerDay = 0

      // Проверка на null заполненных полей
      if(this.facilityNames[0] === undefined || this.workingTypes[0] === undefined ||
          this.workingTimes[0] === undefined)
      {
        // Если null - выводим ошибку
        this.errorFields = true
      } else {

        if (this.text === '') {
          this.text = 'Отчет сформирован без комментариев'
        }

        // Обнуляем массив отчетов для отправки
        this.reportsForSend = []

        // Выводим текущую дату
        var date = new Date()

        if(this.offsetAttr) {
          date.setDate(date.getDate() + this.offsetAttr)
        }

        if(this.datePick && this.datePickerShow) {
          date.setFullYear(this.datePick.getFullYear())
          date.setMonth(this.datePick.getMonth())
          date.setDate(this.datePick.getDate())
        }

        // Форматируем текущую дату (добавляем нули, гду нужно и когда нужно)
        this.reportDay = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate())
            + '-' + (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1)) + '-' + date.getFullYear();

        // Готовим массив отчетов для последующей отправки
        this.facilityNames.forEach(f => {
              this.sumHoursPerDay = this.sumHoursPerDay + this.workingTimes[f.formId - 1].name

              this.reportsForSend.push(
                  {
                    id: this.id ? this.id : undefined,
                    facility: {id: getIdByName(this.facilities, f.name)},
                    subFacility: {name: this.subFacilityName.name},
                    typeOfWork: this.workingTypes[f.formId - 1].name,
                    text: this.text,
                    hoursOfWorking: this.workingTimes[f.formId - 1].name,
                    reportDay: this.reportDay
                  }
              )
            }
        )
        ////////////////////////////////////////////////////
        // Отправка в базу данных всех заполненных отчетов//
        ////////////////////////////////////////////////////
        this.reportsForSend.forEach(r => {
              console.log(r)

              // Если все поля заполнены, то делаем запрос - нет ошибка
              if (r.facility && r.hoursOfWorking && r.typeOfWork
                  && ((this.sumHoursPerDay <= 8) ? true : this.editReportStatus)) {
                // если нет id создаем новую позицию
                this.axios.post( 'api/report', r).then(data => {
                  if (data.data === '') {this.errorFields = true} else {
                    this.reportSendConfirmField = true // выводим сообщение об успешной отправке
                  }
                  if (this.id) {
                    let index = getIndexForPost(this.reports, data.data.id) // получеам индекс коллекции
                    this.reports.splice(index, 1, data.data);
                    console.log('index -> ' + index)
                  } else {
                    this.reports.push(data.data)
                  }

                  // Очищаем поля
                  this.reportForms = [1]
                  this.text = ''
                  this.id = '';
                })
                this.errorFields = false
              } else {
                this.errorFields = true
              }
            }
        )

      }
    },
    clickOnSelect: function (form) {
      this.reportSendConfirmField = false // убираем сообщение при редактировании полей
      this.form = form
    },

    clickSel1: function(facilityName) {
      if (this.facilityNames.find(f => (f.id === this.facilityName.formId)) !== undefined) {
        this.facilityName = {
          name: facilityName
        }
      } else {
        this.facilityName = {
          name: facilityName,
          formId: this.form
        }
      }

      if (this.facilityNames.find(f => (f.formId === this.facilityName.formId)) !== undefined) {
        // Заменяем элемент массива отчетов для отправки
        let index = getIndex(this.facilityNames, this.facilityName.formId) // получеам индекс коллекции
        console.log(index + " index")
        this.facilityNames.splice(index-1, 1, this.facilityName);
      } else {
        this.facilityNames.push(this.facilityName)
      }

      // Массив имен объектов для отправки
      console.log(this.facilityNames)
    },
    clickSel2: function(workingTime) {

      if (this.workingTimes.find(f => (f.id === this.workingTime.formId)) !== undefined) {
        this.workingTime = {
          name: workingTime
        }
      } else {
        this.workingTime = {
          name: workingTime,
          formId: this.form
        }
      }

      if (this.workingTimes.find(f => (f.formId === this.workingTime.formId)) !== undefined) {
        // Заменяем элемент массива отчетов для отправки
        let index = getIndex(this.workingTimes, this.workingTime.formId) // получеам индекс коллекции
        console.log(index + " index")
        this.workingTimes.splice(index-1, 1, this.workingTime);
      } else {
        this.workingTimes.push(this.workingTime)
      }

      // Массив имен объектов для отправки
      console.log(this.workingTimes)
    },
    clickSel3: function(workingType) {
      if (this.workingTypes.find(f => (f.id === this.workingType.formId)) !== undefined) {
        this.workingType = {
          name: workingType
        }
      } else {
        this.workingType = {
          name: workingType,
          formId: this.form
        }
      }

      if (this.workingTypes.find(f => (f.formId === this.workingType.formId)) !== undefined) {
        // Заменяем элемент массива отчетов для отправки
        let index = getIndex(this.workingTypes, this.workingType.formId) // получеам индекс коллекции
        console.log(index + " index")
        this.workingTypes.splice(index-1, 1, this.workingType);
      } else {
        this.workingTypes.push(this.workingType)
      }

      // Массив имен объектов для отправки
      console.log(this.workingTypes)
    },
    clickSelSubFac: function (subFacilityName) {
      if (this.subFacilityNames.find(f => (f.id === this.subFacilityNames.formId)) !== undefined) {
        this.subFacilityName = {
          name: subFacilityName
        }
      } else {
        this.subFacilityName = {
          name: subFacilityName,
          formId: this.form
        }
      }

      if (this.subFacilityNames.find(f => (f.formId === this.subFacilityName.formId)) !== undefined) {
        // Заменяем элемент массива отчетов для отправки
        let index = getIndex(this.subFacilityNames, this.subFacilityName.formId) // получеам индекс коллекции
        console.log(index + " index")
        this.subFacilityNames.splice(index-1, 1, this.subFacilityName);
      } else {
        this.subFacilityNames.push(this.subFacilityName)
      }
    },
    addReportForm: function (){
      this.countReportForms++
      this.reportForms.push(this.countReportForms)
    },
    clicked: function(offsetText) {
      this.offsetText = offsetText
      switch (offsetText) {
        case 'Сегодня':
          this.offsetAttr = 0;
          break;
        case 'Вчера':
          this.offsetAttr = -1;
          break;
        case 'Позавчера':
          this.offsetAttr = -2;
          break;
        case 'Три дня назад':
          this.offsetAttr = -3;
          break;
        case 'Четыре дня назад':
          this.offsetAttr = -4;
          break;
        case 'Пять дней назад':
          this.offsetAttr = -5;
          break;
        case 'Шесть дней назад':
          this.offsetAttr = -6;
          break;
        default:
          this.offsetAttr = 0;

      }
    },
    datePickerShowFunc: function () {
      var old =this.datePickerShow;
      this.datePickerShow = !old;
    }
  },

// Выводит массив имен объектов
  computed: {
    reportForms: function () {
      return this.reportForms
    }
  },
// функция следит на изменениями переменной
  watch: {
    reportAttr: function (newVal, oldVal) {

      this.text = newVal.text;
      this.id = newVal.id;
    },

  }
}
</script>

<style>

</style>

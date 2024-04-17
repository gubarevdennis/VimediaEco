<template>
  <v-container align="center">


<!--      <report-select-->
<!--          :clearFormAttr="clearFormAttr"-->
<!--          :refreshFormAttr="refreshFormAttr"-->

<!--          v-for="form in reportForms" v-bind:key="form"-->

<!--          @click="clickOnSelect(form)"-->
<!--          :reports="reports"-->
<!--          :reportAttr="reportAttr"-->
<!--          :facilities="facilities"-->
<!--          :subFacilityName="subFacilityName"-->
<!--          :workingType="workingType"-->
<!--          :workingTime="workingTime"-->
<!--          :facilityId="facilityId"-->
<!--          :clickSel1="clickSel1"-->
<!--          :clickSel2="clickSel2"-->
<!--          :clickSel3="clickSel3"-->
<!--          :clickSelSubFac="clickSelSubFac"-->
<!--          :editSelect="editSelect"-->
<!--          :editFormFunction="editFormFunction"-->
<!--          -->
<!--          :reportFormsWithData="reportFormsWithData"-->
<!--      ></report-select>-->
    <v-row v-for="formData in reportFormsWithData" v-bind:key="formData">
        <v-col>
          <report-selected v-if="facilities"
                           :formData="formData"
                            @click="reportSendConfirmField = false">
          </report-selected>
        </v-col>
    </v-row>

    <div class="justify-center">
        <v-btn class="ma-2" width="180" align="center" @click="addReportFormWithData">
          +
        </v-btn>
        <v-btn v-if="deleteFormWithData" class="ma2" width="180" align="center" @click="deleteReportFormWithData">
          -
        </v-btn>
    </div>



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
      <v-text v-if="isErrorMessage" style="color: red; text-align: center" >
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
      <v-btn color="#EBB652"  style="font-size: 20px" height="50" width="260" @click="postReports">
        Отправить отчет
      </v-btn>
    </v-row>
  </v-container>
</template>

<script>


// Функция для определения id объекта по имени
import ReportSelect from "./reportSelect.vue";
import ReportSelected from "./reportSelected.vue"

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

function getyName(facilities, name) {
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
  props: ['reports', 'reportAttr', 'facilities', 'subFacilities',
    'editReportStatus', 'addFacilityFromForm', 'url', 'port', 'editSelect', 'report'], // чтобы рабоать с данной переменной и передавать ее выше в корень
  // функция нужна для того чтобы у каждого компонента было свое уникальное хранилище
  components: {ReportSelect, ReportSelected},
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
      datePickerShow: false,
      clearFormAttr: false,
      deleteFormVisual: false,

      reportFormsWithData: [],
      reportsForSendWithData: [],

      deleteFormWithData: false,
      isErrorMessage: false
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
  mounted() {
    this.createReportFormWithData(this.countReportForms)
  },

  methods: {
    createReportFormWithData: function(id){
      let ReportForm = {
        'id': id,
        'facility': '',
        'subFacility': '',
        'hoursOfWorking': '',
        'workingType': '',
        'facility_id': '',
        'subFacility_id': '',
        'job_id': ''
      }
      this.reportFormsWithData.push(ReportForm)
    },
    editSelect: function () {

    },
    clearForm: function () {
      console.log('clearFormAttr')
      this.clearFormAttr = true;
    },
    refreshFormAttr: function () {
      this.clearFormAttr = false
    },
    editFormFunction: function (report) {
      if (this.editReportStatus) {

        console.log('editFormFunction')
        console.log(report)

        if (report.hoursOfWorking) { this.workingTimes.push(
            {
              name: report.hoursOfWorking,
              formId: 1
            }
        )

          this.workingTime =
              {
                name: report.hoursOfWorking,
                formId: 1
              }
        }
        // if (report.typeOfWork) { this.workingTypes.push(
        //     {
        //       name: report.typeOfWork,
        //       formId: 1
        //     }
        // )

        // this.workingType =
        //     {
        //       name: report.typeOfWork,
        //       formId: 1
        //     }

        if (report.job) { this.workingTypes.push(
            {
              name: report.job,
              formId: 1
            }
        )

          this.workingType =
              {
                name: report.job,
                formId: 1
              }
        }
        if (report.subFacility) { this.subFacilityNames.push(
            {
              name: report.subFacility.name,
              formId: 1
            })

          this.subFacilityName =
              {
                name: report.subFacility.name,
                formId: 1
              }
        }
        if (report.facility) { this.facilityNames.push(
            {
              name: report.facility.name,
              formId: 1
            })

          this.facilityName =
              {
                name: report.facility.name,
                formId: 1
              }
        }

        this.text = report.text;
        this.text = report.text;
        this.id = report.id;

        console.log('editFormFunction array')
        console.log(this.facilityNames)

      }
    },
    checkFieldsInReport: function (report) {
      if(report.facility == '') {
        console.log('no facility')
        return false
      }

      let facilitySelected = this.facilities.find(el=> el.name == report.facility)

      if(facilitySelected.subFacilities.length > 0){
        if(report.subFacility == '') {
          console.log('no subfacility')
          return false
        }
      }

      if(report.hoursOfWorking == '') {
        console.log('no hours')
        return false
      }

      if(report.workingType == '') {
        console.log('no working type')
        return false
      }

      return true
    },
    getHoursOfWorkingInReports: function (reports) {
      let hoursOfWorkingSum = 0
      reports.forEach(report=> {
        hoursOfWorkingSum += parseInt(report.hoursOfWorking)
      })

      return hoursOfWorkingSum
    },
    sortReportByDay : function() {

      let choosenDate = new Date()

      if(this.offsetAttr) {
        choosenDate.setDate(choosenDate.getDate() + this.offsetAttr)
      }

      if(this.datePick && this.datePickerShow) {
        choosenDate.setFullYear(this.datePick.getFullYear())
        choosenDate.setMonth(this.datePick.getMonth())
        choosenDate.setDate(this.datePick.getDate())
      }

      let dateForReport = (choosenDate.getDate() < 10 ? '0' + choosenDate.getDate() : choosenDate.getDate())
          +'-'+(choosenDate.getMonth()+1 < 10 ? '0' + (choosenDate.getMonth()+1) : (choosenDate.getMonth()+1))+'-'+choosenDate.getFullYear()

      return this.reports.filter((r) => {
        return r.reportDay === dateForReport })
    },
    postReports: function () {
      this.reportFormsWithData.forEach(report=> {
        if(!this.checkFieldsInReport(report))
          this.isErrorMessage = true
        else
          this.isErrorMessage = false
      })

      let hoursOfWorkingSum = this.getHoursOfWorkingInReports(this.reportFormsWithData)
      let reportsByDay = this.sortReportByDay()
      let hoursOfReportsSum = this.getHoursOfWorkingInReports(reportsByDay)

      if(!this.isErrorMessage){
        if(hoursOfWorkingSum + hoursOfReportsSum > 8)
          this.isErrorMessage = true
        else
          this.isErrorMessage = false
      }

      if(this.isErrorMessage) {
        console.log('no')
      } else {
        if (this.text === '') {
          this.text = 'Отчет сформирован без комментариев'
        }

        let date = new Date()
        if (this.offsetAttr) {
          date.setDate(date.getDate() + this.offsetAttr)
        }

        if (this.datePick && this.datePickerShow) {
          date.setFullYear(this.datePick.getFullYear())
          date.setMonth(this.datePick.getMonth())
          date.setDate(this.datePick.getDate())
        }

        this.reportDay = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate())
            + '-' + (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1))
            + '-' + date.getFullYear();

        this.reportFormsWithData.forEach(report => {
          this.reportsForSendWithData.push(
              {
                facility: {id: report.facility_id, name: report.facility},
                subFacility: {id: report.subFacility_id ,name: report.subFacility},
                job: {name: report.workingType, id: report.job_id},
                text: this.text,
                hoursOfWorking: report.hoursOfWorking,
                reportDay: this.reportDay,
                typeOfWork: report.workingType
              }
          )
        })

        if(this.reportsForSendWithData){
          this.reportsForSendWithData.forEach(report => {
            this.axios.post('api/report', report).then(data => {
              if(data.data) {
                this.reportSendConfirmField = true
                this.reports.push(data.data)
                console.log('post new report')
                console.log(data.data)
              }
            })
          })

        }
        this.reportsForSendWithData = []
        this.countReportForms = 1
        this.reportFormsWithData = []
        this.createReportFormWithData(this.countReportForms)
        this.deleteFormWithData = false
      }
    },

    // save: function () {
    //   this.defaultFacilityName = ''
    //   // Обнуляем счетчик времени наработки в день
    //   this.sumHoursPerDay = 0
    //
    //   // Проверка на null заполненных полей
    //   if ( this.facilityNames[0] === undefined
    //       || this.workingTypes[0] === undefined
    //       || this.workingTimes[0] === undefined )
    //   {
    //     // Если null - выводим ошибку
    //     this.errorFields = true
    //   } else {
    //
    //     if (this.text === '') {
    //       this.text = 'Отчет сформирован без комментариев'
    //     }
    //
    //     // Обнуляем массив отчетов для отправки
    //     this.reportsForSend = []
    //
    //     // Выводим текущую дату
    //     var date = new Date()
    //
    //     if (this.offsetAttr) {
    //       date.setDate(date.getDate() + this.offsetAttr)
    //     }
    //
    //     if (this.datePick && this.datePickerShow) {
    //       date.setFullYear(this.datePick.getFullYear())
    //       date.setMonth(this.datePick.getMonth())
    //       date.setDate(this.datePick.getDate())
    //     }
    //
    //     // Форматируем текущую дату (добавляем нули, гду нужно и когда нужно)
    //     this.reportDay = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate())
    //         + '-' + (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1)) + '-' + date.getFullYear();
    //
    //     // Готовим массив отчетов для последующей отправки
    //     this.facilityNames.forEach(f => {
    //           this.sumHoursPerDay = this.sumHoursPerDay + this.workingTimes[f.formId - 1].name
    //
    //           this.reportsForSend.push(
    //               {
    //                 id: this.id ? this.id : undefined,
    //                 facility: {id: getIdByName(this.facilities, f.name), name: f.name},
    //                 subFacility: {name: this.subFacilityNames[f.formId - 1] ? this.subFacilityNames[f.formId - 1].name : ''},
    //                 typeOfWork: this.workingTypes[f.formId - 1].name.name,
    //                 job: {id : this.workingTypes[f.formId - 1].name.id},
    //                 text: this.text,
    //                 hoursOfWorking: this.workingTimes[f.formId - 1].name,
    //                 reportDay: this.reportDay
    //               }
    //           )
    //         }
    //     )
    //     ////////////////////////////////////////////////////
    //     // Отправка в базу данных всех заполненных отчетов//
    //     ////////////////////////////////////////////////////
    //     this.reportsForSend.forEach(r => {
    //           console.log(r)
    //
    //           // Если все поля заполнены, то делаем запрос - нет ошибка
    //           if (r.facility && r.hoursOfWorking && r.job
    //               && ((this.sumHoursPerDay <= 8) ? true : this.editReportStatus)) {
    //
    //             ///////// если есть id редактируем старую позицию /////////
    //             if (r.id) {
    //               this.axios.post('api/report/' + r.id, r).then(data => {
    //                 if (data.data === '') {
    //                   this.errorFields = true
    //                 } else {
    //                   this.reportSendConfirmField = true // выводим сообщение об успешной отправке
    //
    //                   let index = getIndexForPost(this.reports, data.data.id) // получеам индекс коллекции
    //                   this.reports.splice(index, 1, data.data);
    //                   console.log('index -> ' + index)
    //                 }
    //
    //                 // Очищаем поля
    //                 this.clearForm() // очищение формы
    //
    //                 this.facilityNames = []
    //                 this.subFacilityNames = []
    //                 this.workingTypes = []
    //                 this.workingTimes = []
    //
    //                 this.reportsForSend = []
    //
    //                 this.text = ''
    //                 this.id = ''
    //
    //
    //               })
    //             } else {
    //               ////////// если нет id создаем новую позицию ////////
    //               this.axios.post('api/report', r).then(data => {
    //                 if (data.data === '') {
    //                   this.errorFields = true
    //                 } else {
    //                   this.reportSendConfirmField = true // выводим сообщение об успешной отправке
    //                 }
    //                 this.reports.push(data.data)
    //
    //                 // Очищаем поля
    //                 this.clearForm() // очищение формы
    //
    //                 this.facilityNames = []
    //                 this.subFacilityNames = []
    //                 this.workingTypes = []
    //                 this.workingTimes = []
    //
    //                 this.reportsForSend = []
    //
    //                 this.text = ''
    //                 this.id = ''
    //               })
    //             }
    //             this.errorFields = false
    //           }
    //           else
    //           {
    //             this.errorFields = true
    //           }
    //
    //         }
    //     )
    //
    //   }
    // },
    clickOnSelect: function (form) {
      this.reportSendConfirmField = false // убираем сообщение при редактировании полей
      this.form = form
      console.log('form')
      console.log(form)
    },

    clickSel1: function(facilityName) {
      if (this.facilityNames.find(f => (f.id === this.facilityName.formId)) !== undefined) { // если форма найдена, то просто заменяем имя
        this.facilityName = {
          name: facilityName
        }
      } else {
        this.facilityName = {
          name: facilityName,
          formId: this.form
        }
      }

      if (this.facilityNames.find(f => (f.formId === this.facilityName.formId)) !== undefined) { // если форма найдена, то просто заменяем имя
        // Заменяем элемент массива отчетов для отправки
        let index = getIndex(this.facilityNames, this.facilityName.formId) // получеам индекс коллекции
        console.log(index + " index")
        this.facilityNames.splice(index-1, 1, this.facilityName);
      } else {
        this.facilityNames.push(this.facilityName)
      }

      // Массив имен объектов для отправки
      console.log('facilityNames')
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
      console.log('workingTimes')
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
      console.log('workingTypes')
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
    // addReportForm: function (){
    //   this.countReportForms++
    //   this.reportForms.push(this.countReportForms)
    //   this.deleteFormVisual = true
    // },
    addReportFormWithData: function () {
      this.countReportForms++
      this.createReportFormWithData(this.countReportForms)
      this.deleteFormWithData = true
    },
    deleteReportFormWithData: function () {
      if(this.countReportForms > 1) {
        this.countReportForms--
        this.reportFormsWithData.pop()
      } else {
        this.deleteFormWithData = false
      }
    },
    // deleteReportForm: function () {
    //   this.countReportForms--
    //   this.reportForms.pop()
    //   this.facilityNames.pop()
    //   this.subFacilityNames.pop()
    //   this.workingTypes.pop()
    //   this.workingTimes.pop()
    //   this.reportsForSend.pop()
    //
    //   if (this.reportForms[1] === undefined) {
    //     this.deleteFormVisual = false
    //   }
    // },
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
      this.facilityName = newVal.facilityName;
      this.workingType = newVal.workingType;
      this.workingTime = newVal.workingTime;
      this.subFacilityName = newVal.subFacilityName;
      this.text = newVal.text;
      this.id = newVal.id;
    },
  }
}
</script>

<style>

</style>

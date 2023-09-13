<template>
  <v-container align="start">
    <v-text-field
        placeholder="Введите название объекта..."
        variant="solo-filled"
        v-model="nameFac">
    </v-text-field>

    <v-text-field v-if="editFacilityStatus ? editFacilityStatus : subAttrVisible "
                  placeholder="Введите название подобъекта..."
                  variant="solo-filled"
                  v-model="nameSubFac">
    </v-text-field>

    <br>
    <br>

    <v-row align="center" justify="center">
      <v-color-picker v-model="colorNumber" show-swatches></v-color-picker>
    </v-row>

    <!--// v-model для того чтобы с input пробросить в data в поле text -->
    <br>
    <br>

    <v-row align="center" justify="center">
      <v-btn @click="save">
        <div v-if="!(editFacilityStatus ? editFacilityStatus : subAttrVisible)">Добавить объект</div>
        <div v-if="(editFacilityStatus && nameSubFac ? editFacilityStatus : subAttrVisible)">Добавить подъобъект</div>
        <div v-if="(!nameSubFac && (editFacilityStatus ? editFacilityStatus : subAttrVisible))">Изменить объект</div>
      </v-btn>
    </v-row>

  </v-container>
</template>

<script>


// const url = 'http://localhost:'
// const port = '9000/'
// const url = 'http://reports.vimedia.ru/'
// const port = ''

// Функция для определения индекса элементов коллекции
function getIndex(list, id) {
  for (let i = 0; i < list.length; i++) {
    if(list[i].id === id) {
      return i;
    }
  }
  return -1;
}
export default {
  props: ['facilities', 'subFacilities', 'facilityAttr', 'subFacilityAttr',
    'addSubFacility', 'editFacilityStatus', 'editFacilityStatusFunc'], // чтобы рабоать с данной переменной и передавать ее выше в корень
  // функция нужна для того чтобы у каждого компонента было свое уникальное хранилище
  data() {
    return {
      nameFac: '',
      nameSubFac: '',
      id: '',
      idSub: '',
      colorNumber: '',
      subAttrVisible: false,
      subFacilityNameArray: [],
      subFacilityName: '',
      subFacilityNames: []
    }
  },
  computed: {
    subFacilityNames: function () {
      this.subFacilityNameArray = []
      this.facilities.forEach(facility => facility.subFacilities.forEach(subFac => this.subFacilityNameArray.push(subFac.name)))
      return this.subFacilityNameArray
    },
  },
  methods: {
    save: function () {

      this.editFacilityStatusFunc(false)

      var facility = this.colorNumber === '' ?
          {
            name: this.nameFac,
          } :
          {
            name: this.nameFac,
            color: this.colorNumber
          };

      var subFacility = {name: this.nameSubFac, color: this.colorNumber, facility: {name: this.nameFac}};

      if (this.nameSubFac === '') {
        if (this.id) {
          // Добавляем id в facility
          facility = {id: this.id, name: this.nameFac, color: this.colorNumber};

          // если есть id в data, тогда обноволяем информацию
          this.axios.post('api/facility', facility).then(res => {
            let index = getIndex(this.facilities, res.data.id) // получеам индекс коллекции
            this.facilities.splice(index, 1, res.data);
            this.nameFac = '';
            this.id = '';
          })
        } else {
          // если нет id создаем новую позицию
          this.axios.post('api/facility', facility).then(data => {
            this.facilities.push(data.data)
            this.nameFac = '';
            this.id = '';
          })
        }
      }

      if (this.nameSubFac !== '') {
        // Логика добавления подобъекта
        this.axios.post('api/subFacility', subFacility).then(data => {
          this.subFacilityNames.push(data.data.name)

          // Цепляем подбобъект для вывода
          this.addSubFacility(data.data)
          this.nameSubFac = ''
          this.idSub = '';
        })
      }
    },
    clickedSubFac: function (){
      return (this.subAttrVisible === true) ?  this.subAttrVisible = false : this.subAttrVisible = true
    }
  },


// функция следит на изменениями переменной
  watch: {
    facilityAttr: function (newVal, oldVal) {
      this.nameFac = newVal.name;
      this.id = newVal.id;
      this.colorNumber = newVal.color
    },
    subFacilityAttr: function (newVal, oldVal) {
      this.nameSubFac = newVal.name;
      this.idSub = newVal.id;
    },
    colorNumber: function (colorNumber) {
      this.colorNumber = colorNumber
    }
  }
}
</script>

<style>

</style>

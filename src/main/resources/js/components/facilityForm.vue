<template>
  <v-container align="start">
    <v-text-field
        placeholder="Введите название объекта..."
        variant="solo-filled"
        v-model="name">
    </v-text-field>

    <v-row align="center" justify="center">
    <v-color-picker v-model="colorNumber" show-swatches></v-color-picker>
    </v-row>
    <!--// v-model для того чтобы с input пробросить в data в поле text -->
    <br>
    <br>

    <v-row align="center" justify="center">
      <v-btn @click="save">
        Добавить объект
      </v-btn>
    </v-row>

  </v-container>
</template>

<script>

const url = 'http://sisyphos.vimedia.ru/'

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
  props: ['facilities', 'facilityAttr'], // чтобы рабоать с данной переменной и передавать ее выше в корень
  // функция нужна для того чтобы у каждого компонента было свое уникальное хранилище
  data() {
    return {
      name: '',
      id: '',
      colorNumber: ''
    }
  },
  methods: {
    save: function () {

      var facility = {
        name: this.name,
        color: this.colorNumber
      };

      if (this.id) {
        // Добавляем id в facility
        facility = {id: this.id, name: this.name, color: this.colorNumber};

        // если есть id в data, тогда обноволяем информацию
        this.axios.post(url + 'api/facility', facility).then(res => {
                     let index = getIndex(this.facilities, res.data.id) // получеам индекс коллекции
          console.log(index)
          console.log(this.facilities)
                     this.facilities.splice(index, 1, res.data);
                     this.name = '';
                     this.id = '';
        })

      } else {
        // если нет id создаем новую позицию
        this.axios.post(url + 'api/facility', facility).then(data => {
              this.facilities.push(data.data)
              this.name = ''
              this.id = '';
            })
      }
    }
  },


// функция следит на изменениями переменной
  watch: {
    facilityAttr: function (newVal, oldVal) {
      this.name = newVal.name;
      this.id = newVal.id;
    },
    colorNumber: function (colorNumber) {
      this.colorNumber = colorNumber
    }
  }
}
</script>

<style>

</style>

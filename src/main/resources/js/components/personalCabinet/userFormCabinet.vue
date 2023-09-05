<template>
  <v-container  align="start">
    <v-card-title  align="start">
      {{profile}}
    </v-card-title>
    <v-card-text   align="start">
      <v-col>
        <div style="color: #EBB652">
          {{role}}
        </div>
      </v-col>

      <v-col>
      <v-row justify="start">
        <v-text-field v-if="changPassVisible"
                      placeholder="Введите пароль..."
                      variant="solo-filled"
                      v-model="password">
        </v-text-field>
      </v-row>

        <br>

        <br>
        <!--// v-model для того чтобы с input пробросить в data в поле text -->
        <v-btn style="font-size: 10px; color: #EBB652" variant="outlined"   rounded="lg" width="150" height="30"  v-if="!changPassVisible" @click="change">
          Изменить пароль
        </v-btn>

      <v-row justify="start">

        <v-banner-text v-if="errorStatus" style="color: red; text-align: center" >
          <br>

          Ошибка! Поле не может быть пустым!
        </v-banner-text>
          <v-banner-text v-if="passSendStatus && !errorStatus" style="color: green; text-align: center" >
            <br>

            Пароль успешно изменен!
          </v-banner-text>
      </v-row>
      </v-col>
    </v-card-text>






    <v-row justify="center">
    <!--// v-model для того чтобы с input пробросить в data в поле text -->
    <v-btn color="#EBB652" v-if="changPassVisible" @click="save">
      Сохранить
    </v-btn >
      <br>
      <br>
    </v-row>

  </v-container>
</template>

<script>

const url = 'http://localhost:'
const port = '9000/'
//const url = 'http://reports.vimedia.ru/'

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
  props: ['users', 'userAttr', 'role', 'profile', 'profileId'], // чтобы рабоать с данной переменной и передавать ее выше в корень
  // функция нужна для того чтобы у каждого компонента было свое уникальное хранилище
  data() {
    return {
      id: '',
      name: '',
      password: '',
      changPassVisible: false,
      passSendStatus: false,
      errorStatus: false
    }
  },
  methods: {
    save: function () {
        // Добавляем id в report

        var user = {
          id: this.profileId,
          name: this.profile,
          role: this.role,
          password: this.password,
        };

        if (!(this.password === '')) {

          // если есть id в data, тогда обноволяем информацию
          this.axios.post(url + port + 'api/user', user).then(res => {

            this.errorStatus = false
            this.passSendStatusFunc()


            // Очищаем поля
            this.id = '';
            this.password = '';
          })
        } else {
          this.passSendStatusFunc()
          this.errorStatus = true

        }

    },
    clicked: function (role) {
      this.role = role
    },
    change: function () {
      if (this.changPassVisible) {this.changPassVisible = false} else {this.changPassVisible = true}
    },
    passSendStatusFunc: function () {
      this.passSendStatus = true
      this.changPassVisible = !this.changPassVisible
    }
  },


// функция следит на изменениями переменной
  watch: {
    userAttr: function (newVal, oldVal) {
      this.id = newVal.id;
      this.name = newVal.name;
      this.role = newVal.role;
      this.password = newVal.password;
    }
  }
}
</script>

<style>

</style>

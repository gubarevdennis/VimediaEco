<template>
  <v-container align="start">
    <v-text-field
        placeholder="Введите имя..."
        variant="solo-filled"
        v-model="name">
    </v-text-field>

    <v-text-field
        placeholder="Введите стоимость часа в рублях..."
        variant="solo-filled"
        v-model="salary">
    </v-text-field>

    <v-text-field
        placeholder="Введите пароль..."
        variant="solo-filled"
        v-model="password">
    </v-text-field>

    <v-select
        variant="outlined"
        @update:modelValue="clicked"
        label="Укажите роль сотрудника..."
        :items="['Монтажник','Программист','Прораб','Сборщик','Бухгалтер',
        'Директор','Менеджер', 'Кладовщик', 'Закупщик','HR','Проектировщик',
        'Диспетчер','Руководитель сборочного цеха','Руководитель проектного отдела',
        'Руководитель отдела ведения проектов', 'Руководитель проектов']"
        :item-value="role"
    ></v-select>


    <!--// v-model для того чтобы с input пробросить в data в поле text -->
    <v-btn @click="save">
      Сохранить
    </v-btn>

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
  props: ['users', 'userAttr'], // чтобы рабоать с данной переменной и передавать ее выше в корень
  // функция нужна для того чтобы у каждого компонента было свое уникальное хранилище
  data() {
    return {
      id: '',
      name: '',
      role: '',
      password: '',
      salary: ''
    }
  },
  methods: {
    save: function () {

      var user = {
        name: this.name,
        role: this.role,
        salary: this.salary,
        password: this.password,
      };

      if (this.id) {
        // Добавляем id в report
        user = {
          id: this.id,
          name: this.name,
          role: this.role,
          salary: this.salary,
          password: this.password,
        };

      if ((this.password === undefined) && (this.role)) {
        // Добавляем id в report
        user = {
          id: this.id,
          name: this.name,
          salary: this.salary,
        };
      }

      if (this.password === undefined) {
        // Добавляем id в report
        user = {
          id: this.id,
          name: this.name,
          role: this.role,
          salary: this.salary,
        };
      }

      console.log(user)

      // если есть id в data, тогда обноволяем информацию
      this.axios.post('api/user/' + this.id, user).then(res => {
        let index = getIndex(this.users, res.data.id) // получеам индекс коллекции

        this.users.splice(index, 1, res.data);

        // Очищаем поля
        this.id = '';
        this.name ='';
        this.salary = '';
        this.password = '';
      })

    } else {
      // если нет id создаем новую позицию
      this.axios.post('api/user', user).then(data => {
        this.users.push(data.data)

        // Очищаем поля
        this.id = '';
        this.name ='';
        this.salary = '';
        this.password = '';
      })
    }
  },
  clicked: function (role) {
    this.role = role
  }
},


// функция следит на изменениями переменной
watch: {
  userAttr: function (newVal, oldVal) {
    this.id = newVal.id;
    this.name = newVal.name;
    this.role = newVal.role;
    this.salary = newVal.salary;
    this.password = newVal.password;
  }
}
}
</script>

<style>

</style>

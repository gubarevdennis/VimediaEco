<template>
  <v-app >
    <v-navigation-drawer
        v-model="drawer"
        temporary
        color="#F9F9F9"
    >
      <v-list-item
          align="center"
          v-bind:title="profile"
      ></v-list-item>

      <v-divider></v-divider>

      <v-list density="compact" nav>
        <v-btn block to="/cabinet" @click="changeAppBoardNameCabinet">Мой кабинет</v-btn>
        <v-btn block to="/reports" @click="changeAppBoardNameReports">Отправить отчет</v-btn>
        <v-btn block to="/byUser" @click="changeAppBoardNameByUser">Календарь</v-btn>
        <v-btn block to="/mainReportForEach" @click="changeAppBoardNameByEachUser">Мои отчеты</v-btn>
        <v-btn block v-if=" this.role.split(' ')[0] === 'Руководитель' /*|| this.role === 'Прораб'*/" to="/mainReportForDepDirectors" @click="changeAppBoardNameByUser">Отчет по отделу</v-btn>
        <v-btn block v-if=" this.role === 'Директор' || this.role === 'Диспетчер'" to="/mainReport" @click="changeAppBoardNameByUser">Все отчеты</v-btn>
        <v-btn block v-if="this.role === 'Директор'" to="/editReportsCost" @click="editReportsCost">Стоимость отчетов</v-btn>
        <v-btn block to="/facilities" @click="changeAppBoardNameFacilities">Объекты</v-btn>
        <v-btn block to="/mainTableTools" @click="changeAppBoardTools">Мои инструменты</v-btn>
        <v-btn block v-if=" this.role === 'Директор' || this.role === 'Кладовщик'" to="/mainTableToolsAll" @click="changeAppBoardTools">Все инструменты</v-btn>
        <v-btn block v-if="this.role === 'admin' || this.role === 'Директор' || this.role === 'HR'" to="/users" @click="changeAppBoardNameUsers">Мои коллеги</v-btn>
        <v-btn block to="/bonus" @click="changeAppBoardNameByMyBonus">Мои бонусы</v-btn>
        <v-btn block v-if="this.role === 'Директор'" to="/bonusByUser" @click="changeAppBoardNameBonusByUser">Бонусы по сотруднику</v-btn>
        <v-btn block v-if=" this.role.split(' ')[0] === 'Руководитель' /*|| this.role === 'Прораб'*/" to="/bonusByMyObjects" @click="changeAppBoardNameByJobs">Бонусы сотрудникам</v-btn>
        <v-btn block v-if=" this.role === 'Директор' || this.role === 'Диспетчер'
        || this.role.split(' ')[0] === 'Руководитель' /*|| this.role === 'Прораб'*/" to="/jobs" @click="changeAppBoardNameByJobs">Все бонусы</v-btn>
        <v-btn block to="/events" @click="changeAppEvents">События</v-btn>
      </v-list>

      <v-list-item
          align="center"
          style="color: green"
          v-bind:title="role"
      ></v-list-item>
    </v-navigation-drawer>


    <v-app-bar class="d-print-none"
               color="#0B0B0B"
               prominent
    >
      <v-app-bar-nav-icon style="color: #F9F9F9" variant="text" @click.stop="drawer = !drawer"></v-app-bar-nav-icon>

      <v-toolbar-title style="color: #F9F9F9">{{appBoardName}}</v-toolbar-title>

      <v-btn style="color: #F9F9F9" href="http://localhost:9000/logout" variant="text" icon="mdi-logout"></v-btn>
    </v-app-bar>

    <v-main style="background: #0B0B0B">
      <router-view :profile="profile" :role="role" :profileId="profileId" :reports="reports" :facilities="facilities" :users="users" ></router-view>
    </v-main>
  </v-app>
</template>

<script>

export default {
  data() {
    return {
      appBoardName: 'Vimedia',
      facilities: [],
      users: [],
      reports: [],
      drawer: false,
      group: null,
      profile: frontendData.name,
      role: frontendData.role[0].authority,
      profileId: frontendData.id,
    }
  },
  watch: {
    group () {
      this.drawer = false
    }
  },
  methods: {
    changeAppBoardNameReports() {
      this.appBoardName="Отправить отчет"
    },
    changeAppBoardNameFacilities() {
      this.appBoardName="Мои объекты"
    },
    changeAppBoardNameUsers() {
      this.appBoardName="Мои сотрудники"
    },
    changeAppBoardNameBonusByUser() {
      this.appBoardName="Бонусы по сотруднику"
    },
    changeAppBoardNameByUser() {
      this.appBoardName="Сервис учета рабочего времени Vimedia"
    },
    changeAppBoardNameCabinet() {
      this.appBoardName="Личный кабинет Vimedia"
    },
    changeAppBoardTools() {
      this.appBoardName="Инструменты"
    },
    changeAppEvents() {
      this.appBoardName="События"
    },
    changeAppBoardNameByEachUser() {
      this.appBoardName="Все отчеты"
    },
    changeAppBoardNameByJobs() {
      this.appBoardName="Работы"
    },
    changeAppBoardNameByMyBonus() {
      this.appBoardName="Мои бонусы"
    },
    editReportsCost() {
      this.appBoardName="Редактировать стоимость отчетов"
    }

  },
  created() {
    this.$watch(
        () => this.$route.params,
        (toParams, previousParams) => {
          switch (this.$route.path) {
            case "/reports" :
              this.changeAppBoardNameReports()
              break;
            case "/facilities" :
              this.changeAppBoardNameFacilities()
              break;
            case "/users" :
              this.changeAppBoardNameUsers()
              break;
            default : {}
          }
        }
    )
  }
}
</script>

<style>

</style>

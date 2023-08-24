<template>
  <v-app>
    <v-navigation-drawer
        v-model="drawer"
        temporary
    >
      <v-list-item
          align="center"
          v-bind:title="profile"
      ></v-list-item>


      <v-divider></v-divider>

      <v-list density="compact" nav>
        <v-btn block to="/reports" @click="changeAppBoardNameReports">Мои отчеты</v-btn>
        <v-btn block to="/facilities" @click="changeAppBoardNameFacilities">Мои объекты</v-btn>
        <v-btn block v-if="this.role === 'admin' || this.role === 'Директор' || this.role === 'HR'" to="/users" @click="changeAppBoardNameUsers">Мои коллеги</v-btn>
        <v-btn block to="/byUser" @click="changeAppBoardNameByUser">Календарь</v-btn>
      </v-list>

      <v-list-item
          align="center"
          style="color: green"
          v-bind:title="role"
      ></v-list-item>
    </v-navigation-drawer>


    <v-app-bar
        color="black"
        prominent
    >
      <v-app-bar-nav-icon variant="text" @click.stop="drawer = !drawer"></v-app-bar-nav-icon>

      <v-toolbar-title>{{appBoardName}}</v-toolbar-title>

      <v-btn href="http://192.168.100.100:6552/logout" variant="text" icon="mdi-logout"></v-btn>

    </v-app-bar>
    <v-main>
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
      this.appBoardName="Мои отчеты"
    },
    changeAppBoardNameFacilities() {
      this.appBoardName="Мои объекты"
    },
    changeAppBoardNameUsers() {
      this.appBoardName="Мои сотрудники"
    },
    changeAppBoardNameByUser() {
      this.appBoardName="Сервис учета рабочего времени Vimedia"
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

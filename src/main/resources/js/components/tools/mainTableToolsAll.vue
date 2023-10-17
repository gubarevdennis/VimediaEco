<template>
  <add-tool v-if="((this.role === 'Кладовщик') || (this.role === 'Директор'))" :addTool="addTool" :toolSets="toolSets" :editTool="editTool" :facilities="facilities" ></add-tool>

  <br>
  <div v-for="tool in sortedTools" v-bind:key="tool.id" align="center"  height="200px">
    <main-table-tools-row :deleteTool="deleteTool"
                          :users="users"
                          :userNames="userNames"
                          :role="role"
                          :profile="profile"
                          :profileId="profileId"
                          :toolSets="toolSets"
                          :editTool="editTool"
                          :tool="tool"></main-table-tools-row>
    <br>
  </div>
</template>

<script>
import MainTableToolsRow from "./mainTableToolsRow.vue";
import AddTool from "./addTool.vue";
import ConfirmToolTable from "./confirmToolTable.vue";
import { isProxy, toRaw } from 'vue'
export default {
  components: {AddTool, MainTableToolsRow, ConfirmToolTable},
  props: ['profile', 'role', 'profileId'],
  data() {
    return {
      tools: [],
      toolSets: [],
      facilities: [],
      sortedTools: [],
      users: [],
      userNames: [],
      sortedUsers: []
    }
  },
  mounted: function() {
    console.log('Запустил mounted')

    // Запрашиваем отчеты
      this.axios.get( "api/tool").then(tools => {
            tools.data.forEach(t =>
                this.tools.push(t))
            this.sortedTools = this.sortToolFunc(this.tools)
          }
      )

    // Запрашиваем пользователей
    this.axios.get( "api/user").then(tools => {
          tools.data.forEach(t => {
            this.users.push(t)
            this.userNames.push(t.name)
          })
          this.sortedUsers = this.sortUserFunc(this.users)
        }
    )

    // Запрашиваем отчеты
    this.axios.get( "api/facility").then(res => {
          res.data.forEach(f => {
                this.facilities.push(f)
              }
          )
        }

    )

    // Запрашиваем комплекты
    this.axios.get( "api/toolset").then(res => {
          res.data.forEach(t =>
              this.toolSets.push(t))
        }
    )
  },
  methods: {
    editTool: function (tool) {
      // Редактируем инструмент в основных данных
      this.tools.splice(
          this.tools.findIndex(t => t.id === tool.id),
          1,
          tool
      )
      this.sortedTools = this.sortToolFunc(this.tools)
    },
    addTool: function (tool) {
      // Редактируем инструмент в основных данных
      this.tools.push(tool)
      this.sortedTools = this.sortToolFunc(this.tools)

    },
    sortToolFunc: function (tools) {
      return tools.sort((a, b) => {
        if (a.id > b.id) {
          return -1;
        }
        if (a.id < b.id) {
          return 1;
        }
        return 0;
      })
    },
    sortUserFunc: function (users) {
      return users.sort((a, b) => {
        if (a.name < b.name) {
          return -1;
        }
        if (a.name > b.name) {
          return 1;
        }
        return 0;
      })
    },
    deleteTool: function (tool) {
      this.tools.splice(this.tools.indexOf(tool), 1) // удаления объекта из коллекции
      this.sortedTools = this.sortToolFunc(this.tools)
    }
  }}
</script>

<style scoped>

</style>

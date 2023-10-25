<template>

  <add-tool v-if="((this.role === 'Кладовщик') || (this.role === 'Директор'))" :addTool="addTool" :toolSets="toolSets" :editTool="editTool" :facilities="facilities" ></add-tool>

  <br>

  <div v-for="row in rows" v-bind:key="row" align="start" height="200px">
    <v-overlay

        v-model="overlay"
        class="align-center justify-center"
        scroll-strategy="reposition"
        align="center"
    >

      <v-btn icon="mdi-close" @click="overlay = !overlay"></v-btn>
      <br>
      <br>
      <decription-tool
          :closeDescriptionToolByDeleteConfirm="closeDescriptionToolByDeleteConfirm"
          :deleteTool="deleteTool"
          :toolSets="toolSets"
          :editTool="editTool"
          :profile="profile"
          :role="role"
          :tool="tool"
      ></decription-tool>
    </v-overlay>

    <v-overlay

        v-model="overlayToGiving"
        class="align-center justify-center"
        scroll-strategy="reposition"
        align="center"
    >
      <v-btn icon="mdi-close" @click="overlayToGiving = !overlayToGiving"></v-btn>
      <br>
      <br>
      <giving-tool :closeDescriptionToolByDeleteConfirm="closeDescriptionToolByDeleteConfirm"
                   :deleteTool="deleteTool"
                   :facilityNames="facilityNames"
                   :facilities="facilities"
                   :toolSets="toolSets"
                   :editTool="editTool"
                   :profile="profile"
                   :role="role"
                   :userNames="userNames"
                   :users="users"
                   :tool="tool"
      ></giving-tool>

    </v-overlay>
    <main-table-tools-row :deleteTool="deleteTool"
                          :facilityNames="facilityNames"
                          :users="users"
                          :userNames="userNames"
                          :role="role"
                          :profile="profile"
                          :profileId="profileId"
                          :toolSets="toolSets"
                          :editTool="editTool"
                          :toolsForRow="toolsForRowFunc(row)"
                          :row="row"
                          :overlayFunc="overlayFunc"
                          :overlayToGivingFunc="overlayToGivingFunc"
                          :toolFunc="toolFunc"
                          :facilities="facilities"
    ></main-table-tools-row>
    <br>
  </div>
</template>

<script>
import MainTableToolsRow from "./mainTableToolsRow.vue";
import AddTool from "./addTool.vue";
import ConfirmToolTable from "./confirmToolTable.vue";
import { isProxy, toRaw } from 'vue'
import GivingTool from "./givingTool.vue";
import DecriptionTool from "./decriptionTool.vue";

export default {
  components: {AddTool, MainTableToolsRow, ConfirmToolTable, GivingTool, DecriptionTool},
  props: ['profile', 'role', 'profileId'],
  data() {
    return {
      tools: [],
      toolSets: [],
      facilities: [],
      sortedTools: [],
      users: [],
      tool: '',
      userNames: [],
      sortedUsers: [],
      toolsForRow: [],
      facilityNames: [],
      row: 1,
      rows: 1,
      overlay: false,
      overlayToGiving: false,
    }
  },
  mounted: function() {
    console.log('Запустил mounted')

    var toolCount = 0;
    var rowCount = 1;

    // Запрашиваем отчеты
    this.axios.get( "api/tool").then(tools => {
          tools.data.forEach(t =>{
            this.tools.push(t)
            if (toolCount <= 6) {
              this.toolsForRow.push({
                row: rowCount,
                tool: t,
              })
              toolCount++
              if (toolCount >= 6) {
                toolCount = 0
                rowCount++
              }
              console.log(this.toolsForRow)
            }
          })
          this.sortedTools = this.sortToolFunc(this.tools)
          this.rows =  Math.ceil(this.tools.length/6);
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

    // Запрашиваем комплекты
    this.axios.get( "api/toolset").then(res => {
          res.data.forEach(t =>
              this.toolSets.push(t))
        }
    )

    // Запрашиваем отчеты
    this.axios.get( "api/facility").then(res => {
          res.data.forEach(f => {
                this.facilities.push(f)
              }
          )
          this.facilities.forEach( f => this.facilityNames.push(f.name))
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
    toolsForRowFunc: function (row) {
      var toolsWithOutRow = []
      var toolsWithRow = this.toolsForRow.filter(t => t.row === row)
      toolsWithRow.forEach(t => toolsWithOutRow.push(t.tool))
      // console.log(toolsWithOutRow)
      return toolsWithOutRow
    },
    deleteTool: function (tool) {
      this.tools.splice(this.tools.indexOf(tool), 1) // удаления объекта из коллекции
      this.sortedTools = this.sortToolFunc(this.tools)
    },
    closeDescriptionToolByDeleteConfirm: function () {
      this.overlay = false;
    },
    overlayFunc: function (overlay) {
      this.overlay = overlay;
    },
    overlayToGivingFunc: function (overlayToGiving) {
      this.overlayToGiving = overlayToGiving;
    },
    toolFunc: function (tool) {
      this.tool = tool;
      // console.log("tool")
      // console.log(tool)
    }
  }}
</script>

<style scoped>

</style>

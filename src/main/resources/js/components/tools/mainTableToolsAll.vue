<template>
    <add-tool  v-if="((this.role === 'Кладовщик') || (this.role === 'Директор'))"
               :addTool="addTool"
               v-bind:toolSets="toolSets"
               :editTool="editTool"
               :facilities="facilities"
               v-bind:categories="categories"
               :editCategory="editCategory"
               :addCategory="addCategory"
               :editToolSet="editToolSet"
               :addToolSet="addToolSet"
               :deleteToolSet="deleteToolSet"
               :deleteCategory="deleteCategory"
    ></add-tool>

    <filter-tool
        :profile="profile"
        :role="role"
        :toolsFromUnder="tools"
        :categories="categories"
        :categoryNames="categoryNames"
        :facilityNames="facilityNames"
        :facilities="facilities"
        :toolNames="toolNames"
        :userNames="userNames"
        :toolArticles="toolArticles"
        :toolSets="toolSets"
        :filterToolFunc="filterToolFunc"
        :loadBlockFunc="loadBlockFunc"
        :filterRequestInfo="filterRequestInfo"
        :filterRequestInfoFunc="filterRequestInfoFunc"
    >
    </filter-tool>

    <v-overlay
        :model-value="overlay"
        class="align-center justify-center"
        align="center"
        :opacity="1"
        :z-index="10"
    >
      <v-btn icon="mdi-close" @click="overlay = false" :ripple="false"></v-btn>
      <br>
      <br>
      <decription-tool
          :closeDescriptionToolByDeleteConfirm="closeDescriptionToolByDeleteConfirm"
          :closeDescriptionToolByCopyConfirm="closeDescriptionToolByCopyConfirm"
          :deleteTool="deleteTool"
          :toolSets="toolSets"
          :categories="categories"
          :editTool="editTool"
          :profile="profile"
          :role="role"
          :tool="tool"
      ></decription-tool>
    </v-overlay>

    <v-overlay
        :model-value="overlayToGiving"
        class="align-center justify-center"
        align="center"
        :opacity="1"
        :z-index="10"
    >
      <v-btn icon="mdi-close" @click="overlayToGiving = false" :ripple="false"></v-btn>
      <br>
      <br>
      <giving-tool :closeDescriptionToolByDeleteConfirm="closeDescriptionToolByDeleteConfirm"
                   :deleteTool="deleteTool"
                   :facilityNames="facilityNames"
                   :facilities="facilities"
                   :toolSets="toolSets"
                   :categories="categories"
                   :editTool="editTool"
                   :profile="profile"
                   :role="role"
                   :userNames="userNames"
                   :users="users"
                   :tool="tool"
      ></giving-tool>
    </v-overlay>
    <keep-alive>
    <v-infinite-scroll  :onLoad="load" >
      <div v-for="row in rows" :key="row" align="start" height="200px">
        <main-table-tools-row :deleteTool="deleteTool"
                              :facilityNames="facilityNames"
                              :users="users"
                              :userNames="userNames"
                              :role="role"
                              :profile="profile"
                              :profileId="profileId"
                              :toolSets="toolSets"
                              :categories="categories"
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

      <template v-slot:load-more="{ props }">
        <v-btn
            icon="mdi-refresh"
            variant="text"
            size="small"
            v-bind="props"
        ></v-btn>
      </template>
    </v-infinite-scroll>
    </keep-alive>
</template>

<script>
import MainTableToolsRow from "./mainTableToolsRow.vue";
import AddTool from "./addTool.vue";
import AddCategory from "./addCategory.vue";
import ConfirmToolTable from "./confirmToolTable.vue";
import { isProxy, toRaw } from 'vue'
import GivingTool from "./givingTool.vue";
import DecriptionTool from "./decriptionTool.vue";
import FilterTool from "./filterTool.vue"

export default {
  components: {AddTool, AddCategory, MainTableToolsRow, ConfirmToolTable, GivingTool, DecriptionTool, FilterTool},
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
      toolNames: [],
      toolArticles: [],
      row: 1,
      rows: 1,
      overlay: false,
      overlayToGiving: false,
      countLoadingPages: 0,
      rowCount: 1,
      filterRequestInfo: '',
      categories: [],
      categoryNames:[]
    }
  },
  mounted: function() {
    console.log('Запустил mounted')

    // Запрашиваем отчеты для информации в поле поиска
    this.axios.get( "api/tool" + '?offset=' + this.countLoadingPages + '&limit=' + 10000).then(tools => {
          tools.data.forEach(t =>{
            this.toolNames.push(t.name.replace(/^\s\s*/, '').replace(/\s\s*$/, ''))
            this.toolArticles.push(t.article)
          })

          this.rows =  Math.ceil(this.tools.length/6);

          this.toolNames.unshift('Все инструменты')
          this.toolArticles.unshift('Все артикулы')

          this.toolNames = removeDuplicates(this.toolNames) // убираем дубликаты
          this.toolArticles = removeDuplicates(this.toolArticles.filter( a => a != null)) // убираем дубликаты

          console.log("this.toolArticles")
          console.log(this.toolArticles)

        }
    )

    // Запрашиваем отчеты для отображения на странице
    var toolCount = 0;
    this.rowCount = 1;

    this.axios.get( "api/tool" + '?offset=' + this.countLoadingPages + '&limit=' + 30).then(tools => {
          tools.data.forEach(t =>{
            this.tools.push(t)

          })

          // Получаем сортированный по объектам список и сортируем по сотруднику
          this.sortedTools =
              this
                  .sortToolFunc(this.tools)
                  .forEach(t => {
                    if (toolCount <= 6) {
                      this.toolsForRow.push({
                        row: this.rowCount,
                        tool: t,
                      })
                      toolCount++
                      if (toolCount >= 6) {
                        toolCount = 0
                        this.rowCount++
                      }
                    }
                  })

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
          this.userNames.unshift('Все сотрудники')
        }
    )

    // Запрашиваем комплекты
    this.axios.get( "api/toolset").then(res => {
          res.data.forEach(t =>
              this.toolSets.push(t))
        }
    )

    // Запрашиваем категории
    this.axios.get( "api/category").then(res => {
          res.data.forEach(t =>
              this.categories.push(t))
      this.categories.forEach( c => this.categoryNames.push(c.name))
      this.categoryNames.unshift('Все категории')
        }
    )

    // Запрашиваем отчеты
    this.axios.get( "api/facility").then(res => {
          res.data.forEach(f => {
                this.facilities.push(f)
              }
          )

          this.facilities.forEach( f => this.facilityNames.push(f.name))
          this.facilityNames.unshift('Все объекты')
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
    editCategory: function (category) {
      // Редактируем категорию инструментов
      this.categories.splice(
          this.categories.findIndex(c => c.id === category.id),
          1,
          category
      )
    },
    addCategory: function (category) {
      // Добавляем категорию инструментов
      this.categories.push(category)
    },
    editToolSet: function (toolSet) {
      // Редактируем комплекты
      this.toolSets.splice(
          this.toolSets.findIndex(ts => ts.id === toolSet.id),
          1,
          toolSet
      )
    },
    addToolSet: function (toolSet) {

      // Добавляем комплекты
      this.toolSets.push(toolSet)

      console.log(this.toolSets)
    },
    // сортирует сначала по объектам затем по коллегам
    sortToolFunc: function (tools) {
      return tools
          .sort((a, b) => {
        if (a.facility) {
          if (b.facility) {

            if (((a.facility ? a.facility.name : '') < (b.facility ? b.facility.name : ''))) {
              return -1;
            }
            if ((a.facility ? a.facility.name : '') > (b.facility ? b.facility.name : '')) {
              return 1;
            }
            if (a.facility.name === b.facility.name) {
              if (((a.user ? a.user.name : '') < (b.user ? b.user.name : ''))) {
                return -1;
              }
              if ((a.user ? a.user.name : '') > (b.user ? b.user.name : '')) {
                return 1;
              }
            }
            return 0;
          }
        }
      })
    },
    // сортирует сначала по объектам затем по коллегам
    sortToolSetFunc: function (toolSets) {
      return toolSets
          .sort((a, b) => {
            if (a.name) {
              if (b.name) {
                if ((a.name) < (b.name)) {
                  return -1;
                }
                if ((a.name) > (b.name)) {
                  return 1;
                }
              }
            }
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

      console.log("this.tool -> ")
      console.log(this.tools)
      console.log("delete tool index -> " + this.tools.findIndex(t => t.id === tool.id))

      this.tools.splice(this.tools.findIndex(t => t.id === tool.id), 1) // удаления объекта из коллекции
      this.sortedTools = this.sortToolFunc(this.tools)
    },
    deleteToolSet: function (toolSet) {

      console.log(this.toolSets)
      console.log("this.toolSets id -> ")
      this.toolSets.forEach(t => console.log(t.name + toolSet.name));
      console.log("delete toolSet index -> " + this.toolSets.findIndex(t => t.name === toolSet.name))

      this.toolSets.splice(this.toolSets.findIndex(t => t.name === toolSet.name), 1) // удаления объекта из коллекции

      this.toolSets = this.sortToolSetFunc(this.toolSets)

      console.log(this.toolSets)
    },
    deleteCategory: function (category) {

      console.log("this.categories -> ")
      console.log(this.categories)
      console.log("delete categories index -> " + this.categories.findIndex(t => t.name === category.name))

      this.categories.splice(this.categories.findIndex(t => t.name === category.name), 1) // удаления объекта из коллекции
    },
    copyTool: function (tool) {
      this.tools.add(tool) // копирование объекта в коллекцию
      this.sortedTools = this.sortToolFunc(this.tools)
    },
    closeDescriptionToolByDeleteConfirm: function () {
      this.overlay = false;
    },
    closeDescriptionToolByCopyConfirm: function () {
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
    },
    // Блокровка загрузки страниц в режиме посике
    loadBlockFunc: function (state) {
       // this.loadBlock = state;

      // console.log("tool")
      // console.log(tool)
    },
    filterRequestInfoFunc: function (requestInfo) {
      if (requestInfo !== '') {
          this.filterRequestInfo = requestInfo;
      }
    },
    load({ done }) {
      if (!this.loadBlock) {
        this.countLoadingPages++

        var toolCount = 0;

        // Запрашиваем отчеты
        this.axios.get("api/tool" + '?offset=' + this.countLoadingPages + '&limit=' + 30 + this.filterRequestInfo).then(tools => {
              tools.data.forEach(t => {
                this.tools.push(t)
                // this.toolNames.push(t.name)
                // this.toolArticles.push(t.article)
                if (toolCount <= 6) {
                  this.toolsForRow.push({
                    row: this.rowCount,
                    tool: t,
                  })
                  toolCount++
                  if (toolCount >= 6) {
                    toolCount = 0
                    this.rowCount++
                  }
                  // console.log(this.toolsForRow)
                  done('ok')
                }
              })
              this.sortedTools = this.sortToolFunc(this.tools)
              this.rows = Math.ceil(this.tools.length / 6);

              //
              // this.toolNames = removeDuplicates(this.toolNames) // убираем дубликаты
              // this.toolArticles = removeDuplicates(this.toolArticles.filter( a => a != null)) // убираем дубликаты

            }
        )
      } else {
        done('ok')
      }
    },
    filterToolFunc: function (tools) {
      this.sortedTools = tools;
      this.toolsForRow = []

      console.log('Запустил filterToolFunc')

      var toolCount = 0;
      var rowCount = 1;

      if (this.sortedTools)
        this.sortedTools.forEach(t =>{
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
            // console.log(this.toolsForRow)
          }
        })
    }
  }}




// Функция удаления дубликатов
function removeDuplicates(arr) {

  const result = [];
  const duplicatesIndices = [];

  // Перебираем каждый элемент в исходном массиве
  arr.forEach((current, index) => {

    if (duplicatesIndices.includes(index)) return;

    result.push(current);

    // Сравниваем каждый элемент в массиве после текущего
    for (let comparisonIndex = index + 1; comparisonIndex < arr.length; comparisonIndex++) {

      const comparison = arr[comparisonIndex];
      const currentKeys = Object.keys(current);
      const comparisonKeys = Object.keys(comparison);

      // Проверяем длину массивов
      if (currentKeys.length !== comparisonKeys.length) continue;

      // Проверяем значение ключей
      const currentKeysString = currentKeys.sort().join("1").toLowerCase();
      const comparisonKeysString = comparisonKeys.sort().join("1").toLowerCase();
      if (currentKeysString !== comparisonKeysString) continue;

      // Проверяем индексы ключей
      let valuesEqual = true;
      for (let i = 0; i < currentKeys.length; i++) {
        const key = currentKeys[i];
        if ( current[key] !== comparison[key] ) {
          valuesEqual = false;
          break;
        }
      }
      if (valuesEqual) duplicatesIndices.push(comparisonIndex);

    } // Конец цикла
  });
  return result;
}


</script>

<style scoped>

</style>

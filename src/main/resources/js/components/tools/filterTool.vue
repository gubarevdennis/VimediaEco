<template>
  <v-sheet color="black">
    <v-row align="stretch" justify="center">
      <v-col>
        <br>
        <v-autocomplete
            variant="outlined"
            @update:modelValue="selectTool"
            :item-value="toolNameSelected"
            label="Название"
            :items="toolNames"
        >
        </v-autocomplete>
      </v-col>
      <v-col>
        <br>
        <v-autocomplete
            variant="outlined"
            @update:modelValue="selectArticle"
            :item-value="toolArticleSelected"
            label="Артикул"
            :items="toolArticles"
        >
        </v-autocomplete>
      </v-col>
      <v-col>
        <br>
        <v-autocomplete
            variant="outlined"
            @update:modelValue="selectCategory"
            :item-value="toolCategorySelected"
            label="Категория"
            :items="toolCategories"
        >
        </v-autocomplete>
      </v-col>
      <v-col>
        <br>
        <v-autocomplete
            label="Объект"
            variant="outlined"
            @update:modelValue="selectFacility"
            :items="facilityNames"
            :item-value="facilityNameSelected"
        >
        </v-autocomplete>
      </v-col>
      <v-col v-if="subFacilities ? subFacilities[1] : ''">
        <br>
        <v-autocomplete
            label="Подобъект"
            variant="outlined"
            @update:modelValue="selectSubFacility"
            :items="subFacilities"
            :item-value="subFacilityNameSelected"
        >

        </v-autocomplete>
      </v-col>
      <v-col>
        <br>
        <v-autocomplete
            variant="outlined"
            @update:modelValue="selectUser"
            :item-value="userNameSelected"
            label="Сотрудник"
            :items="userNames"
        >
        </v-autocomplete>
      </v-col>
    </v-row>
  </v-sheet>
</template>

<script>
export default {
  name: "filterTool",
  props: ['profile', 'role', 'facilities', 'facilityNames', 'userNames' , 'toolNames' ,
    'filterToolFunc', 'toolArticles', 'toolsFromUnder', 'loadBlockFunc'],
  data() {
    return {
      facilityNameSelected: '',
      subFacilityNameSelected: '',
      toolNameSelected: '',
      toolCategorySelected: '',
      byFacility: false,
      bySubFacility: false,
      byTool: false,
      byCategory: false,
      sortedToolsByFacility: [],
      sortedToolsBySubFacility: [],
      sortedToolsByCategory: [],
      sortedToolsByName: [],
      sortedTools: [],
      userNameSelected: '',
      byUser: false,
      sortedToolsByUser: [],
      subFacilities: [],
      toolArticleSelected: '',
      sortedToolsByArticle: [],
      tools: [],
      toolsFirst: [],
      paginationInfo: '',
      toolCategories: ['Все категории', 'Электроинструмент', 'Абразивный инструмент', 'Измерительный инструмент',
        'Слесарно-монтажный инструмент', 'Без категории'],

    }
  },
  mounted() {
    this.toolsFirst = this.toolsFromUnder
  },
  methods: {
    selectFacility: function (facilityNameSelected) {
      console.log('Запустил selectFacility')
      this.byFacility = true
      this.facilityNameSelected = facilityNameSelected

      this.resultFilter()
      this.selectVariantsSubFacilities()
    },
    sortByFacility: function (tools) {
      console.log('Запустил sortByFacility')

      // Если выбраны все объекты
      if (this.facilityNameSelected === 'Все объекты') {
        // Выключаем фильтр по подобъекта когда выбраны Все объекты
        this.bySubFacility = false

        return this.sortedToolsByFacility = tools
      }

      if (!(this.byArticle || this.byTool || this.byUser || this.byCategory) && this.byFacility) {
        this.tools = []
        this.axios.get("api/tool" + '?facilityname=' + this.facilityNameSelected).then(tools => {
          tools.data.forEach(t => {
            this.tools.push(t)
          })

          if (this.byFacility) {
            return this.sortedTools = this.tools
          }
        })
      } else if (this.byFacility) {
        return this.sortedToolsByFacility = tools.filter(t =>
            t.facility ? (t.facility.name === this.facilityNameSelected) : false)}

    },
    selectSubFacility: function (subFacilityNameSelected) {
      console.log('Запустил selectSubFacility')
      this.bySubFacility = true
      this.subFacilityNameSelected = subFacilityNameSelected

      this.resultFilter()
    },
    sortBySubFacility: function (tools) {
      console.log('Запустил sortBySubFacility')
      // Если выбраны все подобъекты
      if (this.bySubFacility && this.subFacilityNameSelected === 'Все подобъекты') {
        return this.sortedToolsBySubFacility = tools
            .filter(t => (t.facility ? (t.facility.name === this.facilityNameSelected) : false))}

      if (this.bySubFacility) {
        return this.sortedToolsBySubFacility = tools
            .filter(t => ((t.facility ? (t.facility.name === this.facilityNameSelected) : false)
                && ((t.subFacility ? t.subFacility.name : '') === this.subFacilityNameSelected)))
      }
    },
    selectCategory: function (toolCategorySelected) {
      console.log('Запустил selectCategory')
      this.byCategory = true
      this.toolCategorySelected = toolCategorySelected

      this.resultFilter()
    },
    sortByCategory: function (tools) {
      console.log('Запустил sortByCategory')

      // Если выбраны все работы
      if (this.toolCategorySelected === 'Все категории') {
        this.byUser = false;
        return this.sortedToolsByCategory = tools}

      if (!(this.byArticle || this.byTool || this.byFacility || this.byUser) && this.byCategory) {
        this.tools = []
        this.axios.get( "api/tool" + '?category=' + this.toolCategorySelected).then(tools => {
          tools.data.forEach(t => {
            this.tools.push(t)
          })

          if (this.byCategory) {
            return this.sortedTools = this.tools
          }
        })
      } else if (this.byCategory) {
        return this.sortedToolsByCategory = tools.filter(t => t.category ? (t.category === this.toolCategorySelected) : false)
      }

    },
    selectUser: function (userNameSelected) {
      console.log('Запустил selectUser')
      this.byUser = true
      this.userNameSelected = userNameSelected

      this.resultFilter()
    },
    sortByUser: function (tools) {
      console.log('Запустил sortByUser')
      // Если выбраны все работы
      if (this.userNameSelected === 'Все сотрудники') {
        this.byUser = false;
        return this.sortedToolsByUser = tools}

      if (!(this.byArticle || this.byTool || this.byFacility || this.byCategory) && this.byUser) {
        this.tools = []
        this.axios.get( "api/tool" + '?username=' + this.userNameSelected).then(tools => {
          tools.data.forEach(t => {
            this.tools.push(t)
          })

          if (this.byUser) {
            return this.sortedTools = this.tools
          }
        })
      } else if (this.byUser) {
        return this.sortedToolsByUser = tools.filter(t => t.user ? (t.user.name === this.userNameSelected) : false)
      }

    },
    selectTool: function (toolNameSelected) {
      console.log('Запустил selectWork')
      this.byTool = true
      this.toolNameSelected = toolNameSelected

      this.resultFilter()
    },
    sortByTool: function (tools) {
      console.log('Запустил sortByToolName')

      // Если выбраны все работы
      if (this.toolNameSelected === 'Все инструменты') {
        this.byTool = false
        return this.sortedToolsByName = tools
      }

      if (!(this.byArticle || this.byUser || this.byFacility || this.byCategory) && this.byTool) {
        this.tools = []
        this.axios.get( "api/tool" + '?name=' + this.toolNameSelected).then(tools => {
          tools.data.forEach(t => {
            this.tools.push(t)
          })

          if (this.byTool) {
            return this.sortedTools = this.tools
          }
        })
      } else if (this.byTool) {
        return this.sortedToolsByName = tools.filter(t => t.name === this.toolNameSelected)
      }

    },
    selectArticle: function (toolArticleSelected) {
      console.log('Запустил selectArticle')
      this.byArticle = true
      this.toolArticleSelected = toolArticleSelected

      this.resultFilter()

    },
    sortByArticle: function (tools) {
      console.log('Запустил sortByArticle')

      // Если выбраны все работы
      if (this.toolArticleSelected === 'Все артикулы') {
        this.byArticle = false
        return this.sortedToolsByArticle = tools
      }

      if (!(this.byTool || this.byUser || this.byFacility || this.byCategory) && this.byArticle) {
        this.tools = []
        this.axios.get("api/tool" + '?article=' + this.toolArticleSelected).then(tools => {
          tools.data.forEach(t => {
            this.tools.push(t)
          })

          if (this.byArticle) {
            return this.sortedTools = this.tools
          }
        })
      } else if (this.byArticle) {
        return this.sortedToolsByArticle = tools.filter(t => t.article === this.toolArticleSelected)
      }
    },
    resultFilter: function () {
      console.log('Запустил resultFilter')

      var sorted = this.toolsFromUnder

      this.loadBlockFunc(true)

      if (this.byFacility) {
        sorted = this.sortByFacility(sorted)
      }
      //
      if (this.byUser) {
        sorted = this.sortByUser(sorted)
      }
      //
      if (this.byCategory) {
        sorted = this.sortByCategory(sorted)
      }
      //
      if (this.byTool) {
        sorted = this.sortByTool(sorted)
      }
      //
      if (this.byArticle) {
        sorted = this.sortByArticle(sorted)
      }
      //
      if (this.byFacility && this.bySubFacility) {
        sorted = this.sortBySubFacility(sorted)
      }

      this.sortedTools = sorted

      if (!(this.byFacility) && !(this.byUser) && !(this.byTool) && !(this.byArticle) && !(this.byCategory))
        this.loadBlockFunc(false) // блокируем разворачивание страниц


    },
    selectVariantsSubFacilities: function () {
      console.log('Запустил selectVariantsSubFacilities')
      this.subFacilities = []
      if (this.facilities.find(f => f.name === this.facilityNameSelected))
        this.facilities.find(f => f.name === this.facilityNameSelected)
            .subFacilities.forEach(s => this.subFacilities.push(s.name))
      this.subFacilities.unshift('Все подобъекты')
    },
  },
// функция следит на изменениями переменной
  watch: {
    sortedTools: function (newVal, oldVal) {
      this.filterToolFunc(newVal)
    }
  }
}
</script>

<style scoped>

</style>

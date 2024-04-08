<template>
  <v-sheet>
    <v-table
        fixed-header
    >
      <thead>
      <tr>
        <th class="text-left">
          Id
        </th>
        <th class="text-left">
          Событие
        </th>
        <th class="text-left">
          Инструмент
        </th>
        <th class="text-left">
          Откуда
        </th>
        <th class="text-left">
          Куда
        </th>
        <th class="text-left">
          Кто
        </th>
        <th class="text-left">
          Кому
        </th>
        <th class="text-left">
          Дата и время
        </th>
      </tr>
      </thead>
      <tbody>
      <tr
          v-for="event in events"
          :key="event.id"
      >
        <td>{{ event.id }}</td>
        <td>{{ event.name }}</td>
        <td>{{ event.tool ? event.tool.name + " " +  event.tool.article : 'инструмент не выбран'}}</td>
        <td>{{ event.fromFacility }}</td>
        <td>{{ event.toFacility }}</td>
        <td>{{ event.user ? event.user.name : 'не назначен отправитель'}}</td>
        <td>{{ event.toUser ? event.toUser : 'не назначен получатель'}}</td>
        <td>{{ event.eventTimeAndDate }}</td>
      </tr>
      </tbody>
    </v-table>
  </v-sheet>
</template>

<script>

export default {

  props: ['profile', 'role'],
  data() {
    return {
      events: [],
      facilities: [],
      sortedEvents: []
    }
  },
  mounted: function() {
    console.log('Запустил mounted')

    if (this.role === 'Директор') {
      // Запрашиваем события
      this.axios.get("api/event").then(events => {
            events.data.forEach(t =>
                this.events.push(t))
            this.sortedEvents = this.sortToolFunc(this.events)
          }
      )
    } else {
      // Запрашиваем события
      this.axios.get(`api/event/user`).then(events => {
            events.data.forEach(t =>
                this.events.push(t))
            this.sortedEvents = this.sortToolFunc(this.events)
          }
      )
    }
    //
    // // Запрашиваем отчеты
    // this.axios.get( "api/facility").then(res => {
    //       res.data.forEach(f => {
    //             this.facilities.push(f)
    //           }
    //       )
    //     }
    //
    // )
    //
    // // Запрашиваем комплекты
    // this.axios.get( "api/toolset").then(res => {
    //       res.data.forEach(t =>
    //           this.toolSets.push(t))
    //     }
    // )
  },
  methods: {
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
  }
}
</script>

<style scoped>

</style>

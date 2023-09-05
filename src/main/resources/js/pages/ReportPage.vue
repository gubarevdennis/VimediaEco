<template>
<report-list :reports="reports" :reportAttr="report" :facilities="facilities" ></report-list>
</template>

<script>

import reportList from "../components/reports/reportList.vue";

export default {
  components: {
    reportList
  }
}
</script>

<style>

</style>

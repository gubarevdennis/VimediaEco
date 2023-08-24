import { createStore } from 'vuex'

// Create a new store instance.
export default createStore({
  state () {
    return {
      facilities: [],
      users: [],
      reports: [],
    }
  },
  getters: {
    sortedFacilities: state => this.facilities.sort((a,b) => -(a.id-b.id)) // сортировка по убыванию (каждый раз когда будет меняться facilities)

  },
  mutations: {
    addFacilityMutation(state, facility) {
      state.facilities = {
        ...state.facilities,
        facility
      }
    },
    updateFacilityMutation(state, facility) {
      const updateIndex = state.facilities.findIndex(item => item.id === facility.id)

      state.facilities = {
        ...state.facilities.slice(0,updateIndex),
        facility,
        ...state.facilities.slice(updateIndex+1),
      }
    },
    removeFacilityMutation(state, facility) {
      const deleteIndex = state.facilities.findIndex(item => item.id === facility.id)

      if (deleteIndex>-1) {
        state.facilities = {
          ...state.facilities.slice(0, deleteIndex),
          ...state.facilities.slice(deleteIndex + 1),
        }
      }
    }
  },
  actions: {
    async addFacilityAction({commit},facility){
          const res = await this.axios.post('http://localhost:9000/facility', facility)
          const index = this.facilities.findIndex(item => item.id === res.data.id)

      console.log(index)
      console.log(res.data)

      if (index > -1) {
            commit('updateFacilityMutation',res.data)
        } else {
            commit('addFacilityMutation',res.data)
      }
    },
    async updateFacilityAction({commit},facility){
      const res = await this.axios.post('http://localhost:9000/facility', facility)
      commit('updateFacilityMutation',res.data)
    },
    async removeFacilityAction({commit},facility){
      const res = await this.axios.delete(`http://localhost:9000/facility/${facility.id}`)

      if (result.status === 200) {
        commit('removeFacilityMutation',res.data)
      }
    },
  }
})



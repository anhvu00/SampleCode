import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useCounterStore = defineStore({
  id: 'counter',
  state: () => ({
    count: 20
  }),
  actions: {
   add() {
      this.count++
    },
    minus() {
      this.count--
    },
    greeting() {
      return ("HELLO FROM STORE")
    }
  },
  getters: {
    oddOrEven: (state) => {
      return ( (state.count % 2 === 0) ? 'even' : 'odd')
    }
  }

})

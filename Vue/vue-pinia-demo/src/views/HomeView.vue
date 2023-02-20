<template>
  <div class="home">
    <div class="count">{{counterStore.count}}</div>
    <div class="buttons">
      <button data-cy="minus-one" @click="counterStore.minus">-1</button>
      <button data-cy="add-one" @click="counterStore.add">+1</button>
    </div>
    <div class="compute">
      <p>The value is {{ counterStore.oddOrEven }}</p>
    </div>

  </div>
</template>

<script>
import { ref, computed } from "vue";
import { useCounterStore } from "@/stores/counter.js";

export default {
  name: "HomeView",

  setup() {
    // create a pinia store
    const counterStore = useCounterStore();
    return {
      counterStore,
    };
  }
};
</script>

<style scoped>
.count {
  font-size: 60px;
  margin: 20px;
}
.buttons button {
  font-size: 40px;
  margin: 10px;
}
</style>
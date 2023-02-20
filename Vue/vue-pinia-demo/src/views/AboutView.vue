<template>
  <div class="about">
    <h1>{{ counterStore.count }}</h1>
    <h2>{{ counterStore.oddOrEven }}</h2>
    <button data-cy="my-url" @click="handleClick">Get URL</button>
  </div>
</template>
<script setup>
import { ref, computed } from "vue";
import { useCounterStore } from "@/stores/counter.js";
import axios from "axios";

const counterStore = useCounterStore();
let mydata = {};

async function handleClick() {
  console.log("visit url");

  await axios
    .get("https://jsonplaceholder.typicode.com/posts/1")
    .then(response => {
      console.log(response.data);
      mydata = response.data;
      console.log(mydata.id)
    })
    .catch(error => {
      console.log(error);
    });
}
// if (window.Cypress) {
//   window.store = {counterStore}  // test can see window.store
// }
</script>
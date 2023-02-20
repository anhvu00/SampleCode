

<template>
  <q-page class="flex flex-center">
    <!-- A toggle button to test conditional rendering -->
    <q-card>
      <button @click="handleClick()">Toggle</button>
      <div v-if="flag1">flag is true</div>
      <div v-else>flag is false</div>
    </q-card>
  </q-page>
</template>

<script>
import { defineComponent } from "vue";
import { ref, computed } from "vue";

export default defineComponent({
  name: "IndexPage",

  setup() {
    const flag1 = ref(false);
    function handleClick() {
      flag1.value = !flag1.value;
      console.log(flag1.value);
      return flag1;
    }

    return {
      flag1,
      handleClick,
    };
  },
});
</script>

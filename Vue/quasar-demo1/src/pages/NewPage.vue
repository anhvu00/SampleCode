<template>
  <q-page class="flex flex-center">
    <p>Test 2 dialog buttons</p>
    <div class="q-pa-md q-gutter-sm">
      <q-btn label="MissingItem" color="primary" @click="confirm1"></q-btn>
      <q-btn label="Conflict" color="primary" @click="confirm2"></q-btn>
    </div>
  </q-page>
</template>

<script>
import { useQuasar } from "quasar";

export default {
  name: "NewPage",

  setup(context) {
    const $q = useQuasar();

    function confirm1() {
      $q.dialog({
        title: "Confirm1",
        message: "continue with missing item?",
        cancel: true,
        persistent: true,
      })
        .onOk(() => {
          console.log(">>>> OK missing item");
        })
        .onOk(() => {
          console.log(">>>> second OK catcher");
        })
        .onCancel(() => {
          console.log(">>>> Cancel missing item");
        })
        .onDismiss(() => {
          // console.log('I am triggered on both OK and Cancel')
        });
    }

    function confirm2() {
      $q.dialog({
        title: "Confirm2",
        message: "Continue with conflicts?",
        cancel: true,
        persistent: true,
      })
        .onOk(() => {
          console.log(">>>> OK with conflicts");
        })
        .onCancel(() => {
          console.log(">>>> Cancel conflicts");
        });
    }

    return {
      confirm1,
      confirm2,
    };
  },
};
</script>

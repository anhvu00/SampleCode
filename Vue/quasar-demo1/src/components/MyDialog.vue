<template>
  <q-dialog v-model="isActive">
    <div>
      <q-card>
        <h4>{{ message }}</h4>
        <q-card-actions align="right">
          <q-btn
            flat
            label="Cancel"
            color="primary"
            v-close-popup
            @click="onCancelClick"
          />
          <q-btn
            flat
            label="OK"
            color="primary"
            v-close-popup
            @click="onOkClick"
          />
        </q-card-actions>
      </q-card>
    </div>
  </q-dialog>
</template>

<script>
import { useQuasar } from "quasar";
import { ref } from "vue";

export default {
  name: "MyDialog",
  props: {
    message: String,
  },

  setup(props, context) {
    const $q = useQuasar();
    const isActive = ref(true);

    /*function confirm1() {
      $q.dialog({
        title: "Confirm1",
        message: "continue or not?",
        cancel: true,
        persistent: true,
      })
        .onOk(() => {
          console.log(">>>> OK confirm");
        })
        .onCancel(() => {
          console.log(">>>> reject");
        })
        .onDismiss(() => {
          // console.log('I am triggered on both OK and Cancel')
        });
    }*/

    function onOkClick() {
      console.log("user confirm/accept");
      context.emit("confirmOk");
    }

    function onCancelClick() {
      console.log("user cancel");
      context.emit("confirmCancel");
    }

    return {
      // confirm1,
      isActive,
      onOkClick,
      onCancelClick,
    };
  },
};
</script>

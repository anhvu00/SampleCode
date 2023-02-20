<template>
  <div class="q-pa-md col-lg-8">
    <!-- A form to test confirm popup -->
    <q-form @submit="onSubmit" class="q-gutter-md">
      <q-card>
        <!-- input 1 -->
        <div class="q-pa-md" style="max-width: 300px">
          <q-input
            filled
            v-model="model1"
            label="Enter code"
            bottom-slots
            hint="May cause missing data"
          ></q-input>
        </div>
        <!-- input 2 -->
        <div class="q-pa-md" style="max-width: 300px">
          <q-input
            filled
            v-model="model2"
            label="Enter name"
            bottom-slots
            hint="May cause conflict"
          ></q-input>
        </div>
        <div v-if="missing">
          <MyDialog
            message="missing code"
            @confirmOk="handleMissingAccept"
            @confirmCancel="handleMissingReject"
          />
        </div>
        <div v-if="conflict">
          <MyDialog
            message="conflicting data"
            @confirmOk="handleConflictAccept"
            @confirmCancel="handleConflictReject"
          />
        </div>
      </q-card>

      <div class="q-pt-sm">
        <div class="row">
          <div class="col">
            <q-btn
              label="Save form"
              type="submit"
              color="primary"
              data-cy="saveButton"
            >
            </q-btn>
          </div>
        </div>
      </div>
    </q-form>
  </div>
</template>

<script>
import { ref, computed } from "vue";
import { useQuasar } from "quasar";
import MyDialog from "../components/MyDialog.vue";

export default {
  name: "ConditionalDisplay",
  components: {
    MyDialog,
  },

  setup(props, context) {
    const $q = useQuasar();
    const missing = ref(false);
    const conflict = ref(false);
    const model1 = ref(""); // input 1
    const model2 = ref(""); // input 2

    // check input and popup confirm dialog
    function onSubmit() {
      missing.value = false;
      if (model1.value === "") {
        console.log("missing data");
        missing.value = true;
      } else {
        if (isConflict()) {
          console.log("no missing but conflict");
          conflict.value = true;
        }
      }
    }

    function isConflict() {
      return model2.value === "abc" ? true : false;
    }
    // user Accept the missing data, moving on...
    function handleMissingAccept() {
      // call the second popup by setting the condition
      conflict.value = isConflict();
      // if no conflict, save
      if (!conflict.value) {
        saveForm();
      }
      // reset the missing flag to popup again
      missing.value = false;
    }

    function handleMissingReject() {
      // reset the missing flag to popup again
      missing.value = false;
    }

    function handleConflictAccept() {
      saveForm();
    }

    function handleConflictReject() {
      // reset the missing flag to popup again
      conflict.value = false;
      missing.value = false;
      console.log("user cancel conflict");
    }

    function saveForm() {
      console.log("form submitted");
      $q.notify({
        type: "info",
        message: "submitted",
      });
    }

    return {
      onSubmit,
      missing,
      conflict,
      MyDialog,
      model1,
      model2,
      isValid: computed(() => model1.value.length <= 3),
      handleMissingAccept,
      handleMissingReject,
      handleConflictAccept,
      handleConflictReject,
      saveForm,
    };
  },
};
</script>

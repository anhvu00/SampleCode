<template>
    <div v-if="visible" class="dialog-overlay" @click.self="close">
      <div class="dialog-content">
        <div class="dialog-header">
          <h5>Agency JSON</h5>
          <button type="button" class="btn-close" aria-label="Close" @click="close"></button>
        </div>
        <div class="dialog-body">
          <pre>{{ JSON.stringify(agency, null, 2) }}</pre>
        </div>
        <div class="dialog-footer">
          <button type="button" class="btn btn-secondary" @click="close">Close</button>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  const props = defineProps({
    agency: Object,
    visible: Boolean
  })
  
  const emit = defineEmits(['close'])
  
  const close = () => {
    emit('close')
  }
  </script>
  
  <style scoped>
  .dialog-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
  }
  
  .dialog-content {
    background: white;
    border-radius: 8px;
    padding: 20px;
    width: 80%;
    max-height: 80%;
    overflow-y: auto;
  }
  
  .dialog-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .dialog-body {
    margin-top: 10px;
  }
  
  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    margin-top: 20px;
  }
  </style>
  
<template>
  <div class="container mt-4">
    <h1 class="mb-3">eCFR Agencies</h1>

    <!-- Result Display -->
    <div v-if="resultMessage" class="mb-3">
      <span class="result-text">{{ resultMessage }}</span>
    </div>

    <!-- Table -->
    <div class="table-container mb-3">
      <table class="table table-bordered">
        <thead class="table-light">
          <tr>
            <th>Select</th>
            <th>Name</th>
            <th>Short Name</th>
            <th>Title</th>
            <th>Chapter</th>
          </tr>
        </thead>
        <tbody>
          <tr 
            v-for="agency in agencies" 
            :key="agency.slug"
            :class="{ 'table-primary': selectedAgency?.slug === agency.slug }"
          >
            <td class="text-center">
              <input 
                type="radio" 
                name="selectedAgency" 
                @change="() => selectedAgency = agency" 
                :checked="selectedAgency?.slug === agency.slug"
              />
            </td>
            <td>{{ agency.name }}</td>
            <td>{{ agency.short_name }}</td>
            <td>{{ agency.cfr_references[0]?.title || 'N/A' }}</td>
            <td>{{ agency.cfr_references[0]?.chapter || 'N/A' }}</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Buttons -->
    <div class="d-flex gap-3 mb-3">
      <button class="btn btn-primary" @click="handleRefresh">Refresh</button>
      <button class="btn btn-info" @click="handleWordCount">Word Count</button>
      <button class="btn btn-warning" @click="handleChecksum">Checksum</button>
      <button class="btn btn-secondary" @click="handleReview">Review</button>
      <button class="btn btn-success" @click="handleDoge">🐶 Doge</button>
    </div>

    <!-- Review JSON Dialog -->
    <JsonDialog 
      :agency="selectedAgency"
      :visible="showJsonDialog"
      @close="closeJsonDialog"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import JsonDialog from './JsonDialog.vue'

const agencies = ref([])
const selectedAgency = ref(null)
const resultMessage = ref('')
const showJsonDialog = ref(false)

const dogePhrases = [
  "Elon for 2028",
  "Fire them all",
  "Drain the swamp",
  "Make Mars Great Again",
  "In Elon We Trust",
  "Sleep is for the weak",
  "One rocket to rule them all",
  "Free speech or bust",
  "Code. Launch. Repeat.",
  "Mars or bust!",
  "Make Twitter Free Again",
  "Rocket man, but cooler",
  "Elon, the man with the plan",
  "Starships or bust!",
  "Let’s colonize Mars before the government does"
]

const fetchAgencies = async () => {
  try {
    const res = await axios.get('/api/ecfr/agencies')
    agencies.value = res.data.agencies
    selectedAgency.value = agencies.value[0] || null // default selection
    resultMessage.value = "✅ Agencies refreshed"
  } catch (err) {
    console.error('Failed to fetch agencies:', err)
  }
}

const handleRefresh = async () => {
  await fetchAgencies()
}

const handleWordCount = () => {
  if (selectedAgency.value) {
    const wordCount = selectedAgency.value.name.trim().split(/\s+/).length
    resultMessage.value = `📝 Word count of "${selectedAgency.value.name}": ${wordCount}`
  }
}

const handleChecksum = () => {
  if (selectedAgency.value) {
    const checksum = selectedAgency.value.name
      .split('')
      .reduce((sum, char) => sum + char.charCodeAt(0), 0)
    resultMessage.value = `🔢 Checksum of "${selectedAgency.value.name}": ${checksum}`
  }
}

const handleDoge = () => {
  const phrase = dogePhrases[Math.floor(Math.random() * dogePhrases.length)]
  resultMessage.value = `🐶 ${phrase}`
}

const handleReview = () => {
  if (selectedAgency.value) {
    showJsonDialog.value = true
  }
}

const closeJsonDialog = () => {
  showJsonDialog.value = false
}

onMounted(fetchAgencies)
</script>

<style scoped>
.container {
  max-width: 960px;
}

.table-container {
  max-height: 400px;
  overflow-y: auto;
  border: 1px solid #dee2e6;
  border-radius: 4px;
}

.result-text {
  font-weight: bold;
  color: #198754; /* Bootstrap success green */
  font-size: 1.2rem;
}

.modal {
  display: block !important;
}

.modal-dialog {
  max-width: 800px;
}

pre {
  background-color: #f8f9fa;
  padding: 15px;
  border-radius: 5px;
  white-space: pre-wrap;
  word-wrap: break-word;
}
</style>

<template>
  <div class="page">
    <div class="card">
        <button class="btn small" @click="$router.back()">Back</button>
        <h2>Vaccine Details</h2>
        <div v-if="vaccine">
          <p><strong>Name:</strong> {{ vaccine.name }}</p>
          <p><strong>Disease:</strong> {{ vaccine.disease }}</p>
          <p><strong>Total Doses:</strong> {{ vaccine.totalDoses }}</p>
          <p><strong>Gap Between Doses (days):</strong> {{ vaccine.gapBetweenDoses }}</p>
          <p><strong>Booster Required:</strong> {{ vaccine.boosterRequired ? 'Yes' : 'No' }}</p>
          <p v-if="vaccine.boosterRequired"><strong>Booster After (days):</strong> {{ vaccine.boosterAfterDays }}</p>
        </div>
        <div v-else class="muted">Loading...</div>
      </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { getVaccine } from '../services/api'
import { useRoute } from 'vue-router'

const route = useRoute()
const vaccine = ref<any>(null)

async function load() {
  const id = String(route.params.vaccineId)
  try {
    vaccine.value = await getVaccine(id)
  } catch (err) {
    console.error(err)
    alert('Failed to load vaccine')
  }
}

onMounted(load)
</script>

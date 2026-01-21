<template>
  <div class="page">
    <div class="card">
      <button class="btn small" @click="$router.back()">Back</button>
      <h2>Booster Details</h2>
      <div v-if="booster">
        <p><strong>Name:</strong> {{ booster.name }}</p>
        <p><strong>Status:</strong> {{ booster.status }}</p>
        <p v-if="booster.dueDate"><strong>Due Date:</strong> {{ booster.dueDate }}</p>
      </div>
      <div v-else class="muted">Loading...</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const booster = ref<any>(null)

onMounted(async () => {
  // In many APIs booster details are part of user or vaccine.
  // Here we read booster id and show a minimal placeholder.
  const id = String(route.params.boosterId)
  booster.value = { id, name: 'Booster for ' + id, status: 'Pending', dueDate: null }
})
</script>

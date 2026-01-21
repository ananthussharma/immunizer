<template>
  <div class="page auth">
    <div class="card">
      <h1>Create account</h1>
      <form @submit.prevent="onSubmit">
        <label>Full name</label>
        <input v-model="fullName" required />

        <label>Date of birth</label>
        <input v-model="dateOfBirth" type="date" required />

        <label>Gender</label>
        <select v-model="gender" required>
          <option value="">Select</option>
          <option value="Male">Male</option>
          <option value="Female">Female</option>
          <option value="Other">Other</option>
        </select>

        <label>Phone</label>
        <input v-model="phone" type="tel" />

        <label>Email</label>
        <input v-model="email" type="email" required />

        <label>Password</label>
        <input v-model="password" type="password" required />

        <button class="btn" type="submit">Register</button>
      </form>
      <p class="muted">Already registered? <router-link to="/">Login</router-link></p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '../services/api'

const fullName = ref('')
const dateOfBirth = ref('')
const gender = ref('')
const phone = ref('')
const email = ref('')
const password = ref('')
const router = useRouter()

async function onSubmit() {
  try {
    const payload = {
      fullName: fullName.value,
      dateOfBirth: dateOfBirth.value || null,
      gender: gender.value || null,
      email: email.value,
      phone: phone.value || null,
      password: password.value
    }
    // Backend expects RegisterDTO object
    const user = await register(payload)
    if (user && user.id) {
      alert('Registration successful')
      // optionally store basic details
      localStorage.setItem('userId', String(user.id))
      localStorage.setItem('userFullName', user.fullName || '')
      localStorage.setItem('userEmail', user.email || '')
      router.push('/')
    } else {
      alert('Registration returned unexpected response')
    }
  } catch (err: any) {
    alert(err?.response?.data?.message || 'Registration failed')
  }
}
</script>

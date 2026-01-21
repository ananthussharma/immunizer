<template>
  <div class="page auth">
    <div class="card">
      <h1>Sign in</h1>
      <form @submit.prevent="onSubmit">
        <label>Email</label>
        <input v-model="email" type="email" required />

        <label>Password</label>
        <input v-model="password" type="password" required />

        <button class="btn" type="submit">Login</button>
      </form>
      <p class="muted">Don't have an account? <router-link to="/register">Register</router-link></p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '../services/api'

const email = ref('')
const password = ref('')
const router = useRouter()

async function onSubmit() {
  try {
    const user = await login({ email: email.value, password: password.value })
    // Backend returns UserDTO
    if (!user || !user.id) return alert('Invalid login response from server')
    // store basic user details locally for dashboard
    localStorage.setItem('userId', String(user.id))
    localStorage.setItem('userFullName', user.fullName || user.fullName || '')
    localStorage.setItem('userEmail', user.email || '')
    if (user.dateOfBirth) localStorage.setItem('userDob', user.dateOfBirth)
    if (user.gender) localStorage.setItem('userGender', user.gender)
    if (user.phone) localStorage.setItem('userPhone', user.phone)
    router.push('/dashboard')
  } catch (err: any) {
    alert(err?.response?.data?.message || 'Login failed')
  }
}
</script>

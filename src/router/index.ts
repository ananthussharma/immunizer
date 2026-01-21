import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Dashboard from '../views/Dashboard.vue'
import VaccineDetails from '../views/VaccineDetails.vue'
import BoosterDetails from '../views/BoosterDetails.vue'

const routes = [
  { path: '/', name: 'Login', component: Login },
  { path: '/register', name: 'Register', component: Register },
  { path: '/dashboard', name: 'Dashboard', component: Dashboard },
  { path: '/vaccine/:vaccineId', name: 'VaccineDetails', component: VaccineDetails, props: true },
  { path: '/booster/:boosterId', name: 'BoosterDetails', component: BoosterDetails, props: true }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router

import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/vaccinator',
  headers: { 'Content-Type': 'application/json' }
})

export async function login(payload: { email: string; password: string }) {
  // Returns UserDTO (no JWT)
  return api.post('/login', payload).then(r => r.data)
}

export async function register(payload: any) {
  // Returns UserDTO
  return api.post('/register', payload).then(r => r.data)
}

export async function getUser(userId: string) {
  // Returns an object with `administered` (UserVaccineDTO[]) and `upcoming` (array)
  return api.get(`/user/${userId.replace(/^\//, '')}`).then(r => r.data)
}

export async function getVaccine(vaccineId: string) {
  // Returns VaccineDTO
  return api.get(`/vaccine/${vaccineId}`).then(r => r.data)
}

export default api

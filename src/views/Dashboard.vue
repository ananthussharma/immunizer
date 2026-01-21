<template>
  <div class="page dashboard">
    <div class="top">
        <h2>
          Welcome,
          <span class="clickable" @click="toggleProfile">{{ user?.fullName || 'User' }}</span>
          <small class="muted">— {{ ageString }}</small>
        </h2>
        <button class="btn small" @click="logout">Logout</button>
      </div>

      <div v-if="showProfile" class="card">
        <h3>Profile Details</h3>
        <p><strong>Full name:</strong> {{ user?.fullName }}</p>
        <p v-if="user?.dateOfBirth"><strong>DOB:</strong> {{ user.dateOfBirth }}</p>
        <p v-if="user?.gender"><strong>Gender:</strong> {{ user.gender }}</p>
        <p v-if="user?.email"><strong>Email:</strong> {{ user.email }}</p>
        <p v-if="user?.phone"><strong>Phone:</strong> {{ user.phone }}</p>
      </div>

      <div class="grid">
        <div class="card">
          <h3>Administered Vaccines</h3>
          <div v-if="uniqueLatest.length">
            <ul>
              <li v-for="a in pagedLatest" :key="a.vaccineId" class="item" :class="{ soon: a._soon }">
                <div>
                  <strong class="clickable" @click="viewVaccine(a.vaccineId)">{{ vaccineMeta[a.vaccineId]?.name || ('Vaccine #' + a.vaccineId) }}</strong>
                  <div class="muted">Last given: {{ a.administeredDate || '—' }}</div>
                  <div class="muted">Administered by: {{ a.administeredBy || '—' }}</div>
                  <div class="muted" v-if="a._nextDate"><strong>Next dose:</strong> {{ a._nextDate }}</div>
                </div>
                <div class="actions">
                  <button class="btn tiny" @click="showHistory(a.vaccineId)">Details</button>
                </div>
              </li>
            </ul>

            <div class="muted" style="margin-top:8px">
              <button class="btn tiny" v-if="!showAll" @click="showAll = true">See all</button>
              <button class="btn tiny" v-else @click="showAll = false">Show less</button>
            </div>

            <div v-if="showAll" class="pagination" style="margin-top:8px;display:flex;gap:8px;align-items:center">
              <button class="btn tiny" :disabled="currentPage===1" @click="currentPage--">Prev</button>
              <div class="muted">Page {{ currentPage }} / {{ totalPages }}</div>
              <button class="btn tiny" :disabled="currentPage===totalPages" @click="currentPage++">Next</button>
            </div>

          </div>
          <div v-else class="muted">No vaccine records found.</div>
        </div>

        <div class="card">
          <h3>Upcoming Vaccines</h3>
          <div v-if="upcoming?.length">
            <ul>
              <li v-for="u in upcoming" :key="u.vaccineId" class="item">
                <div>
                  <strong class="clickable" @click="viewVaccine(u.vaccineId)">{{ u.name }}</strong>
                  <div class="muted">Next dose #: {{ u.nextDoseNumber }} — {{ u.nextDoseDate || 'TBD' }}</div>
                  <div class="muted" v-if="u._place"><strong>Place:</strong> {{ u._place }}</div>
                </div>
                <div class="actions">
                  <button class="btn tiny" @click="showUpcomingDetails(u.vaccineId)">Details</button>
                </div>
              </li>
            </ul>
          </div>
          <div v-else class="muted">No upcoming vaccines.</div>
        </div>
      </div>

      <div v-if="selectedVaccineId && selectedHistory.length" class="card">
        <h3>History for {{ vaccineMeta[selectedVaccineId]?.name || ('Vaccine #' + selectedVaccineId) }}</h3>
        <div>
          <div v-if="selectedHistory.length">
            <div class="highlight" style="margin-bottom:8px">
              <strong>Latest — Dose {{ selectedHistory[0].doseNumber }}: {{ selectedHistory[0].administeredDate }}</strong>
              <div class="muted">Administered by: {{ selectedHistory[0].administeredBy || '—' }}</div>
              <div class="muted">Notes: {{ selectedHistory[0].notes || '—' }}</div>
            </div>
            <div v-if="selectedHistory.length>1">
              <h4>Previous doses</h4>
              <div v-for="(h, idx) in selectedHistory.slice(1)" :key="h.id" style="margin-bottom:8px">
                <div>
                  <strong>Dose {{ h.doseNumber }} — {{ h.administeredDate }}</strong>
                  <div class="muted">Administered by: {{ h.administeredBy || '—' }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div v-if="selectedUpcoming && selectedUpcomingId" class="card">
        <h3>Upcoming: {{ selectedUpcoming.name }}</h3>
        <p><strong>Vaccine:</strong> {{ selectedUpcoming.name }}</p>
        <p><strong>Disease:</strong> {{ selectedUpcoming.disease }}</p>
        <p><strong>Next dose #:</strong> {{ selectedUpcoming.nextDoseNumber }}</p>
        <p><strong>Next dose date:</strong> {{ selectedUpcoming.nextDoseDate || 'TBD' }}</p>
        <p v-if="selectedUpcoming._place"><strong>Place for booster:</strong> {{ selectedUpcoming._place }}</p>
      </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { getUser, getVaccine } from '../services/api'

const router = useRouter()
// user info comes from login/register responses and is stored locally
const user = ref<any>({
  id: localStorage.getItem('userId'),
  fullName: localStorage.getItem('userFullName'),
  email: localStorage.getItem('userEmail'),
  dateOfBirth: localStorage.getItem('userDob'),
  gender: localStorage.getItem('userGender'),
  phone: localStorage.getItem('userPhone')
})
const administered = ref<any[]>([])
const upcoming = ref<any[]>([])
const vaccineMeta: Record<string, any> = {}
const showProfile = ref(false)
const showAll = ref(false)
const pageSize = 5
const currentPage = ref(1)
const selectedVaccineId = ref<number | null>(null)
const selectedUpcomingId = ref<number | null>(null)
const selectedUpcoming = ref<any | null>(null)

const ageString = computed(() => {
  const dob = user.value?.dateOfBirth
  if (!dob) return ''
  try {
    const d = new Date(dob)
    const diff = Date.now() - d.getTime()
    const years = Math.floor(diff / (1000 * 60 * 60 * 24 * 365.25))
    return `${years} yrs`
  } catch (e) { return '' }
})

function toggleProfile() { showProfile.value = !showProfile.value }

function selectVaccine(vaccineId: number) {
  selectedVaccineId.value = vaccineId
}

function viewVaccine(vaccineId: number) {
  // navigate to vaccine details page
  router.push(`/vaccine/${vaccineId}`)
}

function showHistory(vaccineId: number) {
  selectedVaccineId.value = vaccineId
  selectedUpcomingId.value = null
  selectedUpcoming.value = null
}

function showUpcomingDetails(vaccineId: number) {
  selectedUpcomingId.value = vaccineId
  selectedVaccineId.value = null
  selectedUpcoming.value = upcoming.value.find((u: any) => Number(u.vaccineId) === Number(vaccineId)) || null
}

const uniqueLatest = computed(() => {
  // group by vaccineId and pick the last administered (by administeredDate or by id)
  const map = new Map<number, any>()
  administered.value.forEach((a: any) => {
    const id = Number(a.vaccineId)
    const existing = map.get(id)
    if (!existing) { map.set(id, a); return }
    const aDate = a.administeredDate ? new Date(a.administeredDate) : null
    const eDate = existing.administeredDate ? new Date(existing.administeredDate) : null
    if (aDate && eDate) {
      if (aDate > eDate) map.set(id, a)
    } else if (!eDate && aDate) {
      map.set(id, a)
    } else if (!aDate && !eDate) {
      if ((a.doseNumber || 0) >= (existing.doseNumber || 0)) map.set(id, a)
    }
  })
  const arr = Array.from(map.values())
  arr.sort((x: any, y: any) => {
    const xd = x.administeredDate ? new Date(x.administeredDate).getTime() : 0
    const yd = y.administeredDate ? new Date(y.administeredDate).getTime() : 0
    return yd - xd
  })
  return arr
})

const totalPages = computed(() => Math.max(1, Math.ceil(uniqueLatest.value.length / pageSize)))
const pagedLatest = computed(() => {
  const arr = uniqueLatest.value
  if (!showAll.value) return arr.slice(0, pageSize)
  const p = Math.min(Math.max(1, currentPage.value), totalPages.value)
  const start = (p - 1) * pageSize
  return arr.slice(start, start + pageSize)
})

const selectedHistory = computed(() => {
  if (selectedVaccineId.value == null) return []
  return administered.value
    .filter((a: any) => Number(a.vaccineId) === Number(selectedVaccineId.value))
    .slice()
    .sort((x: any, y: any) => {
      const xd = x.administeredDate ? new Date(x.administeredDate).getTime() : 0
      const yd = y.administeredDate ? new Date(y.administeredDate).getTime() : 0
      return yd - xd
    })
})

function logout() {
  localStorage.removeItem('userId')
  localStorage.removeItem('token')
  router.push('/')
}

async function load() {
  const userId = localStorage.getItem('userId')
  if (!userId) return router.push('/')
  try {
    // GET /user/{userId} returns { administered: UserVaccineDTO[], upcoming: [] }
    const data = await getUser(userId)
    administered.value = Array.isArray(data?.administered) ? data.administered : []
    upcoming.value = Array.isArray(data?.upcoming) ? data.upcoming : []

    // fetch vaccine metadata for unique vaccineIds
    const ids = Array.from(new Set(administered.value.map((a: any) => a.vaccineId).concat(upcoming.value.map((u: any) => u.vaccineId))))
    await Promise.all(ids.map(async (id: any) => {
      try {
        const meta = await getVaccine(String(id))
        vaccineMeta[String(id)] = meta
      } catch (e) {
        // ignore fetch error
      }
    }))

    // compute nextDoseDate for administered entries using vaccine gapBetweenDoses
    const today = new Date()
    administered.value.forEach((a: any) => {
      const meta = vaccineMeta[String(a.vaccineId)]
      if (a.administeredDate && meta?.gapBetweenDoses) {
        const adm = new Date(a.administeredDate)
        const nd = new Date(adm)
        nd.setDate(nd.getDate() + Number(meta.gapBetweenDoses))
        a._nextDate = nd.toISOString().slice(0, 10)
        const diffDays = Math.ceil((nd.getTime() - today.getTime()) / (1000 * 60 * 60 * 24))
        a._soon = diffDays >= 0 && diffDays <= 100
      } else {
        a._nextDate = a.nextDoseDate || null
        a._soon = false
      }
    })

    // for upcoming entries, if it's a booster (alreadyAdministered true) attach place from last administered
    upcoming.value.forEach((u: any) => {
      const last = administered.value.slice().reverse().find((a: any) => a.vaccineId === u.vaccineId)
      if (last?.administeredBy) u._place = last.administeredBy
    })
    // recompute pagination bounds
    if (currentPage.value > totalPages.value) currentPage.value = totalPages.value
  } catch (err) {
    console.error(err)
    alert('Failed to load vaccine data')
  }
}

onMounted(load)
</script>

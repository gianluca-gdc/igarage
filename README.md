# iGarage
*A Kotlin Multiplatform automotive companion app*

iGarage is a Kotlin Multiplatform (KMM) project built with **Compose Multiplatform**, designed to give car enthusiasts a clean, powerful way to track their vehicles, maintenance schedules, market values, and overall vehicle health — all in one garage.

This project is structured for future **iOS support**, while the current development focuses on Android.

---

## Features (In Progress)

### Garage Overview
- Add vehicles by VIN
- Fetch vehicle metadata (make, model, year, trim)
- Display image cards for each car
- Show mileage, estimated market value, and 6-month price trends
- API-backed image loading (Coil) with caching coming soon

### Vehicle Detail Screen (WIP)
- Expanded vehicle information
- Health scoring system
- Upcoming / overdue maintenance preview
- Navigation to full maintenance history
- High-quality vehicle image display

### Maintenance Tracking
- Pull manufacturer / condition-based maintenance schedules
- Mark items as completed
- Track next due intervals by mileage or date
- Categorized tasks (Critical / Major / Minor)

### Parts Lookup (Planned)
- VIN-based part search
- Web-scraped pricing comparison
- OEM-only filter
- Review/rating aggregation

---

## Architecture

iGarage uses a **clean, testable modular structure**:
### Tech Stack

- **Kotlin Multiplatform**
- **Compose Multiplatform**
- **Voyager** for navigation
- **Ktor** for networking
- **Coil** for image loading
- **VehicleDatabases.com API** (VIN decode, maintenance, market value)
- **ServiceLocator** (manual DI)
- **Coroutines** + StateScreenModel for state management

---

## Getting Started

### 1. Add your API key
Create `local.properties`: your local api key here
### 2. Build & run on Android
Open in Android Studio → Run.

iOS target will be added later.

---

## Status
Actively in development.  
Vehicle management + basic detail screen logic complete.  
Maintenance + parts search next.

---

##  Author
**Gianluca Cutugno**  
Computer Science @ SUNY New Paltz  
GitHub: https://github.com/gianluca-gdc

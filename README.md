# iGarage

I spent five years as a lead technician at multiple dealerships. One thing that 
always bothered me was how scattered vehicle information is — maintenance history 
lives in the shop's system, market value is on some third party site, parts lookup 
is a whole separate thing. iGarage is an attempt to put all of that in one place.

Built with Kotlin Multiplatform and Compose Multiplatform, Android first.

---

## What it does

You add a vehicle by VIN and the app pulls the full decode — make, model, year, 
trim, engine. From there you get a running maintenance schedule based on 
manufacturer intervals, a health score, mileage tracking, and estimated market 
value with a 6-month price trend. The goal is a single screen that tells you 
everything you need to know about a car at a glance.

Parts lookup is in progress — VIN-based search with pricing pulled from multiple 
sources so you're not just getting one quote.

---

## Stack

- Kotlin Multiplatform / Compose Multiplatform
- Voyager for navigation
- Ktor for networking
- Coil for image loading
- VehicleDatabases.com API for VIN decode, maintenance schedules, and market data
- Coroutines + StateScreenModel for state management

---

## Running it

Add your API key to `local.properties`, open in Android Studio, run. iOS target 
is set up but not the current focus.

---

## Status

Vehicle management and detail screen are working. Maintenance tracking is next, 
then parts lookup. WIP...

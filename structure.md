# Project Structure

This document outlines the directory structure and key components of the DoseCalculator Android project.

## Directory Tree

```text
DoseCalculator/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/skul9x/dosecalculator/
│   │   │   │   ├── Drug.kt              # Data class for medication
│   │   │   │   ├── DrugAdapter.kt       # Adapter for ManageDrugs list
│   │   │   │   ├── DrugPersistence.kt   # Logic for saving/loading drugs
│   │   │   │   ├── MainActivity.kt      # Main calculation screen
│   │   │   │   └── ManageDrugsActivity.kt # Screen for drug management
│   │   │   ├── res/                     # UI resources (layouts, drawables, etc.)
│   │   │   └── AndroidManifest.xml      # App manifest
│   ├── build.gradle.kts                 # App-level build configuration
│   └── proguard-rules.pro               # ProGuard configuration
├── build.gradle.kts                     # Project-level build configuration
├── settings.gradle.kts                  # Project settings
└── README.md                            # Project overview
```

## Key Components

### 1. Data Model (`Drug.kt`)
A simple `Serializable` data class that holds the properties of a medication:
- `name`: Name of the drug.
- `mg`: Concentration in milligrams.
- `ml`: Volume in milliliters.
- `dose`: Recommended dosage rate (e.g., mg/kg).

### 2. Persistence Layer (`DrugPersistence.kt`)
Handles the storage of the drug list. It uses `SharedPreferences` to store data as a JSON string via the `Gson` library. It also provides a default list of medications if no data is found.

### 3. Main Screen (`MainActivity.kt`)
The primary interface where users perform calculations. It features:
- `AutoCompleteTextView` for drug selection.
- `TextWatcher` for real-time calculation.
- `ChipGroup` for selecting dosage frequency.

### 4. Management Screen (`ManageDrugsActivity.kt`)
Allows users to customize their medication list. It uses a `RecyclerView` with `DrugAdapter` to display and interact with the drug data.

### 5. UI Resources (`res/`)
- `layout/`: XML files defining the visual structure of activities.
- `values/`: Strings, colors, and styles used throughout the app.

# DoseCalculator

DoseCalculator is a simple and efficient Android application designed to help healthcare professionals and caregivers calculate pediatric medication dosages based on a child's weight.

## 🚀 Features

- **Quick Calculation**: Automatically calculates the dose per administration and total daily dose based on weight, concentration (mg/ml), and dosage rate.
- **Drug Management**: Pre-loaded with common pediatric medications. Users can add, edit, or delete drugs from the list.
- **Dynamic UI**: Real-time calculation as you type or change parameters.
- **Persistent Storage**: Saves your custom drug list locally using SharedPreferences and Gson.
- **User-Friendly Interface**: Clean design with Material Design components like Chips and AutoCompleteTextView.

## 🛠 Technology Stack

- **Language**: Kotlin
- **Platform**: Android SDK
- **UI Framework**: XML with ViewBinding
- **Data Persistence**: SharedPreferences + Gson
- **Architecture**: Activity-based with a dedicated persistence layer.

## 📦 Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/DoseCalculator.git
   ```
2. Open the project in **Android Studio**.
3. Sync the project with Gradle files.
4. Run the app on an emulator or a physical Android device.

## 📖 Usage

1. **Select a Drug**: Start typing the drug name in the search box or select from the dropdown.
2. **Enter Weight**: Input the patient's weight in kilograms.
3. **Adjust Parameters**: (Optional) Modify the concentration (mg/ml) or dosage rate if necessary.
4. **Choose Frequency**: Select the number of doses per day using the chips (1x, 2x, 3x, 4x).
5. **View Results**: The calculated dose per administration and total daily dose will appear instantly at the bottom.
6. **Manage Drugs**: Click the "Manage Drugs" button to customize your medication list.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

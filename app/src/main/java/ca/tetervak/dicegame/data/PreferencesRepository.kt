package ca.tetervak.dicegame.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException

class PreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        const val DEFAULT_NUMBER_OF_DICE = 3
        const val TAG = "PreferencesRepository"
    }

    private val numberOfDiceKey = intPreferencesKey("number_of_dice")

    private val numberOfDice: Flow<Int> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[numberOfDiceKey] ?: DEFAULT_NUMBER_OF_DICE
        }

    fun getNumberOfDiceFlow(): Flow<Int> = numberOfDice

    suspend fun saveNumberOfDice(numberOfDice: Int) {
        withContext(context = Dispatchers.IO){
            dataStore.edit { preferences ->
                preferences[numberOfDiceKey] = numberOfDice
            }
        }
    }

}